package com.crm.inventory.service;

import com.crm.inventory.model.PurchaseOrder;
import com.crm.inventory.model.Supplier;

/*
 Target interface for the Adapter pattern.
 Defines the unified contract that the CRM system uses to interact with any supplier data source — including ERP systems.
*/
public interface SupplierDataService {

    // Fetch a supplier record by its ID.
    Supplier fetchSupplier(String supplierId);

    // Push / submit a supplier record to the underlying data source.
    void pushSupplier(Supplier supplier);

    // Purchase Order operations

    // get a purchase order from the external data source by its ID.
    PurchaseOrder fetchPurchaseOrder(String poId);

    // send a purchase order to the external data source.
    void pushPurchaseOrder(PurchaseOrder purchaseOrder);
}
