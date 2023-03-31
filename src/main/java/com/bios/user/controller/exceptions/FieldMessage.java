package com.bios.user.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String message;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
