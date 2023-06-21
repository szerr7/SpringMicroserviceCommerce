package edu.miu.inventoryservice.repository;

import edu.miu.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

//
//    @Query("SELECT s From Inventory s WHERE s.skuCode  = :skuCode")
//     Optional<Inventory> findBySkuCode(String skuCode);


    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
