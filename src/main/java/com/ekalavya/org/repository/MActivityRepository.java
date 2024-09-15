package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Activity;
import com.ekalavya.org.entity.M_Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MActivityRepository extends JpaRepository<M_Activity, Long> {

    M_Activity findById(long id);
    M_Activity findByNameAndComponent(String name, M_Component component);
}
