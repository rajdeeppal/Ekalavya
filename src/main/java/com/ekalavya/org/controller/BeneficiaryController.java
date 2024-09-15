package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.BeneficiaryCreationRequest;
import com.ekalavya.org.DTO.BeneficiaryResponse;
import com.ekalavya.org.DTO.TaskCreationRequest;
import com.ekalavya.org.service.BeneficiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/beneficiary")
@Slf4j
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    ObjectMapper objectMapper;

    // TODO: add the update logic
    @PostMapping("/create")
    public ResponseEntity<String> addBeneficiary(@RequestBody BeneficiaryCreationRequest beneficiaryCreationRequest){
        return new ResponseEntity<>(beneficiaryService.addBeneficiary(beneficiaryCreationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/addTask/{activityId}")
    public ResponseEntity<String> addTaskUnderActivityById(@PathVariable("activityId") long activity, @RequestBody TaskCreationRequest taskCreationRequest){
        return new ResponseEntity<>(beneficiaryService.addTask(activity, taskCreationRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public BeneficiaryResponse getBeneficiaryByAadhar(@RequestParam long aadharNumber){
        return beneficiaryService.getBeneficiaryByAadhar(aadharNumber);
    }

    @PreAuthorize("hasAuthority('TRUSTEE')")
    @PostMapping("/criteriaSearch")
    public List<BeneficiaryResponse> getBeneficiaryByProjectName(@RequestBody Map<String, String> params){
        return beneficiaryService.getBeneficiaryByProjectName(params);
    }
}
