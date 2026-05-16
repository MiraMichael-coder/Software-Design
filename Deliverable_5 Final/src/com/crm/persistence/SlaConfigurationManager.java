package com.crm.persistence;

public class SlaConfigurationManager {
    private static volatile SlaConfigurationManager instance;
    private int slaHours;

    private SlaConfigurationManager() {
        this.slaHours = 24; // Default SLA time
    }

    public static synchronized SlaConfigurationManager getInstance() {
        if (instance == null) {
            synchronized(SlaConfigurationManager.class) {
                if (instance == null) {
                    instance = new SlaConfigurationManager();
                }
            }
        }
        return instance;
    }

    //
    public int getSlaHours() {
        return slaHours;
    }

    public void setSlaHours(int slaHours) {
        this.slaHours = slaHours;
    }
}