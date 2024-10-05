package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Resolution_Update;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MResolutionUpdateRepository extends JpaRepository<M_Resolution_Update, Long> {
    @Query("SELECT m FROM M_Resolution_Update m WHERE m.project.projectName = :projectName ORDER BY m.uploadTimestamp DESC")
    List<M_Resolution_Update> findByProjectNameOrderByTimestampDesc(@Param("projectName") String projectName, Pageable pageable);

}
