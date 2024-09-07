package com.ekalavya.org.repository;

import com.ekalavya.org.entity.Project;
import com.ekalavya.org.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.user = :user AND p.terminate = 'N'")
    List<Project> findActiveProjectByUser(@Param("user") User user);

    Project findByProjectName(String projectName);
}
