package com.crm.inventory.external;

import java.util.HashMap;
import java.util.Map;

public class ErpSystem { //Adaptee ERP system

    // Simulated ERP supplier database — different data per supplier ID
    // [0] = supplier ID, [1] = supplier name, [2] = contact email, [3] = contact phone
    private static final Map<String, String[]> ERP_SUPPLIERS = new HashMap<>();

    static {
        ERP_SUPPLIERS.put("S001", new String[]{"S001", "Acme Corp",         "acme@supply.com",    "0111000001"});
        ERP_SUPPLIERS.put("S002", new String[]{"S002", "Global Goods Ltd",  "global@goods.com",   "0111000002"});
        ERP_SUPPLIERS.put("S003", new String[]{"S003", "Delta Distributors","delta@distrib.com",  "0111000003"});
        ERP_SUPPLIERS.put("S004", new String[]{"S004", "Prime Parts Inc",   "prime@parts.com",    "0111000004"});
    }

    //assume its an api call to the erp system
    //returns: [0] = supplier ID, [1] = supplier name, [2] = contact email, [3] = contact phone
    public String[] getErpSupplierData(String id) {
        System.out.println("[ ERP ] Fetching supplier: " + id);
        if (ERP_SUPPLIERS.containsKey(id)) {
            return ERP_SUPPLIERS.get(id);
        }
        // fallback for unknown IDs — simulates ERP generating a generic record
        System.out.println("[ ERP ] Supplier " + id + " not in catalogue, returning generated record.");
        return new String[]{ id, "ERP-Supplier-" + id, id.toLowerCase() + "@erp.com", "0100000000" };
    }

    //Submits a supplier record to the ERP system: id, name, email
    public void submitSupplierRecord(String id, String name, String email) {
        System.out.println("[ ERP ] Submitting supplier " + id + ": " + name + ", " + email);
    }

    // Purchase Order operations
    //[0] = purchase order ID, [1] = supplier ID, [2] = product ID, [3] = quantity, [4] = total cost amount, [5] = currency code

    public String[] getErpPurchaseOrder(String poId) {
        System.out.println("[ ERP ] Fetching purchase order: " + poId);
        return new String[]{ poId, "S001", "PROD-01", "100", "5000.00", "USD" };
    }

    //Submits a new purchase order to the ERP system: poId, supplierId, productId, quantity, totalCost
    public void submitPurchaseOrder(String poId, String supplierId,
                                    String productId, int quantity, String totalCost) {
        System.out.println("[ ERP ] Submitting purchase order " + poId
                + " | Supplier: " + supplierId
                + " | Product: " + productId
                + " | Qty: " + quantity
                + " | Cost: " + totalCost);
    }
}
