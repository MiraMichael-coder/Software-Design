package com.crm.inventory.model;

import com.crm.common.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Supplier {
    private final String supplierId;
    @Setter
    private String supplierName;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private Address address;

    public Supplier(String supplierId, String supplierName, String contactName, String contactEmail, String contactPhone, Address address) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.address = address;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
