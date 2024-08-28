package com.ekalavya.org.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String password;
	private String emailid;
	private String domain;
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Column(unique = true)
	private Long empId;
}
