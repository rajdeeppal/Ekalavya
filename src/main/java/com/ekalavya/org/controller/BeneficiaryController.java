package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.*;
import com.ekalavya.org.service.BeneficiaryService;
import com.ekalavya.org.service.ResolutionTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private ResolutionTaskService resolutionTaskService;

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
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/addTask/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable("taskId") long taskId, @RequestBody TaskCreationRequest taskCreationRequest) {
        if ("SUCCESS".equals(beneficiaryService.updateTask(taskId, taskCreationRequest))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getBeneficiaryList")
    public List<BeneficiaryResponse> getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    //@PreAuthorize("hasAuthority('TRUSTEE')")
    @PostMapping("/criteriaSearch")
    public List<BeneficiaryResponse> getBeneficiaryByProjectName(@RequestBody Map<String, String> params) {
        return beneficiaryService.getBeneficiaryByProjectName(params);
    }

    @PostMapping("/addTaskUpdate/{taskId}")
    public ResponseEntity<String> addTaskUpdate(@PathVariable("taskId") long taskId,
                                                @RequestParam("passbook") MultipartFile passbookDoc,
                                                @RequestParam("otherDoc") List<MultipartFile> otherDocs,
                                                @RequestBody TaskUpdateDTO taskUpdateDTO) throws IOException {
        if ("SUCCESS".equals(beneficiaryService.addTaskUpdate(taskId, taskUpdateDTO, passbookDoc, otherDocs))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/addTaskUpdate/{taskUpdateId}")
    public ResponseEntity<String> updateTaskUpdate(@PathVariable("taskUpdateId") long taskUpdateId,
                                                   @RequestParam("passbook") MultipartFile passbookDoc,
                                                   @RequestParam("otherDoc") List<MultipartFile> otherDocs,
                                                   @RequestBody TaskUpdateDTO taskUpdateDTO) throws IOException {
        if ("SUCCESS".equals(beneficiaryService.updateTaskUpdate(taskUpdateId, taskUpdateDTO, passbookDoc, otherDocs))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/resolution/upload/{userId}")
    public ResponseEntity<String> uploadResolutionDocs(@PathVariable String userId,
                                                       @RequestParam("file") MultipartFile resolutionDoc,
                                                       @RequestParam String projectName) throws IOException {
        if ("SUCCESS".equals(resolutionTaskService.uploadResolutionDocs(userId, resolutionDoc, projectName))){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/resolution/view")
    public List<ResolutionTaskResponseDTO> getLatestResolutions(@RequestParam String projectName){
        return resolutionTaskService.getLatest5ResolutionsByProjectName(projectName);
    }

}
