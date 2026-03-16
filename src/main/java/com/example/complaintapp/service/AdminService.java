package com.example.complaintapp.service;

import com.example.complaintapp.entity.Complaint;
import com.example.complaintapp.entity.ComplaintStatus;
import com.example.complaintapp.exception.BadRequestException;
import com.example.complaintapp.exception.ResourceNotFoundException;
import com.example.complaintapp.repository.ComplaintRepository;
import com.example.complaintapp.repository.ComplaintStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintStatusRepository complaintStatusRepository;

    public Map<String, Object> getAllComplaints() {
        logger.info("Admin fetching all complaints");

        List<Complaint> complaints = complaintRepository.findAll();
        logger.info("Total complaints fetched: {}", complaints.size());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("complaints", complaints);
        return response;
    }

    public Map<String, Object> updateComplaint(Long id, String statusName, String assignedTo) {
        logger.info("Admin update request for complaintId: {}", id);

        if (statusName == null || statusName.trim().isEmpty()) {
            logger.warn("Update failed - status is missing for complaintId: {}", id);
            throw new BadRequestException("Status is required");
        }

        ComplaintStatus complaintStatus = complaintStatusRepository.findByStatusName(statusName)
                .orElseThrow(() -> {
                    logger.warn("Update failed - invalid status '{}' for complaintId: {}", statusName, id);
                    return new BadRequestException("Invalid status. Allowed: Open, In Progress, Resolved, Rejected");
                });

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Update failed - complaint not found for complaintId: {}", id);
                    return new ResourceNotFoundException("Complaint not found");
                });

        complaint.setStatus(complaintStatus);

        if (assignedTo != null && !assignedTo.trim().isEmpty()) {
            logger.info("Assigning complaint {} to department: {}", id, assignedTo);
            complaint.setAssignedTo(assignedTo);
        }

        complaintRepository.save(complaint);
        logger.info("Complaint {} updated successfully with status: {}", id, statusName);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Complaint updated successfully");
        return response;
    }
}