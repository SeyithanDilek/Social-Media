package com.example.backend.model;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_email")
public class NotificationEmail {
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="recipient")
	private String recipient;
	
	@Column(name="body")
	private String body;

}
