package com.ekalavya.org.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
	List<User> findByRole(Role role);
	List<User> findByRoleIsNull();
	
	@Query("SELECT u FROM User u JOIN RoleRequest rr ON rr.user = u WHERE u.role = :role AND rr.status = :status")
    List<User> findApprovedUsersWithSpecificRole(@Param("role") Role role, @Param("status") String status);

	@Query("SELECT MAX(u.empId) FROM User u")
	Long findMaxEmpId();
}
