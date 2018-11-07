package com.java.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Patient {
	@Id
	@GeneratedValue
	int id;
	String firstName;
	String lastName;
	String emailId;
	long phoneNumber;
	
}
