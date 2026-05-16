package com.crm.common;

public class Address {
    private String street;
    private String city;
    private String zone;

    public Address(String street, String city, String zone) {
        this.street = street;
        this.city = city;
        this.zone = zone;
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
}
