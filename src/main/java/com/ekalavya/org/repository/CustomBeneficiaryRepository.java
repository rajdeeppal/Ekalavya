package com.ekalavya.org.repository;

import com.ekalavya.org.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomBeneficiaryRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public List<M_Beneficiary> findBeneficiariesBasedOnCriteria(Map<String, String> params) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<M_Beneficiary> query = cb.createQuery(M_Beneficiary.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            predicates.add(cb.equal(beneficiaryRoot.get(entry.getKey()), entry.getValue()));
        }
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        return entityManager.createQuery(query).getResultList();
    }

    public List<M_Beneficiary> findBeneficiariesWithStageCriteria(
            String projectName,
            String componentName,
            String stateName,
            String districtName,
            Long employeeId,
            String employeeRole,
            String stage,
            int page,
            int size) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<M_Beneficiary> query = cb.createQuery(M_Beneficiary.class);
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        Join<M_Beneficiary, Project> projectJoin = beneficiaryRoot.join("project", JoinType.INNER);
        Join<M_Beneficiary, M_Component> componentJoin = beneficiaryRoot.join("components", JoinType.LEFT);
        Join<M_Component, M_Activity> activityJoin = componentJoin.join("activities", JoinType.LEFT);
        Join<M_Activity, M_Task> taskJoin = activityJoin.join("tasks", JoinType.LEFT);
        Join<M_Task, M_Task_Update> taskUpdateJoin = taskJoin.join("taskUpdates", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // Apply common filters
        if (projectName != null && !projectName.isEmpty()) {
            predicates.add(cb.equal(projectJoin.get("projectName"), projectName));
        }

        if (componentName != null && !componentName.isEmpty()) {
            predicates.add(cb.equal(componentJoin.get("componentName"), componentName));
        }

        if (stateName != null && !stateName.isEmpty()) {
            predicates.add(cb.equal(beneficiaryRoot.get("stateName"), stateName));
        }

        if (districtName != null && !districtName.isEmpty()) {
            predicates.add(cb.equal(beneficiaryRoot.get("districtName"), districtName));
        }

        // Handle stage-specific logic based on role
        switch (employeeRole) {
            case "PM":
                switch (stage) {
                    case "sanction":
                        // No TaskUpdate: Exclude beneficiaries having task updates with currentlyPendingWith set
                        predicates.add(cb.isNull(taskUpdateJoin.get("pendingWith")));
                        break;
                    case "inprogress":
                        // PM in progress: currentlyPendingWith = PM and isRejection = N
                        predicates.add(cb.and(
                                cb.equal(taskUpdateJoin.get("pendingWith"), "PM"),
                                cb.equal(taskUpdateJoin.get("isRejectionOccurred"), false)));
                        break;
                    case "preview":
                        // Preview: currentlyPendingWith = DE and isRejection = N
                        predicates.add(cb.and(
                                cb.equal(taskUpdateJoin.get("pendingWith"), "DE"),
                                cb.equal(taskUpdateJoin.get("isRejectionOccurred"), false)));
                        break;
                    case "rejection":
                        // Rejection: currentlyPendingWith = PM and isRejection = Y
                        predicates.add(cb.and(
                                cb.equal(taskUpdateJoin.get("pendingWith"), "PM"),
                                cb.equal(taskUpdateJoin.get("isRejectionOccurred"), true)));
                        break;
                }
                break;

            case "DE":
                // DE role filter: currentlyPendingWith = DE and domainExpertEmpId = employeeId
                predicates.add(cb.and(
                        cb.equal(taskUpdateJoin.get("pendingWith"), "DE"),
                        cb.equal(taskUpdateJoin.get("domainExpertEmpId"), employeeId)));
                break;

            default:
                throw new RuntimeException("Unsupported role: " + employeeRole);
        }

        // Apply predicates to the query
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Ensure distinct beneficiaries to avoid duplicates caused by joins
        query.select(beneficiaryRoot).distinct(true);

        // Fetch only the required relationships (eager loading)
        beneficiaryRoot.fetch("components", JoinType.LEFT)
                .fetch("activities", JoinType.LEFT)
                .fetch("tasks", JoinType.LEFT)
                .fetch("taskUpdates", JoinType.LEFT);

        // Create the typed query with pagination
        TypedQuery<M_Beneficiary> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        // Execute the query and return results
        return typedQuery.getResultList();
    }


    public long countBeneficiariesWithCriteria(
            String projectName,
            String componentName,
            String stateName,
            String districtName,
            Long employeeId,
            String employeeRole,
            String stage) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        Join<M_Beneficiary, Project> projectJoin = beneficiaryRoot.join("project", JoinType.INNER);
        Join<M_Beneficiary, M_Component> componentJoin = beneficiaryRoot.join("components", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // Apply common filters
        if (projectName != null && !projectName.isEmpty()) {
            predicates.add(cb.equal(projectJoin.get("projectName"), projectName));
        }

        if (componentName != null && !componentName.isEmpty()) {
            predicates.add(cb.equal(componentJoin.get("componentName"), componentName));
        }

        if (stateName != null && !stateName.isEmpty()) {
            predicates.add(cb.equal(beneficiaryRoot.get("stateName"), stateName));
        }

        if (districtName != null && !districtName.isEmpty()) {
            predicates.add(cb.equal(beneficiaryRoot.get("districtName"), districtName));
        }

        // Handle stage-specific logic based on role
        Join<M_Beneficiary, M_Task_Update> taskUpdateJoin = beneficiaryRoot.join("components")
                .join("activities")
                .join("tasks")
                .join("taskUpdates", JoinType.LEFT);

        switch (employeeRole) {
            case "PM":
                switch (stage) {
                    case "sanction":
                        // No TaskUpdate: Exclude beneficiaries having task updates with currentlyPendingWith set
                        predicates.add(cb.isNull(taskUpdateJoin.get("pendingWith")));
                        break;
                    case "inprogress":
                        // PM in progress: pendingWith = PM and isRejection = N
                        predicates.add(cb.and(
                                cb.equal(taskUpdateJoin.get("pendingWith"), "PM"),
                                cb.equal(taskUpdateJoin.get("isRejectionOccurred"), false)));
                        break;
                    case "preview":
                        // Preview: currentlyPendingWith = DE and isRejection = N
                        predicates.add(cb.and(
                                cb.equal(taskUpdateJoin.get("pendingWith"), "DE"),
                                cb.equal(taskUpdateJoin.get("isRejectionOccurred"), false)));
                        break;
                    case "rejection":
                        // Rejection: currentlyPendingWith = PM and isRejection = Y
                        predicates.add(cb.and(
                                cb.equal(taskUpdateJoin.get("pendingWith"), "PM"),
                                cb.equal(taskUpdateJoin.get("isRejectionOccurred"), true)));
                        break;
                }
                break;

            case "DE":
                // DE role filter: currentlyPendingWith = DE and domainExpertEmpId = employeeId
                predicates.add(cb.and(
                        cb.equal(taskUpdateJoin.get("pendingWith"), "DE"),
                        cb.equal(taskUpdateJoin.get("domainExpertEmpId"), employeeId)));
                break;

            default:
                throw new RuntimeException("Unsupported role: " + employeeRole);
        }

        // Apply predicates to the query
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Count distinct beneficiaries
        query.select(cb.countDistinct(beneficiaryRoot));

        // Execute query and return the count
        return entityManager.createQuery(query).getSingleResult();
    }
}
