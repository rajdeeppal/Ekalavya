package com.ekalavya.org.repository;

import com.ekalavya.org.entity.Component;
import com.ekalavya.org.entity.Vertical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    Component findByComponentNameAndVertical(String componentName, Vertical vertical);

    List<Component> findByVertical(Vertical verticalEntity);

    Component findByComponentName(String component);
}
