package com.crm.persistence;

public class SystemConfigurationManager {
    private static volatile SystemConfigurationManager instance;

    private SystemConfigurationManager() {}

    public static synchronized SystemConfigurationManager getInstance() {
        if (instance == null) {
            synchronized(SystemConfigurationManager.class)
            {
                if(instance == null) {
                    instance = new SystemConfigurationManager();
                }
            }
        }
        return instance;
    }

    // Removed low stock threshold management
    public int getSlaHours() {
        return SlaConfigurationManager.getInstance().getSlaHours();
    }

    public void setSlaHours(int slaHours) {
        SlaConfigurationManager.getInstance().setSlaHours(slaHours);
    }
}