package com.crm.persistence;

public class SystemConfigurationManager {
    private static volatile SystemConfigurationManager instance;

<<<<<<< Updated upstream
    private int slaHours = 24;
    private int lowStockThreshold = 5;

=======
>>>>>>> Stashed changes
    private SystemConfigurationManager() {}

    public static synchronized SystemConfigurationManager getInstance() {
        if (instance == null) {
            synchronized(SystemConfigurationManager.class)
            {
<<<<<<< Updated upstream
                if(instance==null)
                {
=======
                if(instance == null) {
>>>>>>> Stashed changes
                    instance = new SystemConfigurationManager();
                }
            }
        }
        return instance;
    }

<<<<<<< Updated upstream

    public int getSlaHours() { return slaHours; }
    public void setSlaHours(int slaHours) { this.slaHours = slaHours; }

    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
}
=======
    // Removed low stock threshold management
    public int getSlaHours() {
        return SlaConfigurationManager.getInstance().getSlaHours();
    }

    public void setSlaHours(int slaHours) {
        SlaConfigurationManager.getInstance().setSlaHours(slaHours);
    }
}
>>>>>>> Stashed changes
