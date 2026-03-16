package com.example.complaintapp.controller;

import com.example.complaintapp.entity.Complaint;
import com.example.complaintapp.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public Map<String, Object> raiseComplaint(@RequestParam Long userId, @RequestBody Complaint complaint) {
        return complaintService.raiseComplaint(userId, complaint);
    }

    @GetMapping("/my/{userId}")
    public Map<String, Object> getMyComplaints(@PathVariable Long userId) {
        return complaintService.getMyComplaints(userId);
    }
}