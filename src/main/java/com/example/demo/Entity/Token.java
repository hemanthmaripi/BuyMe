package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tokens")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tokenid;

	@ManyToOne
	@JoinColumn(name="userid", nullable = false)
	private User user;

	@Column(nullable = false, name="jwttoken")
	private String jwttoken;

	@Column(nullable = false, name="expiresat")
	private LocalDateTime expiresat;

	public Token(Integer tokenid, User user, String jwttoken, LocalDateTime expiresat) {
		super();
		this.tokenid = tokenid;
		this.user = user;
		this.jwttoken = jwttoken;
		this.expiresat = expiresat;
	}

	public Token() {
		super();
	}

	public Integer getTokenid() {
		return tokenid;
	}

	public void setTokenid(Integer tokenid) {
		this.tokenid = tokenid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public Token(User user, String jwttoken, LocalDateTime expiresat) {
		super();
		this.user = user;
		this.jwttoken = jwttoken;
		this.expiresat = expiresat;
	}

	public LocalDateTime getExpiresat() {
		return expiresat;
	}

	public void setExpiresat(LocalDateTime expiresat) {
		this.expiresat = expiresat;
	}

}
