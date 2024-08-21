package com.ekalavya.org.repository;

import com.ekalavya.org.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    Admin findByUsername(String username);
}
