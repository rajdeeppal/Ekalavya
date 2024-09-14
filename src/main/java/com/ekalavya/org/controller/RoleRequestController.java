package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.UserCreateRequest;
import com.ekalavya.org.exception.CustomException;
import com.ekalavya.org.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/self-service")
public class RoleRequestController {

    @Autowired
    private RoleRequestService roleRequestService;

    @PostMapping("/submitRoleRequest")
    public ResponseEntity<?> submitRoleRequest(@RequestBody UserCreateRequest userCreateRequest) throws CustomException {
        return new ResponseEntity<>(roleRequestService.createNewUserAndCreateARoleRequest(userCreateRequest) ? "SUCCESS" : "FAILURE",
                HttpStatus.CREATED);
    }

}
