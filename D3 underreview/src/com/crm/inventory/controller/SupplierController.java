package com.crm.inventory.controller;

import com.crm.inventory.event.StockChangedEvent;
import com.crm.inventory.event.StockObserver;
import com.crm.inventory.model.PurchaseOrder;
import com.crm.inventory.model.Supplier;
import com.crm.inventory.repository.PurchaseOrderRepository;
import com.crm.inventory.repository.SupplierRepository;
import com.crm.inventory.service.SupplierDataService;

// concrete Observer/subscriber 
public class SupplierController {

    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierDataService supplierDataService;

    public SupplierController(SupplierRepository supplierRepository) {
        this(supplierRepository, null, null);
    }

    // Constructor with ERP adapter but no PO repository.
    public SupplierController(SupplierRepository supplierRepository, SupplierDataService supplierDataService) {
        this(supplierRepository, supplierDataService, null);
    }

    // Full constructor — enables both ERP operations and purchase order
    // supplierRepository CRM supplier store
    // purchaseOrderRepository CRM
    public SupplierController(SupplierRepository supplierRepository, SupplierDataService supplierDataService,
            PurchaseOrderRepository purchaseOrderRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierDataService = supplierDataService;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    // CRUD methods

    public void createSupplier(Supplier supplier) {
        supplierRepository.save(supplier.getSupplierId(), supplier);
    }

    public Supplier getSupplier(String supplierId) {
        return supplierRepository.findById(supplierId);
    }

    public void updateSupplier(Supplier supplier) {
        supplierRepository.update(supplier.getSupplierId(), supplier);
    }

    public void deleteSupplier(String supplierId) {
        supplierRepository.delete(supplierId);
    }

    // ERP methods

    // Fetches a supplier from the external ERP system and saves it into the local

    // supplierId the ERP-side supplierid
    public void importFromErp(String supplierId) {
        if (!isErpAvailable())
            System.out.println("supplier id cant be importeted");
        ;
        Supplier supplier = supplierDataService.fetchSupplier(supplierId); // through target interface
        supplierRepository.save(supplier.getSupplierId(), supplier);
        System.out.println("Imported supplier " + supplierId + " from ERP into CRM.");
    }

    // sends an existing CRM supplier to the external ERP system via the target
    // supplierId the CRM supplier identifier to look up and export
    public void exportToErp(String supplierId) {
        if (!isErpAvailable())
            return;
        Supplier supplier = supplierRepository.findById(supplierId);
        if (supplier == null) {
            System.out.println("[ Controller ] Supplier " + supplierId + " not found in CRM.");
            return;
        }
        supplierDataService.pushSupplier(supplier); // through target interface
        System.out.println("[ Controller ] Exported supplier " + supplierId + " from CRM to ERP.");
    }

    // Purchase Order — ERP integration

    // Fetches a purchase order from the ERP via the target interface and, saves it
    // poId the ERP purchase order identifier
    // returns the PurchaseOrder from ERP

    public PurchaseOrder importPoFromErp(String poId) {
        if (!isErpAvailable())
            return null;
        PurchaseOrder po = supplierDataService.fetchPurchaseOrder(poId);
        if (purchaseOrderRepository != null) {
            purchaseOrderRepository.save(po.getPurchaseOrderId(), po);
            System.out.println("[ Controller ] PO " + poId + " imported from ERP and saved to CRM.");
        } else {
            System.out.println("[ Controller ] PO " + poId + " imported from ERP (not persisted — no PO repository).");
        }
        return po;
    }

    // Looks up a purchase order from the local CRM repository and sends it to ERP
    // poId the CRM purchase order identifier to export

    public void exportPoToErp(String poId) {
        if (!isErpAvailable())
            return;
        if (purchaseOrderRepository == null) {
            System.out.println("Cannot export PO — no PurchaseOrderRepository configured.");
            return;
        }
        PurchaseOrder po = purchaseOrderRepository.findById(poId);
        if (po == null) {
            System.out.println("PO " + poId + " not found in CRM.");
            return;
        }
        supplierDataService.pushPurchaseOrder(po);
        System.out.println("PO " + poId + " exported from CRM to ERP.");
    }

    // Helper — returns true if ERP adapter is configured, false (with message)
    private boolean isErpAvailable() {
        if (supplierDataService == null) {
            System.out.println("""
                    No ERP adapter configured.
                    Use SupplierController(repo, new ErpSupplierAdapter()) to enable ERP operations.""");
            return false;
        }
        return true;
    }

    public void pushPurchaseOrder(PurchaseOrder po) {
        if (!isErpAvailable()) {
            System.out.println("Cannot push PO — ERP not available.");
            return;
        }
        supplierDataService.pushPurchaseOrder(po);
        System.out.println("PO " + po.getPurchaseOrderId() + " pushed from CRM to ERP.");
    }
}
