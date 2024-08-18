package com.ekalavya.org.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ekalavya.org.entity.RoleAudit;
import com.ekalavya.org.entity.User;

@Repository
public interface RoleAuditRepository extends JpaRepository<RoleAudit, Long>{

	RoleAudit findByUser(User user);

}
