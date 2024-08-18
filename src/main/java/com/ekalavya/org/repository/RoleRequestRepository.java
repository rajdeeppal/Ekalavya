package com.ekalavya.org.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ekalavya.org.entity.RoleRequest;

@Repository
public interface RoleRequestRepository extends JpaRepository<RoleRequest, Long>{
	
	List<RoleRequest> findByStatus(String status);

}
