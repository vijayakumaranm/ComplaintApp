package com.example.complaintapp.repository;

import com.example.complaintapp.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComplaintStatusRepository extends JpaRepository<ComplaintStatus, Long> {
    Optional<ComplaintStatus> findByStatusName(String statusName);
}