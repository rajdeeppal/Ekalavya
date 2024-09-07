package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<M_Beneficiary, Long> {

    M_Beneficiary findByAadharNumber(long aadharNumber);

    List<M_Beneficiary> findByProjectName(String projectName);

}
