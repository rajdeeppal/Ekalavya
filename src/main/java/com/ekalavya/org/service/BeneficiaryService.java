package com.ekalavya.org.service;

import com.ekalavya.org.DTO.BeneficiaryCreationRequest;
import com.ekalavya.org.DTO.BeneficiaryResponse;
import com.ekalavya.org.DTO.TaskCreationRequest;
import com.ekalavya.org.DTO.TaskUpdateDTO;
import com.ekalavya.org.entity.*;
import com.ekalavya.org.exception.CustomException;
import com.ekalavya.org.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String addBeneficiary(BeneficiaryCreationRequest bRequest) {
        M_Beneficiary beneficiary = null;
        try {
            Project project = projectService.findByName(bRequest.getProjectName());
            boolean checkAdmision = checkRestrictToAssignProject(bRequest.getAadharNumber(), project);
            if (!checkAdmision) {
                M_Beneficiary beneficiaryRepositoryByAadharNumber = beneficiaryRepository.
                        findByAadharAndProjectAndProjectTermination(project, bRequest.getAadharNumber());
                if (beneficiaryRepositoryByAadharNumber != null && bRequest.getBeneficiaryName().equals(beneficiaryRepositoryByAadharNumber.getName())) {
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
        m_beneficiary.removeIf(beneficiary -> !beneficiary.getProject().equals(assignProject));
        return m_beneficiary.size() > 0;
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

    public BeneficiaryResponse getBeneficiaryByAadhar(long aadharNumber) {
        return objectMapper.convertValue(beneficiaryRepository.findByAadharNumber(aadharNumber), BeneficiaryResponse.class);
    }


    public List<BeneficiaryResponse> getBeneficiaryByProjectName(Map<String, String> params) {

        //Long projectId = projectRepository.findByProjectName(projectName).getId();

        return objectMapper.convertValue(customBeneficiaryRepository.findBeneficiariesBasedOnCriteria(params), objectMapper.getTypeFactory().constructCollectionType(List.class, BeneficiaryResponse.class));
    }


    public String addTask(long activityId, TaskCreationRequest taskCreationRequest) {
        try {
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
            task.setBalanceRemaining(taskCreationRequest.getTotalCost());
            task.setUnitRemain(taskCreationRequest.getUnits());
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
            beneficiary.setName(bRequest.getBeneficiaryName());
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
        M_Component mComponent = mComponentRepository.findByNameAndBeneficiary(bRequest.getComponentName(), beneficiary);
        if (mComponent == null) {
            mComponent = new M_Component();
            mComponent.setName(bRequest.getComponentName());
            mComponent.setBeneficiary(beneficiary);
            mComponentRepository.save(mComponent);
        }

        // create the activity entity
        M_Activity mActivity = mActivityRepository.findByNameAndComponent(bRequest.getActivityName(), mComponent);
        if (mActivity == null) {
            mActivity = new M_Activity();
            mActivity.setName(bRequest.getActivityName());
            mActivity.setComponent(mComponent);
            mActivityRepository.save(mActivity);
        }

        M_Task mTask = mTaskRepository.findByNameAndActivity(bRequest.getTaskName(), mActivity);
        // create the task entity
        if (mTask == null) {
            mTask = new M_Task();
            mTask.setName(bRequest.getTaskName());
            mTask.setTypeOfUnit(bRequest.getTypeOfUnit());
            mTask.setUnits(bRequest.getUnits());
            mTask.setRatePerUnit(bRequest.getRatePerUnit());
            mTask.setTotalCost(bRequest.getTotalCost());
            mTask.setBalanceRemaining(bRequest.getTotalCost());
            mTask.setUnitRemain(bRequest.getUnits());
            mTask.setBeneficiaryContribution(bRequest.getBeneficiaryContribution());
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
            task.setName(taskCreationRequest.getName());
            task.setTypeOfUnit(taskCreationRequest.getTypeOfUnit());
            task.setUnits(taskCreationRequest.getUnits());
            task.setRatePerUnit(taskCreationRequest.getRatePerUnit());
            task.setTotalCost(taskCreationRequest.getTotalCost());
            task.setBeneficiaryContribution(taskCreationRequest.getBeneficiaryContribution());
            task.setGrantAmount(taskCreationRequest.getGrantAmount());
            task.setYearOfSanction(taskCreationRequest.getYearOfSanction());
            if(taskCreationRequest.getTotalCost() != null ){
                task.setBalanceRemaining(taskCreationRequest.getTotalCost());
            }
            if( taskCreationRequest.getUnits() > 0 ){
                task.setUnitRemain(taskCreationRequest.getUnits());
            }
            mTaskRepository.save(task);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("Exception occurred : {}", e.getMessage());
            return "FAILURE";
        }
    }

    public String addTaskUpdate(long taskId, TaskUpdateDTO taskUpdateDTO) throws IOException {
        try {
            M_Task mTask = mTaskRepository.findById(taskId);

            Document passbookDoc = documentStorageService.storeFile(taskUpdateDTO.getPassbookDoc());

            List<Document> otherDocuments = taskUpdateDTO.getOtherDocs().stream()
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
        if (mTask.getUnits() - taskUpdateDTO.getAchievementUnit() >= 0) {
            mTask.setUnitRemain(mTask.getUnits() - taskUpdateDTO.getAchievementUnit());
            mTask.setBalanceRemaining(mTask.getTotalCost() - (taskUpdateDTO.getAchievementUnit() * mTask.getRatePerUnit()));
            mTaskRepository.save(mTask);
            return false;
        }
        return true;
    }

    public String updateTaskUpdate(long taskUpdateId, TaskUpdateDTO taskUpdateDTO) {
        try {
            M_Task_Update m_task_update = mTaskUpdateRepository.findById(taskUpdateId).orElse(null);
            if (m_task_update != null) {
                M_Task mTask = m_task_update.getTask();
                if (taskUpdateDTO.getPayeeName() != null && !taskUpdateDTO.getPayeeName().isEmpty()) {
                    m_task_update.setPayeeName(taskUpdateDTO.getPayeeName());
                }
                if (taskUpdateDTO.getAchievementUnit() != 0 && !checkInvalidDataLimit(taskUpdateDTO, mTask)) {
                    m_task_update.setAchievementUnit(taskUpdateDTO.getAchievementUnit());
                }
                if (taskUpdateDTO.getAchievementUnit() != 0 && checkInvalidDataLimit(taskUpdateDTO, mTask)) {
                    return "FAILURE";
                }
                if (taskUpdateDTO.getPassbookDoc() != null && !taskUpdateDTO.getPassbookDoc().isEmpty()) {
                    Document passbookDoc = documentStorageService.storeFile(taskUpdateDTO.getPassbookDoc());
                    m_task_update.setPassbookDoc(passbookDoc);
                }
                if (taskUpdateDTO.getOtherDocs() != null && !taskUpdateDTO.getOtherDocs().isEmpty()) {
                    List<Document> otherDocuments = taskUpdateDTO.getOtherDocs().stream()
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
