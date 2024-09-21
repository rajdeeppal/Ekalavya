package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import com.ekalavya.org.entity.M_Component;
import com.ekalavya.org.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<M_Beneficiary, Long> {

    //M_Beneficiary findByAadharNumber(long aadharNumber);

    @Query("SELECT e FROM M_Beneficiary e WHERE e.project = :project AND e.aadharNumber = :aadhar AND e.terminate = 'N'")
    M_Beneficiary findByAadharAndProjectAndProjectTermination(@Param("project") Project project, @Param("aadhar") Long aadhar);

    @Query("SELECT e FROM M_Beneficiary e WHERE e.aadharNumber = :aadhar AND e.terminate = 'N'")
    List<M_Beneficiary> findByAadharNumberAndProjectTermination(@Param("aadhar") Long aadharNumber);

    @Query("SELECT a FROM M_Beneficiary a WHERE a.terminate = 'N'")
    Page<M_Beneficiary> findIncompleteBeneficiaries(Pageable pageable);
}
