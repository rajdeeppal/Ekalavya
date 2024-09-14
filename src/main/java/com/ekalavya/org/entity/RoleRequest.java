package com.ekalavya.org.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String requestedRole;
	private String status; //PENDING, APPROVED, REJECTED
	private String approverComments;
	private LocalDate requestDate;
	private LocalDate approvalDate;
}
