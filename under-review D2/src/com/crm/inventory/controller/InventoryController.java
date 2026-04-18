package com.crm.inventory.controller;

import com.crm.inventory.model.InventoryRecord;
import com.crm.inventory.repository.InventoryRecordRepository;

public class InventoryController {
    private final InventoryRecordRepository inventoryRecordRepository;

    public InventoryController(InventoryRecordRepository inventoryRecordRepository) {
        this.inventoryRecordRepository = inventoryRecordRepository;
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
    }

    public boolean isBelowReorderLevel(String recordId) {
        InventoryRecord record = inventoryRecordRepository.findById(recordId);
        return record != null && record.isBelowReorderLevel();
    }

    public void deleteInventoryRecord(String recordId) {
        inventoryRecordRepository.delete(recordId);
    }
}
