package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
}
