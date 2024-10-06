package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Activity;
import com.ekalavya.org.entity.M_Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MActivityRepository extends JpaRepository<M_Activity, Long> {

    M_Activity findById(long id);
    M_Activity findByActivityNameAndComponent(String name, M_Component component);

    @Query("SELECT mActivity FROM M_Activity mActivity WHERE mActivity.isCompleted = 'N'")
    Page<M_Activity> findIncompleteActivities(Pageable pageable);
}
