package com.crm.inventory.controller;

import com.crm.inventory.event.StockChangedEvent;
import com.crm.inventory.event.StockListener;
import com.crm.inventory.model.InventoryRecord;
import com.crm.inventory.repository.InventoryRecordRepository;

import com.crm.inventory.mediator.RestockMediator;

//warehouse controller
// concrete observer
public class InventoryController implements StockListener {
    private final InventoryRecordRepository inventoryRecordRepository;
    private RestockMediator mediator;

    public InventoryController(InventoryRecordRepository inventoryRecordRepository) {
        this.inventoryRecordRepository = inventoryRecordRepository;
    }

    public void setRestockMediator(RestockMediator mediator) {
        this.mediator = mediator;
    }

    public void createInventoryRecord(InventoryRecord inventoryRecord) {
        inventoryRecordRepository.save(inventoryRecord.getRecordId(), inventoryRecord);
    }

    public InventoryRecord getInventoryRecord(String recordId) {
        return inventoryRecordRepository.findById(recordId);
    }

    public void increaseInventory(String recordId, int quantity) {
        InventoryRecord record = inventoryRecordRepository.findById(recordId);
        if (record == null) {
            return;
        }
        record.increase(quantity);
        inventoryRecordRepository.update(recordId, record);
    }

    public void decreaseInventory(String recordId, int quantity) {
        InventoryRecord record = inventoryRecordRepository.findById(recordId);
        if (record == null) {
            return;
        }
        record.decrease(quantity);
        inventoryRecordRepository.update(recordId, record);

        if (isBelowReorderLevel(recordId) && mediator != null) {
            mediator.onLowStock(recordId, record.getProductId());
        }
    }

    public boolean isBelowReorderLevel(String recordId) {
        InventoryRecord record = inventoryRecordRepository.findById(recordId);
        return record != null && record.isBelowReorderLevel();
    }

    public void deleteInventoryRecord(String recordId) {
        inventoryRecordRepository.delete(recordId);
    }

    // When a Product deduces its stock (catalogue level), this ensures the
    // warehouse (InventoryRecord) stays perfectly synchronized without manual
    // duplicate calls in the client code.

    @Override
    public void onStockChanged(StockChangedEvent event) {
        InventoryRecord record = inventoryRecordRepository.findByProductId(event.getProductId());
        if (record != null && record.getAvailableQuantity() != event.getNewQuantity()) {
            // Calculate the difference and explicitly apply it to avoid infinite loops
            // if the event originated from InventoryRecord itself.
            int diff = record.getAvailableQuantity() - event.getNewQuantity();
            if (diff > 0) {
                System.out.println("Auto-syncing warehouse:" + record.getWarehouseName() + " Decreasing record "
                        + record.getRecordId() + " by " + diff);
                decreaseInventory(record.getRecordId(), diff);
            }
        }
    }
}