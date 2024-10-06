package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import com.ekalavya.org.entity.M_Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MComponentRepository extends JpaRepository<M_Component, Long> {
    M_Component findByComponentNameAndBeneficiary(String name, M_Beneficiary m_beneficiary);

    @Query("SELECT mComponent FROM M_Component mComponent WHERE mComponent.isCompleted = 'N'")
    Page<M_Component> findIncompleteComponents(Pageable pageable);
}
