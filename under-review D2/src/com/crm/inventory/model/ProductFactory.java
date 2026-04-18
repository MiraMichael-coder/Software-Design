package com.crm.inventory.model;

import com.crm.common.Money;
import java.util.HashMap;
import java.util.Map;

//Flyweight Pattern
public class ProductFactory {
    private static Map<String, Product> cache = new HashMap<>();

    public static Product getOrCreate(String productId, String name, String description,
            Money unitPrice, int stockQuantity, String supplierId) {
        if (cache.containsKey(productId)) {
            System.out.println("[ Flyweight ] Returning existing Product: " + productId);
            return cache.get(productId);
        } else {
            System.out.println("[ Flyweight ] Creating new Product: " + productId);
            Product product = new Product(productId, name, description, unitPrice, stockQuantity, supplierId);
            cache.put(productId, product);
            return product;
        }
    }

    public static Product get(String productId) {
        return cache.get(productId);
    }
}
