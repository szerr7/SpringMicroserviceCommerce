package edu.miu.controller;


import edu.miu.dto.OrderRequest;
import edu.miu.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
         orderService.placeOrder(orderRequest);
            return "Order Placed Successfully";
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name = "inventory" , fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name = "inventory")
//    @Retry(name = "inventory")
//    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
//        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
//
//    }


//    public CompletableFuture<String>fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
//        return CompletableFuture.supplyAsync(()->"Oops! Something went wrong. Please try again later");
//    }

    public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return "Oops! Something went wrong. Please try again later";
    }

}
