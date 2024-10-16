package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import com.ekalavya.org.entity.M_Component;
import com.ekalavya.org.entity.Project;
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


    public List<M_Beneficiary> findBeneficiariesBasedOnCriteria(Map<String, String> params){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<M_Beneficiary> query = cb.createQuery(M_Beneficiary.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        for(Map.Entry<String, String> entry : params.entrySet()){
            predicates.add(cb.equal(beneficiaryRoot.get(entry.getKey()), entry.getValue()));
        }
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        return entityManager.createQuery(query).getResultList();
    }

    public List<M_Beneficiary> findBeneficiariesWithCriteria(String projectName, String componentName, String stateName, String districtName, int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<M_Beneficiary> query = cb.createQuery(M_Beneficiary.class);
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        Join<M_Beneficiary, Project> projectJoin = beneficiaryRoot.join("project", JoinType.INNER);
        Join<M_Beneficiary, M_Component> componentJoin = beneficiaryRoot.join("components", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

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

        query.where(predicates.toArray(new Predicate[0]));
        query.select(beneficiaryRoot).distinct(true);


        beneficiaryRoot.fetch("components", JoinType.LEFT)
                .fetch("activities", JoinType.LEFT)
                .fetch("tasks", JoinType.LEFT)
                .fetch("taskUpdates", JoinType.LEFT);

        TypedQuery<M_Beneficiary> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    public long countBeneficiariesWithCriteria(String projectName, String componentName, String stateName, String districtName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        Join<M_Beneficiary, Project> projectJoin = beneficiaryRoot.join("project", JoinType.INNER);
        Join<M_Beneficiary, M_Component> componentJoin = beneficiaryRoot.join("components", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

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

        query.select(cb.count(beneficiaryRoot));
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getSingleResult();
    }
}
