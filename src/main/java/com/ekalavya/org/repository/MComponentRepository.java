package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import com.ekalavya.org.entity.M_Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MComponentRepository extends JpaRepository<M_Component, Long> {
    M_Component findByNameAndBeneficiary(String name, M_Beneficiary m_beneficiary);
}
