package com.crm.customer.controller;

import com.crm.customer.model.Complaint;
import com.crm.customer.repository.ComplaintRepository;

public class ComplaintController {
    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public void createComplaint(Complaint complaint) {
<<<<<<< Updated upstream
        complaintRepository.save(complaint);
=======
        complaintRepository.save(complaint.getComplaintId(), complaint);
>>>>>>> Stashed changes
    }

    public Complaint getComplaint(String complaintId) {
        return complaintRepository.findById(complaintId);
    }

    public void updateComplaint(Complaint complaint) {
<<<<<<< Updated upstream
        complaintRepository.update(complaint);
=======
        complaintRepository.update(complaint.getComplaintId(), complaint);
>>>>>>> Stashed changes
    }

    public void deleteComplaint(String complaintId) {
        complaintRepository.delete(complaintId);
    }
}
