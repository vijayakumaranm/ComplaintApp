package com.example.complaintapp.service;

import com.example.complaintapp.entity.Complaint;
import com.example.complaintapp.entity.ComplaintStatus;
import com.example.complaintapp.entity.User;
import com.example.complaintapp.exception.BadRequestException;
import com.example.complaintapp.exception.ResourceNotFoundException;
import com.example.complaintapp.repository.ComplaintRepository;
import com.example.complaintapp.repository.ComplaintStatusRepository;
import com.example.complaintapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintService {

    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintStatusRepository complaintStatusRepository;

    public Map<String, Object> raiseComplaint(Long userId, Complaint complaint) {
        logger.info("Raise complaint request received for userId: {}", userId);

        if (complaint.getTitle() == null || complaint.getTitle().trim().isEmpty()) {
            logger.warn("Raise complaint failed - title is missing for userId: {}", userId);
            throw new BadRequestException("Title is required");
        }

        if (complaint.getDescription() == null || complaint.getDescription().trim().isEmpty()) {
            logger.warn("Raise complaint failed - description is missing for userId: {}", userId);
            throw new BadRequestException("Description is required");
        }

        if (complaint.getCategory() == null || complaint.getCategory().trim().isEmpty()) {
            logger.warn("Raise complaint failed - category is missing for userId: {}", userId);
            throw new BadRequestException("Category is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Raise complaint failed - user not found for userId: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        ComplaintStatus openStatus = complaintStatusRepository.findByStatusName("Open")
                .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));

        complaint.setUser(user);
        complaint.setStatus(openStatus);
        complaintRepository.save(complaint);

        logger.info("Complaint raised successfully for userId: {}", userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Complaint raised successfully");
        return response;
    }

    public Map<String, Object> getMyComplaints(Long userId) {
        logger.info("Fetching complaints for userId: {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Fetch complaints failed - user not found for userId: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        List<Complaint> complaints = complaintRepository.findByUserId(userId);
        logger.info("Fetched {} complaints for userId: {}", complaints.size(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("complaints", complaints);
        return response;
    }
}