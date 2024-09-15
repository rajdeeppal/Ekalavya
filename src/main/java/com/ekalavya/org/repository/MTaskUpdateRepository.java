package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Task_Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MTaskUpdateRepository extends JpaRepository<M_Task_Update, Long> {
}
