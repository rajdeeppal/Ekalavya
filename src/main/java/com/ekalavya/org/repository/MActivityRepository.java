package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MActivityRepository extends JpaRepository<M_Activity, Long> {

    M_Activity findById(long id);
}
