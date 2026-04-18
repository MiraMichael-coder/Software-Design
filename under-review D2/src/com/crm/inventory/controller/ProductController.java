package com.crm.inventory.controller;

import com.crm.inventory.model.Product;
import com.crm.inventory.repository.ProductRepository;

public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(Product product) {
        productRepository.save(product.getProductId(), product);
    }

    public Product getProduct(String productId) {
        return productRepository.findById(productId);
    }

    public void restockProduct(String productId, int quantity) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            return;
        }
        product.increaseStock(quantity);
        productRepository.update(productId, product);
    }

    public void deductStock(String productId, int quantity) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            return;
        }
        product.decreaseStock(quantity);
        productRepository.update(productId, product);
    }

    public void updateProduct(Product product) {
        productRepository.update(product.getProductId(), product);
    }

    public void deleteProduct(String productId) {
        productRepository.delete(productId);
    }
}
