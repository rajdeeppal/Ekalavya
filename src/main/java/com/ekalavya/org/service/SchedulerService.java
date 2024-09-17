package com.ekalavya.org.service;

import com.ekalavya.org.entity.*;
import com.ekalavya.org.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulerService {

    @Autowired
    private MTaskRepository mTaskRepository;

    @Autowired
    private MActivityRepository mActivityRepository;

    @Autowired
    private MComponentRepository mComponentRepository;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private static int pageNumber = 0;
    private static int pageSize = 1000;

    @Transactional
    public void updateTaskCompletionStatus() {
        Page<M_Task> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = mTaskRepository.findIncompleteTasks(pageable);

            List<M_Task> updatedMTask = page.getContent()
                    .stream()
                    .peek(mTask -> {
                        boolean allTasksCompleted = mTask.getTaskUpdates()
                                .stream()
                                .allMatch(mTaskUpdate -> "Y".equals(mTaskUpdate.getIsCompleted()));
                        if (allTasksCompleted) {
                            mTask.setIsCompleted("Y");
                        }
                    })
                    .collect(Collectors.toList());
            mTaskRepository.saveAll(updatedMTask);
            pageNumber++;
        } while (page.hasNext());
    }

    @Transactional
    public void updateActivityCompletionStatus() {
        Page<M_Activity> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = mActivityRepository.findIncompleteActivities(pageable);

            List<M_Activity> updatedMActivity = page.getContent()
                    .stream()
                    .peek(mActivity -> {
                        boolean allTasksCompleted = mActivity.getTasks()
                                .stream()
                                .allMatch(mTask -> "Y".equals(mTask.getIsCompleted()));
                        if (allTasksCompleted) {
                            mActivity.setIsCompleted("Y");
                        }
                    })
                    .collect(Collectors.toList());
            mActivityRepository.saveAll(updatedMActivity);
            pageNumber++;
        } while (page.hasNext());
    }

    @Transactional
    public void updateComponentCompletionStatus() {
        Page<M_Component> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = mComponentRepository.findIncompleteComponents(pageable);

            List<M_Component> updatedMComponent = page.getContent()
                    .stream()
                    .peek(mComponent -> {
                        boolean allActivitiesCompleted = mComponent.getActivities()
                                .stream()
                                .allMatch(mActivity -> "Y".equals(mActivity.getIsCompleted()));
                        if (allActivitiesCompleted) {
                            mComponent.setIsCompleted("Y");
                        }
                    })
                    .collect(Collectors.toList());
            mComponentRepository.saveAll(updatedMComponent);
            pageNumber++;
        } while (page.hasNext());
    }

    @Transactional
    public void updateBeneficiaryCompletionStatus() {
        Page<M_Beneficiary> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = beneficiaryRepository.findIncompleteBeneficiaries(pageable);

            List<M_Beneficiary> updatedMComponent = page.getContent()
                    .stream()
                    .peek(mBeneficiary -> {
                        boolean allComponentsCompleted = mBeneficiary.getComponents()
                                .stream()
                                .allMatch(mComponent -> "Y".equals(mComponent.getIsCompleted()));
                        if (allComponentsCompleted) {
                            mBeneficiary.setTerminate("Y");
                        }
                    })
                    .collect(Collectors.toList());
            beneficiaryRepository.saveAll(updatedMComponent);
            pageNumber++;
        } while (page.hasNext());
    }

    @Transactional
    public void updateProjectCompletionStatus() {
        Page<Project> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = projectRepository.findIncompleteProjects(pageable);

            List<Project> updatedProject = page.getContent()
                    .stream()
                    .peek(project -> {
                        boolean allCompletedBeneficiaries = project.getMBeneficiaries()
                                .stream()
                                .allMatch(mBeneficiary -> "Y".equals(mBeneficiary.getTerminate()));
                        if (allCompletedBeneficiaries) {
                            project.setTerminate("Y");
                        }
                    })
                    .collect(Collectors.toList());
            projectRepository.saveAll(updatedProject);
            pageNumber++;
        } while (page.hasNext());
    }
}
