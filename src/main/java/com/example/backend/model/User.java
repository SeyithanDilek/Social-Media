package com.example.backend.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	
	@NotBlank(message = "Username is requried")
	@Column(name="username")
	private String username;
	
	@NotBlank(message="Password is required")
	@Column(name="password")
	private String password;
	
	@Email
	@NotEmpty(message = "Email is required")
	@Column(name="email")
	private String email;
	
	@Column(name="created")
	private Instant created;
	
	@Column(name="enabled")
	private boolean enabled;
	
}
