package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Activity;
import com.ekalavya.org.entity.M_Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MTaskRepository extends JpaRepository<M_Task, Long> {
    M_Task findById(long id);
    M_Task findByNameAndActivity(String name, M_Activity mActivity);

    @Query("SELECT mtask FROM M_Task mtask WHERE mtask.isCompleted = 'N'")
    Page<M_Task> findIncompleteTasks(Pageable pageable);
}
