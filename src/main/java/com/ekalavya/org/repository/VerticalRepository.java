package com.ekalavya.org.repository;

import com.ekalavya.org.entity.Vertical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerticalRepository extends JpaRepository<Vertical, Long> {
    Vertical findByVerticalName(String verticalName);
}
