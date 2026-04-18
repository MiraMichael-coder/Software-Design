package com.crm.customer.model;

public class CustomerSegment {
    private String segmentId;
    private String name;
    private String criteriaDescription;

    public CustomerSegment(String segmentId, String name, String criteriaDescription) {
        this.segmentId = segmentId;
        this.name = name;
        this.criteriaDescription = criteriaDescription;
    }

    public String getSegmentId() { return segmentId; }
    public void setSegmentId(String segmentId) { this.segmentId = segmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCriteriaDescription() { return criteriaDescription; }
    public void setCriteriaDescription(String criteriaDescription) { this.criteriaDescription = criteriaDescription; }
}
