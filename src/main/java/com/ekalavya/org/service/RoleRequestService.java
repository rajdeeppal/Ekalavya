package com.ekalavya.org.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekalavya.org.entity.RoleRequest;
import com.ekalavya.org.repository.RoleRequestRepository;

@Service
public class RoleRequestService {

	@Autowired
    private RoleRequestRepository roleRequestRepository;

    public RoleRequest submitRoleRequest(RoleRequest request) {
        request.setStatus("PENDING");
        request.setRequestDate(LocalDate.now());
        return roleRequestRepository.save(request);
    }

    public List<RoleRequest> getPendingRequests() {
        return roleRequestRepository.findByStatus("PENDING");
    }

    public RoleRequest approveRequest(Long requestId, String approverComments) {
        RoleRequest request = roleRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");
        request.setApprovalDate(LocalDate.now());
        request.setApproverComments(approverComments);
        return roleRequestRepository.save(request);
    }

    public RoleRequest rejectRequest(Long requestId, String approverComments) {
        RoleRequest request = roleRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        request.setApprovalDate(LocalDate.now());
        request.setApproverComments(approverComments);
        return roleRequestRepository.save(request);
    }

}
