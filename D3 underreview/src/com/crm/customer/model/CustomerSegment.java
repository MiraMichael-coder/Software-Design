package com.crm.customer.model;

public class CustomerSegment {
    private String segmentId;
    private String name;
    private String criteriaDescription;
    private com.crm.customer.strategy.SegmentationStrategy strategy;

    public CustomerSegment(String segmentId, String name, String criteriaDescription) {
        this(segmentId, name, criteriaDescription, null);
    }

    public CustomerSegment(String segmentId, String name, String criteriaDescription,
            com.crm.customer.strategy.SegmentationStrategy strategy) {
        this.segmentId = segmentId;
        this.name = name;
        this.criteriaDescription = criteriaDescription;
        this.strategy = strategy;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCriteriaDescription() {
        return criteriaDescription;
    }

    public void setCriteriaDescription(String criteriaDescription) {
        this.criteriaDescription = criteriaDescription;
    }

    public com.crm.customer.strategy.SegmentationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(com.crm.customer.strategy.SegmentationStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean belongsToSegment(Customer customer) {
        if (strategy == null) {
            return false;
        }
        return strategy.evaluate(customer);
    }
}
