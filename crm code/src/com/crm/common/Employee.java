package com.crm.common;

public class Employee {
    private String employeeId;
    private String name;
    private String role;
    private String email;

    public Employee(String employeeId, String name, String role, String email) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
<<<<<<< Updated upstream
}
=======

    public void getDetails()
    {
        System.out.println("Employee details:");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
    }
}


>>>>>>> Stashed changes
