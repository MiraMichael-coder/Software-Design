package com.crm.customer.model;

import com.crm.common.enums.CustomerStatus;
import java.time.LocalDateTime;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private CustomerStatus status;
    private LocalDateTime createdAt;

    // Fields for segmentation strategies
    private double totalSpending;
    private LocalDateTime lastActivityDate;
    private String region;

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = CustomerStatus.Active;
        this.createdAt = LocalDateTime.now();
      
    }
public Customer(String customerId, String name, String email, String phone, double totalSpending, 
    LocalDateTime lastActivityDate, String regionl,CustomerStatus status) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.totalSpending = totalSpending;
        this.lastActivityDate = lastActivityDate;
        this.region = region;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateInfo(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void changeStatus(CustomerStatus status) {
        this.status = status;
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
