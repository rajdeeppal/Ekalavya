package com.ekalavya.org.repository;

import com.ekalavya.org.entity.Activity;
import com.ekalavya.org.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByActivityNameAndComponent(String name, Component component);

    List<Activity> findByComponent(Component componentEntity);
}
