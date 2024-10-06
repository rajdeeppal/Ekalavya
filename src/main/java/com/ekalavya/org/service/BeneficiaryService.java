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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private MComponentRepository mComponentRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DocumentStorageService documentStorageService;

    @Autowired
    private CustomBeneficiaryRepository customBeneficiaryRepository;

    @Autowired
    private MTaskUpdateRepository mTaskUpdateRepository;

    @Autowired
    private ResolutionTaskService resolutionTaskService;

    @Transactional
    public String addBeneficiary(BeneficiaryCreationRequest bRequest) {
        M_Beneficiary beneficiary = null;
        try {
            Project project = projectService.findByName(bRequest.getProjectName());
            boolean checkAdmision = checkRestrictToAssignProject(bRequest.getAadharNumber(), project);
            if (checkAdmision) {
                M_Beneficiary beneficiaryRepositoryByAadharNumber = beneficiaryRepository.
                        findByAadharAndProjectAndProjectTermination(project, bRequest.getAadharNumber());
                if (beneficiaryRepositoryByAadharNumber != null && bRequest.getBeneficiaryName().equals(beneficiaryRepositoryByAadharNumber.getBeneficiaryName())) {
                    beneficiary = parseRequestAndCreateDbObject(beneficiaryRepositoryByAadharNumber, bRequest, project);
                } else if (beneficiaryRepositoryByAadharNumber != null) {
                    throw new CustomException("Error: Duplicate Aadhar number is not permitted !! ");
                } else if (project != null) {
                    beneficiary = parseRequestAndCreateDbObject(null, bRequest, project);
                } else
                    throw new CustomException("Error: The specified project could not be found !! ");
                beneficiaryRepository.save(beneficiary);
                return "SUCCESS";
            } else
                throw new CustomException("Operation Failed: The beneficiary is already associated with another project !! ");
        } catch (Exception e) {
            log.error("Exception Occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }

    private boolean checkRestrictToAssignProject(Long aadharNumber, Project assignProject) {
        List<M_Beneficiary> m_beneficiary = beneficiaryRepository.findByAadharNumberAndProjectTermination(aadharNumber);
        if(m_beneficiary.isEmpty()){
            return true;
        }
        m_beneficiary.removeIf(beneficiary -> beneficiary.getProject().equals(assignProject));
        return m_beneficiary.isEmpty();
    }

//    private M_Beneficiary updateAndAddBeneficiaryDetails(M_Beneficiary beneficiaryEntity, BeneficiaryCreationRequest bRequest, Project project) {
//
//        log.info("Received a Beneficiary Update Request : {}", bRequest);
//
//        M_Component mComponent = new M_Component();
//        mComponent.setBeneficiary(beneficiaryEntity);
//        mComponent.setName(bRequest.getComponentName());
//
//        M_Activity mActivity = new M_Activity();
//        mActivity.setName(bRequest.getActivityName());
//        mActivity.setComponent(mComponent);
//
//        M_Task mTask = new M_Task();
//        mTask.setName(bRequest.getTaskName());
//        mTask.setTypeOfUnit(bRequest.getTypeOfUnit());
//        mTask.setUnits(bRequest.getUnits());
//        mTask.setRatePerUnit(bRequest.getRatePerUnit());
//        mTask.setTotalCost(bRequest.getTotalCost());
//        mTask.setBeneficiaryContribution(bRequest.getBeneficiaryContribution());
//        mTask.setGrantAmount(bRequest.getGrantAmount());
//        mTask.setYearOfSanction(bRequest.getYearOfSanction());
//        mTask.setActivity(mActivity);
//
//        return beneficiaryEntity;
//    }

    public List<BeneficiaryResponse> getAllBeneficiaries() {
        List<M_Beneficiary> beneficiaries = beneficiaryRepository.findAll();
        return beneficiaries.stream()
                .map(mBeneficiary -> objectMapper.convertValue(mBeneficiary, BeneficiaryResponse.class))
                .collect(Collectors.toList());
    }


    public List<BeneficiaryResponse> getBeneficiaryByProjectName(Map<String, String> params) {

        //Long projectId = projectRepository.findByProjectName(projectName).getId();

        return objectMapper.convertValue(customBeneficiaryRepository.findBeneficiariesBasedOnCriteria(params), objectMapper.getTypeFactory().constructCollectionType(List.class, BeneficiaryResponse.class));
    }


    public String addTask(long activityId, TaskCreationRequest taskCreationRequest) {
        try {
            M_Activity mActivity = mActivityRepository.findById(activityId);
            M_Task task = new M_Task();
            task.setTaskName(taskCreationRequest.getName());
            task.setTypeOfUnit(taskCreationRequest.getTypeOfUnit());
            task.setUnits(taskCreationRequest.getUnits());
            task.setRatePerUnit(taskCreationRequest.getRatePerUnit());
            task.setTotalCost(taskCreationRequest.getTotalCost());
            task.setBeneficiaryContribution(taskCreationRequest.getBeneficiaryContribution());
            task.setGrantAmount(taskCreationRequest.getGrantAmount());
            task.setYearOfSanction(taskCreationRequest.getYearOfSanction());
            task.setBalanceRemaining(taskCreationRequest.getTotalCost());
            task.setUnitRemain(taskCreationRequest.getUnits());
            task.setBeneficiaryContributionRemain(taskCreationRequest.getBeneficiaryContribution());
            task.setActivity(mActivity);
            mTaskRepository.save(task);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("Exception occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }


    private M_Beneficiary parseRequestAndCreateDbObject(M_Beneficiary beneficiary, BeneficiaryCreationRequest bRequest, Project project) {

        log.info("Received a Beneficiary Creation Request : {}", bRequest);

        if (beneficiary == null) {
            beneficiary = new M_Beneficiary();
            beneficiary.setBeneficiaryName(bRequest.getBeneficiaryName());
            beneficiary.setGuardianName(bRequest.getGuardianName());
            beneficiary.setProject(project);
            beneficiary.setVillageName(bRequest.getVillageName());
            beneficiary.setStateName(bRequest.getStateName());
            beneficiary.setMandalName(bRequest.getMandalName());
            beneficiary.setDistrictName(bRequest.getDistrictName());
            beneficiary.setAadharNumber(bRequest.getAadharNumber());
            beneficiaryRepository.save(beneficiary);
        }

        /// create the component entity
        M_Component mComponent = mComponentRepository.findByComponentNameAndBeneficiary(bRequest.getComponentName(), beneficiary);
        if (mComponent == null) {
            mComponent = new M_Component();
            mComponent.setComponentName(bRequest.getComponentName());
            mComponent.setBeneficiary(beneficiary);
            mComponentRepository.save(mComponent);
        }

        // create the activity entity
        M_Activity mActivity = mActivityRepository.findByActivityNameAndComponent(bRequest.getActivityName(), mComponent);
        if (mActivity == null) {
            mActivity = new M_Activity();
            mActivity.setActivityName(bRequest.getActivityName());
            mActivity.setComponent(mComponent);
            mActivityRepository.save(mActivity);
        }

        M_Task mTask = mTaskRepository.findByTaskNameAndActivity(bRequest.getTaskName(), mActivity);
        // create the task entity
        if (mTask == null) {
            mTask = new M_Task();
            mTask.setTaskName(bRequest.getTaskName());
            mTask.setTypeOfUnit(bRequest.getTypeOfUnit());
            mTask.setUnits(bRequest.getUnits());
            mTask.setRatePerUnit(bRequest.getRatePerUnit());
            mTask.setTotalCost(bRequest.getTotalCost());
            mTask.setBalanceRemaining(bRequest.getGrantAmount());
            mTask.setUnitRemain(bRequest.getUnits());
            mTask.setBeneficiaryContribution(bRequest.getBeneficiaryContribution());
            mTask.setBeneficiaryContributionRemain(bRequest.getBeneficiaryContribution());
            mTask.setGrantAmount(bRequest.getGrantAmount());
            mTask.setYearOfSanction(bRequest.getYearOfSanction());
            mTask.setActivity(mActivity);
            mTaskRepository.save(mTask);
        }

        return beneficiary;
    }

    public String updateTask(long taskId, TaskCreationRequest taskCreationRequest) {
        try {
            M_Task task = mTaskRepository.findById(taskId);
            if(taskCreationRequest.getUnits() != 0 || taskCreationRequest.getBeneficiaryContribution() != 0){
                task.setUnits(taskCreationRequest.getUnits());
                task.setUnitRemain(taskCreationRequest.getUnits());
                task.setTotalCost(taskCreationRequest.getTotalCost());
                if(taskCreationRequest.getBeneficiaryContribution() != 0){
                    task.setBeneficiaryContribution(taskCreationRequest.getBeneficiaryContribution());
                    task.setBeneficiaryContributionRemain(taskCreationRequest.getBeneficiaryContribution());
                }
                task.setGrantAmount(taskCreationRequest.getGrantAmount());
                task.setBalanceRemaining(taskCreationRequest.getGrantAmount());
            }
            if(taskCreationRequest.getYearOfSanction() != 0 ){
                task.setYearOfSanction(taskCreationRequest.getYearOfSanction());
            }
            mTaskRepository.save(task);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("Exception occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }

    public String addTaskUpdate(long taskId, TaskUpdateDTO taskUpdateDTO, MultipartFile passbook, List<MultipartFile> otherDocs) throws IOException {
        try {
            M_Task mTask = mTaskRepository.findById(taskId);

            Document passbookDoc = documentStorageService.storeFile(passbook);

            List<Document> otherDocuments = otherDocs.stream()
                    .map(file -> {
                        try {
                            return documentStorageService.storeFile(file);
                        } catch (IOException e) {
                            throw new RuntimeException("Error storing file: " + file.getOriginalFilename());
                        }
                    }).collect(Collectors.toList());
            if (mTask != null) {
                if (!checkInvalidDataLimit(taskUpdateDTO, mTask)) {
                    M_Task_Update m_task_update = new M_Task_Update();
                    m_task_update.setCurrentCost(taskUpdateDTO.getCurrentCost());
                    m_task_update.setAchievementUnit(taskUpdateDTO.getAchievementUnit());
                    m_task_update.setPayeeName(taskUpdateDTO.getPayeeName());
                    m_task_update.setPassbookDoc(passbookDoc);
                    m_task_update.setOtherDocs(otherDocuments);
                    m_task_update.setAccountNumber(taskUpdateDTO.getAccountNumber());
                    mTaskUpdateRepository.save(m_task_update);
                    return "SUCCESS";
                } else
                    return "FAILURE";
            }
        } catch (Exception e) {
            log.error("Exception occurred : {}", e.getMessage());
            return "FAILURE";
        }
        return "FAILURE";
    }

    private boolean checkInvalidDataLimit(TaskUpdateDTO taskUpdateDTO, M_Task mTask) {
        if(taskUpdateDTO.getAchievementUnit() != 0 && taskUpdateDTO.getBenContribution() != 0){
            if (mTask.getUnitRemain() - taskUpdateDTO.getAchievementUnit() >= 0 && mTask.getBeneficiaryContributionRemain() - taskUpdateDTO.getBenContribution() >= 0) {
                Long balance = mTask.getBalanceRemaining() - (taskUpdateDTO.getAchievementUnit() * mTask.getRatePerUnit()) + taskUpdateDTO.getBenContribution();
                if(balance >= 0 ){
                    mTask.setUnitRemain(mTask.getUnitRemain() - taskUpdateDTO.getAchievementUnit());
                    mTask.setBalanceRemaining(balance);
                    mTask.setBeneficiaryContributionRemain(mTask.getBeneficiaryContributionRemain() - taskUpdateDTO.getBenContribution());
                    mTaskRepository.save(mTask);
                    return false;
                }
                log.error("Error: Sanction amount already consumed !! Add beneficiary contribution amount !! ");
                return true;
            }
            log.error("Error: Invalid Data combinations !! ");
            return true;
        }
        if (taskUpdateDTO.getAchievementUnit() != 0 && (mTask.getUnitRemain() - taskUpdateDTO.getAchievementUnit() >= 0) ) {
            Long balance = mTask.getBalanceRemaining() - (taskUpdateDTO.getAchievementUnit() * mTask.getRatePerUnit());
            if(balance >= 0 ){
                mTask.setUnitRemain(mTask.getUnitRemain() - taskUpdateDTO.getAchievementUnit());
                mTask.setBalanceRemaining(balance);
                mTaskRepository.save(mTask);
                return false;
            }
            log.error("Error: Invalid Data combinations !! ");
            return true;
        }
        if( taskUpdateDTO.getBenContribution() != 0 && mTask.getBeneficiaryContributionRemain() - taskUpdateDTO.getBenContribution() >= 0){
            mTask.setBeneficiaryContributionRemain(mTask.getBeneficiaryContributionRemain() - taskUpdateDTO.getBenContribution());
            mTaskRepository.save(mTask);
            return false;
        }
        log.error("Error: Invalid Data combinations !! ");
        return true;
    }

    public String updateTaskUpdate(long taskUpdateId, TaskUpdateDTO taskUpdateDTO, MultipartFile passbookDoc, List<MultipartFile> otherDocs) {
        try {
            M_Task_Update m_task_update = mTaskUpdateRepository.findById(taskUpdateId).orElse(null);
            if (m_task_update != null) {
                M_Task mTask = m_task_update.getTask();
                if(taskUpdateDTO.getAccountNumber() != null ){
                    m_task_update.setAccountNumber(taskUpdateDTO.getAccountNumber());
                }
                if (taskUpdateDTO.getPayeeName() != null && !taskUpdateDTO.getPayeeName().isEmpty()) {
                    m_task_update.setPayeeName(taskUpdateDTO.getPayeeName());
                }
                if (!checkInvalidDataLimit(taskUpdateDTO, mTask)) {
                    if(taskUpdateDTO.getAchievementUnit() != 0 ){
                        m_task_update.setAchievementUnit(taskUpdateDTO.getAchievementUnit());
                    }
                    if(taskUpdateDTO.getBenContribution() != 0 ){
                        m_task_update.setCurrentBeneficiaryContribution(taskUpdateDTO.getBenContribution());
                    }
                    m_task_update.setCurrentCost(taskUpdateDTO.getCurrentCost());
                }
                if (passbookDoc != null && !passbookDoc.isEmpty()) {
                    Document passbook = documentStorageService.storeFile(passbookDoc);
                    m_task_update.setPassbookDoc(passbook);
                }
                if (otherDocs != null && !otherDocs.isEmpty()) {
                    List<Document> otherDocuments = otherDocs.stream()
                            .map(file -> {
                                try {
                                    return documentStorageService.storeFile(file);
                                } catch (IOException e) {
                                    throw new RuntimeException("Error storing file: " + file.getOriginalFilename());
                                }
                            }).collect(Collectors.toList());
                    m_task_update.setOtherDocs(otherDocuments);
                }
                mTaskUpdateRepository.save(m_task_update);
                return "SUCCESS";
            }
        } catch (Exception e) {
            log.error("Exception occurred : {}", e.getMessage());
        }
        return "FAILURE";
    }
}
