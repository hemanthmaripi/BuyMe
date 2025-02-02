package com.example.demo.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}


	public User registerUser(User user) {
		if(userRepository.findByUserName(user.getUserName()).isPresent()) {
			throw new RuntimeException("User already exits");
		}

		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already registered");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}
}
