package com.crm.inventory.controller;

import com.crm.inventory.model.Product;
import com.crm.inventory.model.PurchaseOrder;
import com.crm.inventory.repository.ProductRepository;
import com.crm.inventory.repository.PurchaseOrderRepository;

public class PurchaseOrderController {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    public PurchaseOrderController(PurchaseOrderRepository purchaseOrderRepository, ProductRepository productRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
    }

    public void createPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderRepository.save(purchaseOrder.getPurchaseOrderId(), purchaseOrder);
    }

    public PurchaseOrder getPurchaseOrder(String purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId);
    }

    public void receivePurchaseOrder(String purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);
        if (purchaseOrder == null || purchaseOrder.isReceived()) {
            return;
        }

        Product product = productRepository.findById(purchaseOrder.getProductId());
        if (product != null) {
            product.increaseStock(purchaseOrder.getQuantity());
            productRepository.update(product.getProductId(), product);
        }

        purchaseOrder.markReceived();
        purchaseOrderRepository.update(purchaseOrderId, purchaseOrder);
    }

    public void deletePurchaseOrder(String purchaseOrderId) {
        purchaseOrderRepository.delete(purchaseOrderId);
    }
}
