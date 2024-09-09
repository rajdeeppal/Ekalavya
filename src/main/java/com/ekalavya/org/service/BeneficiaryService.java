package com.ekalavya.org.service;

import com.ekalavya.org.DTO.*;
import com.ekalavya.org.entity.*;
import com.ekalavya.org.exception.CustomException;
import com.ekalavya.org.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private CustomBeneficiaryRepository customBeneficiaryRepository;

    @Transactional
    public String addBeneficiary(BeneficiaryCreationRequest bRequest){
        M_Beneficiary beneficiary = null;
        try{
            M_Beneficiary beneficiaryRepositoryByAadharNumber = beneficiaryRepository.findByAadharNumber(bRequest.getAadharNumber());
            if(beneficiaryRepositoryByAadharNumber != null && bRequest.getBeneficiaryName().equals(beneficiaryRepositoryByAadharNumber.getName())){
                beneficiary = updateAndAddBeneficiaryDetails(beneficiaryRepositoryByAadharNumber, bRequest);
            }else if(beneficiaryRepositoryByAadharNumber != null && !bRequest.getBeneficiaryName().equals(beneficiaryRepositoryByAadharNumber.getName())){
                throw new CustomException("Duplicate Aadhar Number not allowed !! ");
            }else{
                beneficiary = parseRequestAndCreateDbObject(bRequest);
            }
            beneficiaryRepository.save(beneficiary);
            return "SUCCESS";
        }catch(Exception e){
            log.error("Exception Occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }

    private M_Beneficiary updateAndAddBeneficiaryDetails(M_Beneficiary beneficiaryEntity, BeneficiaryCreationRequest bRequest) {

        log.info("Received a Beneficiary Update Request : {}", bRequest);
        M_Component mComponent = new M_Component();
        mComponent.setBeneficiary(beneficiaryEntity);
        mComponent.setName(bRequest.getComponentName());

        M_Activity mActivity = new M_Activity();
        mActivity.setName(bRequest.getActivityName());
        mActivity.setComponent(mComponent);

        M_Task mTask = new M_Task();
        mTask.setName(bRequest.getTaskName());
        mTask.setTypeOfUnit(bRequest.getTypeOfUnit());
        mTask.setUnits(bRequest.getUnits());
        mTask.setRatePerUnit(bRequest.getRatePerUnit());
        mTask.setTotalCost(bRequest.getTotalCost());
        mTask.setBeneficiaryContribution(bRequest.getBeneficiaryContribution());
        mTask.setGrantAmount(bRequest.getGrantAmount());
        mTask.setYearOfSanction(bRequest.getYearOfSanction());
        mTask.setActivity(mActivity);

        mActivity.getTasks().add(mTask);
        mComponent.getActivities().add(mActivity);
        beneficiaryEntity.getComponents().add(mComponent);

        return beneficiaryEntity;
    }

    public BeneficiaryResponse getBeneficiaryByAadhar(long aadharNumber){
        return objectMapper.convertValue(beneficiaryRepository.findByAadharNumber(aadharNumber), BeneficiaryResponse.class);
    }


    public List<BeneficiaryResponse> getBeneficiaryByProjectName(Map<String, String> params){

        //Long projectId = projectRepository.findByProjectName(projectName).getId();

        return objectMapper.convertValue(customBeneficiaryRepository.findBeneficiariesBasedOnCriteria(params), objectMapper.getTypeFactory().constructCollectionType(List.class, BeneficiaryResponse.class));
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

        log.info("Received a Beneficiary Creation Request : {}", bRequest);

        Project project = projectRepository.findByProjectName(bRequest.getProjectName());

        M_Beneficiary beneficiary = new M_Beneficiary();
        beneficiary.setName(bRequest.getBeneficiaryName());
        beneficiary.setGuardianName(bRequest.getGuardianName());
        beneficiary.setProjectId(project.getId());

        beneficiary.setVillageName(bRequest.getVillageName());
        beneficiary.setStateName(bRequest.getStateName());
        beneficiary.setMandalName(bRequest.getMandalName());
        beneficiary.setDistrictName(bRequest.getDistrictName());

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
