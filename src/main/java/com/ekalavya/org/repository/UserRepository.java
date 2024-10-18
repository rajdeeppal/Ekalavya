package com.ekalavya.org.repository;

import com.ekalavya.org.entity.Role;
import com.ekalavya.org.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.empId = :empId AND u.isActive = 'Y'")
	Optional<User> findByEmployeeId(@Param("empId") Long empId);

	List<User> findByRole(Role role);

	@Query("SELECT u FROM User u WHERE u.role IS NULL")
	List<User> findByRoleIsNull();
	
	@Query("SELECT u FROM User u JOIN RoleRequest rr ON rr.user = u WHERE u.role = :role AND rr.status = :status")
    List<User> findApprovedUsersWithSpecificRole(@Param("role") Role role, @Param("status") String status);

	@Query("SELECT MAX(u.empId) FROM User u")
	Long findMaxEmpId();

	@Query("SELECT u FROM User u WHERE u.component = :componentName AND u.vertical = :verticalName AND u.isActive = 'Y'")
    List<User> findDomainExpertByComponentAndVertical(String componentName, String verticalName);
}
