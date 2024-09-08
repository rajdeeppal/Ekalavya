package com.ekalavya.org.service;

import com.ekalavya.org.DTO.*;
import com.ekalavya.org.entity.*;
import com.ekalavya.org.exception.CustomException;
import com.ekalavya.org.repository.BeneficiaryRepository;
import com.ekalavya.org.repository.MActivityRepository;
import com.ekalavya.org.repository.MTaskRepository;
import com.ekalavya.org.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private MActivityRepository mActivityRepository;

    @Autowired
    private MTaskRepository mTaskRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    public String addBeneficiary(BeneficiaryCreationRequest bRequest){
        try{

            M_Beneficiary beneficiaryRepositoryByAadharNumber = beneficiaryRepository.findByAadharNumber(bRequest.getAadharNumber());
            /// if aadhar number and bname both are equal then it is a update request, else it is a duplicate entry, reject it
            log.info("Received a Beneficiary Update Request : {}", bRequest);
            if(beneficiaryRepositoryByAadharNumber != null && bRequest.getBeneficiaryName().equals(beneficiaryRepositoryByAadharNumber.getName())){
                /// update request
                return null;
            }else if(beneficiaryRepositoryByAadharNumber != null && !bRequest.getBeneficiaryName().equals(beneficiaryRepositoryByAadharNumber.getName())){
                throw new CustomException("Duplicate Aadhar Number not allowed !! ");
            }else{
                log.info("Received a Beneficiary Creation Request : {}", bRequest);
                M_Beneficiary beneficiary = parseRequestAndCreateDbObject(bRequest);
                beneficiaryRepository.save(beneficiary);
                return "SUCCESS";
            }
        }catch(Exception e){
            log.error("Exception Occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }

    public BeneficiaryResponse getBeneficiary(long aadharNumber){
        return objectMapper.convertValue(beneficiaryRepository.findByAadharNumber(aadharNumber), BeneficiaryResponse.class);
    }


    public List<BeneficiaryResponse> getBeneficiaryByProjectName(String projectName){

        Long projectId = projectRepository.findByProjectName(projectName).getId();

        return objectMapper.convertValue(beneficiaryRepository.findByProjectId(projectId), objectMapper.getTypeFactory().constructCollectionType(List.class, BeneficiaryResponse.class));
    }


    public String addTask(long activityId, TaskCreationRequest taskCreationRequest){
        try{
            M_Activity mActivity = mActivityRepository.findById(activityId);
            M_Task task = new M_Task();
            task.setName(taskCreationRequest.getName());
            task.setTypeOfUnit(taskCreationRequest.getTypeOfUnit());
            task.setUnits(taskCreationRequest.getUnits());
            task.setRatePerUnit(taskCreationRequest.getRatePerUnit());
            task.setTotalCost(taskCreationRequest.getTotalCost());
            task.setBeneficiaryContribution(taskCreationRequest.getBeneficiaryContribution());
            task.setGrantAmount(taskCreationRequest.getGrantAmount());
            task.setYearOfSanction(taskCreationRequest.getYearOfSanction());
            task.setActivity(mActivity);
            mActivity.getTasks().add(task);
            mTaskRepository.save(task);
            return "SUCCESS";
        }catch(Exception e){
            log.error("Exception occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }


    private M_Beneficiary parseRequestAndCreateDbObject(BeneficiaryCreationRequest bRequest) {

        Project project = projectRepository.findByProjectName(bRequest.getProjectName());

        M_Beneficiary beneficiary = new M_Beneficiary();
        beneficiary.setName(bRequest.getBeneficiaryName());
        beneficiary.setGuardianName(bRequest.getGuardianName());
        beneficiary.setProjectId(project.getId());

        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setVillageName(bRequest.getVillageName());
        addressDetails.setStateName(bRequest.getStateName());
        addressDetails.setMandalName(bRequest.getMandalName());
        addressDetails.setDistrictName(bRequest.getDistrictName());

        beneficiary.setAddressDetails(addressDetails);
        beneficiary.setAadharNumber(bRequest.getAadharNumber());


        /// create the component entity
        M_Component component = new M_Component();
        component.setName(bRequest.getComponentName());
        component.setBeneficiary(beneficiary);


        // create the activity entity
        M_Activity activity = new M_Activity();
        activity.setName(bRequest.getActivityName());
        activity.setComponent(component);

        // create the task entity
        M_Task task = new M_Task();
        task.setName(bRequest.getTaskName());
        task.setTypeOfUnit(bRequest.getTypeOfUnit());
        task.setUnits(bRequest.getUnits());
        task.setRatePerUnit(bRequest.getRatePerUnit());
        task.setTotalCost(bRequest.getTotalCost());
        task.setBeneficiaryContribution(bRequest.getBeneficiaryContribution());
        task.setGrantAmount(bRequest.getGrantAmount());
        task.setYearOfSanction(bRequest.getYearOfSanction());
        task.setActivity(activity);

        activity.getTasks().add(task);
        component.getActivities().add(activity);
        beneficiary.getComponents().add(component);



        return beneficiary;
    }
}
