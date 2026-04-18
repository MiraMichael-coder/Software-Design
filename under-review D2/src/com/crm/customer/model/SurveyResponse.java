package com.crm.customer.model;

import java.time.LocalDateTime;

public class SurveyResponse {
    private String responseId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public SurveyResponse(String responseId, int rating, String comment) {
        this.responseId = responseId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public String getResponseId() { return responseId; }
    public void setResponseId(String responseId) { this.responseId = responseId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean validate() {
        return rating >= 1 && rating <= 5;
    }
}
