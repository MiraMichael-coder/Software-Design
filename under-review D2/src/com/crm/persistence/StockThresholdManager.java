package com.crm.persistence;

public class StockThresholdManager {
    private static volatile StockThresholdManager instance;
    private int lowStockThreshold;

    private StockThresholdManager() {
        this.lowStockThreshold = 5; // Default low stock threshold
    }

    public static synchronized StockThresholdManager getInstance() {
        if (instance == null) {
            synchronized(StockThresholdManager.class) {
                if (instance == null) {
                    instance = new StockThresholdManager();
                }
            }
        }
        return instance;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
}