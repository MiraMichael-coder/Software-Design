package com.crm.inventory.service;

import com.crm.common.Money;
import com.crm.inventory.external.ErpSystem;
import com.crm.inventory.model.PurchaseOrder;
import com.crm.inventory.model.Supplier;

// Adapter that bridges the external ErpSystem API to the CRM's SupplierDataService interface.

public class ErpSupplierAdapter implements SupplierDataService {

    private final ErpSystem erpSystem; // adaptee

    public ErpSupplierAdapter() {
        this.erpSystem = new ErpSystem();
    }

    // Supplier operations

    // Fetches supplier data from the ERP system and translates it into a
    // CRM-compatible Supplier object.
    // ERP array layout: [0]=id · [1]=name · [2]=email · [3]=phone. Address is set
    // to null as the ERP does not provide it.

    @Override
    public Supplier fetchSupplier(String supplierId) {
        String[] data = erpSystem.getErpSupplierData(supplierId);
        return new Supplier(
                data[0], // supplierId
                data[1], // supplierName
                data[1], // contactName (defaulted to supplier name)
                data[2], // contactEmail
                data[3], // contactPhone
                null // address — not provided by ERP
        );
    }

    // Pushes a CRM Supplier to the ERP.
    @Override
    public void pushSupplier(Supplier supplier) {
        erpSystem.submitSupplierRecord(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getContactEmail());
    }

    // Purchase Order operations

    // gets a purchase order from the ERP system .
    // ERP array layout:[0]=poId · [1]=supplierId · [2]=productId · [3]=qty ·
    // [4]=amount · [5]=currency
    @Override
    public PurchaseOrder fetchPurchaseOrder(String poId) {
        String[] data = erpSystem.getErpPurchaseOrder(poId);
        Money totalCost = new Money(Double.parseDouble(data[4]), data[5]);
        return new PurchaseOrder(
                data[0], // purchaseOrderId
                data[1], // supplierId
                data[2], // productId
                Integer.parseInt(data[3]), // quantity
                totalCost // totalCost
        );
    }

    // sends a CRM PurchaseOrder to the ERP.
    @Override
    public void pushPurchaseOrder(PurchaseOrder purchaseOrder) {
        String totalCostStr = purchaseOrder.getTotalCost().getAmount() + " "
                + purchaseOrder.getTotalCost().getCurrency();
        erpSystem.submitPurchaseOrder(
                purchaseOrder.getPurchaseOrderId(),
                purchaseOrder.getSupplierId(),
                purchaseOrder.getProductId(),
                purchaseOrder.getQuantity(),
                totalCostStr);
    }
}
