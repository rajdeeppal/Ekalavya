package com.ekalavya.org.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	@Transient
	private String password;
	private String emailid;
	private String domain;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Role role;

	@Column(unique = true)
	private Long empId;

	public User() {
		this.isActive = "N";
	}

	private String isActive;
}
