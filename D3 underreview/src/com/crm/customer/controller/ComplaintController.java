package com.crm.customer.controller;

import com.crm.customer.model.Complaint;
import com.crm.customer.repository.ComplaintRepository;

public class ComplaintController {
    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public void createComplaint(Complaint complaint) {
        complaintRepository.save(complaint.getComplaintId(), complaint);
    }

    public Complaint getComplaint(String complaintId) {
        return complaintRepository.findById(complaintId);
    }

    public void updateComplaint(Complaint complaint) {
        complaintRepository.update(complaint.getComplaintId(), complaint);
    }

    public void deleteComplaint(String complaintId) {
        complaintRepository.delete(complaintId);
    }
}
