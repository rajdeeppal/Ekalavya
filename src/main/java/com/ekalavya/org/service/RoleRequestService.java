package com.ekalavya.org.service;

import com.ekalavya.org.DTO.UserCreateRequest;
import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.RoleRequest;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.exception.CustomException;
import com.ekalavya.org.repository.RoleRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class RoleRequestService {

	@Autowired
    private RoleRequestRepository roleRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmployeeIdGeneratorService employeeIdGeneratorService;

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

    public void deleteRoleRequestById(Long roleRequestId){
        roleRequestRepository.deleteById(roleRequestId);
    }

    public RoleRequest rejectRequest(Long requestId, String approverComments) {
        RoleRequest request = roleRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        request.setApprovalDate(LocalDate.now());
        request.setApproverComments(approverComments);
        return roleRequestRepository.save(request);
    }

    @Transactional
    public boolean createNewUserAndCreateARoleRequest(UserCreateRequest userCreateRequest) throws CustomException {
        try{
            User user = new User();
            if ("Domain Expert".equals(userCreateRequest.getRequestedRole())) {
                if (userCreateRequest.getDomain() == null || userCreateRequest.getDomain().isEmpty()) {
                    throw new CustomException("Domain is required for DOMAIN EXPERT role.");
                } else
                    user.setDomain(userCreateRequest.getDomain());
            }
            Role role = roleService.getRoleByRolename(userCreateRequest.getRequestedRole());
            if (role != null) {
                user.setUsername(userCreateRequest.getUsername());
                user.setPassword(userCreateRequest.getPassword());
                user.setRole(role);
                if (user.getEmpId() == null) {
                    user.setEmpId(employeeIdGeneratorService.generateNextEmpId());
                }
                user.setEmailid(userCreateRequest.getEmailId());
                userService.save(user);
                RoleRequest roleRequest = new RoleRequest();
                roleRequest.setUser(user);
                roleRequest.setRequestedRole(userCreateRequest.getRequestedRole());
                submitRoleRequest(roleRequest);

                return true;
            }

        }catch (Exception e){
            log.error("Exception occurred at : {}", e.getMessage());
            return false;
        }
        return false;
    }

}
