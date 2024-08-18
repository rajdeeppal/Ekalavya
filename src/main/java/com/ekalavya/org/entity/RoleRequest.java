package com.ekalavya.org.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RoleRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public RoleRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRequestedRole() {
		return requestedRole;
	}
	public void setRequestedRole(String requestedRole) {
		this.requestedRole = requestedRole;
	}
	public String getStatus() {
		return status;
	}
	public RoleRequest(Long id, User user, String requestedRole, String status, String approverComments,
			LocalDate requestDate, LocalDate approvalDate) {
		super();
		this.id = id;
		this.user = user;
		this.requestedRole = requestedRole;
		this.status = status;
		this.approverComments = approverComments;
		this.requestDate = requestDate;
		this.approvalDate = approvalDate;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApproverComments() {
		return approverComments;
	}
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}
	public LocalDate getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}
	public LocalDate getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String requestedRole;
	private String status; //PENDING, APPROVED, REJECTED
	private String approverComments;
	private LocalDate requestDate;
	private LocalDate approvalDate;
}
