package com.bios.user.model.enums;

public enum Role {
	ADMIN(1, "ROLE_ADMIN"),
	USER(2, "ROLE_USER");
	
	private int cod;
	private String description;
	
	private Role(int cod, String description) {
		this.cod = cod;
		this.description = description;
	}

	public int getCod() {
		return cod;
	}

	public String getDescription() {
		return description;
	}

}
