package edu.miu.service;

import edu.miu.dto.InventoryResponse;
import edu.miu.dto.OrderLineItemsDto;
import edu.miu.dto.OrderRequest;
import edu.miu.model.Order;
import edu.miu.model.OrderLineItems;
import edu.miu.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;


    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }



    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToDto).toList();

        order.setOrderLineItems(orderLineItems);



        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //call inventory service to check if the items are in stock
        InventoryResponse [] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory" , uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);


        if (allProductsInStock){
            orderRepository.save(order);
        }


        else{
            throw new RuntimeException("Items are not in stock");
        }



    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;

    }


}
