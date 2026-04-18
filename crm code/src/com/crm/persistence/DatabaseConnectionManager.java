package com.crm.persistence;

public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;

    private DatabaseConnectionManager() {}

    public static synchronized DatabaseConnectionManager getInstance() {
<<<<<<< Updated upstream
        if (instance == null) instance = new DatabaseConnectionManager();
=======
        if (instance == null) {
            synchronized (DatabaseConnectionManager.class) {
                if (instance == null)
                    instance = new DatabaseConnectionManager();

            }
        }
>>>>>>> Stashed changes
        return instance;
    }

    public void connect() {
<<<<<<< Updated upstream
        // Skeleton: open connection
    }

    public void disconnect() {
        // Skeleton: close connection
=======
        System.out.println("Connecting to database...");
    }

    public void disconnect() {
        System.out.println("Disconnecting  database...");
>>>>>>> Stashed changes
    }
}
