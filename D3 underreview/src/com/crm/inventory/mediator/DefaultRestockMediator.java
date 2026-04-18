package com.crm.inventory.mediator;

import com.crm.alert.controller.InventoryAlertController;
import com.crm.inventory.controller.PurchaseOrderController;
import com.crm.inventory.controller.SupplierController;
import com.crm.inventory.model.Product;
import com.crm.inventory.model.PurchaseOrder;
import com.crm.inventory.repository.ProductRepository;

public class DefaultRestockMediator implements RestockMediator {
    private final InventoryAlertController inventoryAlertController;
    private final PurchaseOrderController purchaseOrderController;
    private final SupplierController supplierController;
    private final ProductRepository productRepository;

    public DefaultRestockMediator(
            InventoryAlertController inventoryAlertController,
            PurchaseOrderController purchaseOrderController,
            SupplierController supplierController,
            ProductRepository productRepository) {
        this.inventoryAlertController = inventoryAlertController;
        this.purchaseOrderController = purchaseOrderController;
        this.supplierController = supplierController;
        this.productRepository = productRepository;
    }

    @Override
    public void onLowStock(String recordId, String productId) {
        // 1. Create a LowStockAlert via InventoryAlertController
        String alertId = "ALT-AUTO-" + productId + "-" + System.currentTimeMillis();
        inventoryAlertController.processAlert(alertId, productId).notifyTarget();

        // 2. Auto-generate a PurchaseOrder via PurchaseOrderController
        Product product = productRepository.findById(productId);
        if (product != null) {
            String poId = "PO-AUTO-" + productId + "-" + System.currentTimeMillis();
            PurchaseOrder autoPo = new PurchaseOrder(
                    poId, product.getSupplierId(), productId, 50,
                    product.getUnitPrice().multiply(50));

            purchaseOrderController.createPurchaseOrder(autoPo);

            // 3. Push the PO to the external ERP system via SupplierController
            supplierController.pushPurchaseOrder(autoPo);
        } else {
            System.out.println("Mediator could not auto-reorder: product not found for ID: " + productId);
        }
    }
}
