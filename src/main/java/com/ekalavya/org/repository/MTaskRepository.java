package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Activity;
import com.ekalavya.org.entity.M_Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MTaskRepository extends JpaRepository<M_Task, Long> {
    M_Task findById(long id);
    M_Task findByNameAndActivity(String name, M_Activity mActivity);
}
