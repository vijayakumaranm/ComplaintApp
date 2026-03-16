package com.example.complaintapp.controller;

import com.example.complaintapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/complaints")
    public Map<String, Object> getAllComplaints() {
        return adminService.getAllComplaints();
    }

    @PutMapping("/complaints/{id}")
    public Map<String, Object> updateComplaint(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        String assignedTo = body.get("assignedTo");
        return adminService.updateComplaint(id, status, assignedTo);
    }
}