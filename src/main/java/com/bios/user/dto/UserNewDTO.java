package com.bios.user.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Builder
public class UserNewDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotEmpty(message="Name is Required")
	@Length(min=2, max=120, message="name length must be between 2 to 120 characters")
	private String name;
	
	@NotEmpty(message="email required")
	@Email(message="Invalid Email")
	private String email;
	
	@NotEmpty(message="password is required")
	private String password;
	
}

