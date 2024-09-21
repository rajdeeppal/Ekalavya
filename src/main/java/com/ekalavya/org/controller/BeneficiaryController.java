package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.BeneficiaryCreationRequest;
import com.ekalavya.org.DTO.BeneficiaryResponse;
import com.ekalavya.org.DTO.TaskCreationRequest;
import com.ekalavya.org.DTO.TaskUpdateDTO;
import com.ekalavya.org.service.BeneficiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<String> addBeneficiary(@RequestBody BeneficiaryCreationRequest beneficiaryCreationRequest) {
        if ("SUCCESS".equals(beneficiaryService.addBeneficiary(beneficiaryCreationRequest))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addTask/{activityId}")
    public ResponseEntity<String> addTaskUnderActivityById(@PathVariable("activityId") long activity, @RequestBody TaskCreationRequest taskCreationRequest) {
        if ("SUCCESS".equals(beneficiaryService.addTask(activity, taskCreationRequest))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    @PutMapping("/addTask/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable("taskId") long taskId, @RequestBody TaskCreationRequest taskCreationRequest) {
        if ("SUCCESS".equals(beneficiaryService.updateTask(taskId, taskCreationRequest))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public BeneficiaryResponse getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    //@PreAuthorize("hasAuthority('TRUSTEE')")
    @PostMapping("/criteriaSearch")
    public List<BeneficiaryResponse> getBeneficiaryByProjectName(@RequestBody Map<String, String> params) {
        return beneficiaryService.getBeneficiaryByProjectName(params);
    }

    @PostMapping("/addTaskUpdate/{taskId}")
    public ResponseEntity<String> addTaskUpdate(@PathVariable("taskId") long taskId, @ModelAttribute TaskUpdateDTO taskUpdateDTO) throws IOException {
        if ("SUCCESS".equals(beneficiaryService.addTaskUpdate(taskId, taskUpdateDTO))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/addTaskUpdate/{taskUpdateId}")
    public ResponseEntity<String> updateTaskUpdate(@PathVariable("taskUpdateId") long taskUpdateId, @ModelAttribute TaskUpdateDTO taskUpdateDTO) throws IOException {
        if ("SUCCESS".equals(beneficiaryService.updateTaskUpdate(taskUpdateId, taskUpdateDTO))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
