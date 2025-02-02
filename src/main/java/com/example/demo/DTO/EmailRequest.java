package com.example.demo.DTO;

public class EmailRequest {
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmailRequest(String email) {
		super();
		this.email = email;
	}

	public EmailRequest() {
		super();
	}

}
