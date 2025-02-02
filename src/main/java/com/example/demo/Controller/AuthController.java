package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.EmailRequest;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.Entity.User;
import com.example.demo.Service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;

	}

	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse respone) {
		try {
			System.out.println(request.getUsername() + "   " + request.getPassword());
			User user = authService.authenticate(request.getUsername(), request.getPassword());
			
			System.out.println(user.getUserName());
			
			String token = authService.generateToken(user);
			System.out.println(token);

			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(3600);
			respone.addCookie(cookie);

			respone.addHeader("Set-Cookie",
					String.format("authToken=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=None", token));

			Map<String, Object> responseBody = new HashMap<>();

			responseBody.put("message", "Login SuccessFull");
			responseBody.put("role", user.getRole().name());
			responseBody.put("username", user.getUserName());

			return ResponseEntity.ok(responseBody);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return ResponseEntity.ok("We couldn't found any acoount with this email... please check and try");
		}

		catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	
	 @PostMapping("/logout")
	 @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,HttpServletResponse response) {
	        try {
	        	System.out.println("I came into the lougut dunctionality");
	        	User user=(User) request.getAttribute("authenticatedUser");
	            authService.logout(user);
	            Cookie cookie = new Cookie("authToken", null);
	            cookie.setHttpOnly(true);
	            cookie.setMaxAge(0);
	            cookie.setPath("/");
	            response.addCookie(cookie);
	            Map<String, String> responseBody = new HashMap<>();
	            responseBody.put("message", "Logout successful");
	            return ResponseEntity.ok(responseBody);
	        } catch (RuntimeException e) {
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("message", "Logout failed");
	            e.printStackTrace();
	            return ResponseEntity.status(500).body(errorResponse);
	        }
	    }
	
//	@PostMapping("/sendOtp")
//	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//	public ResponseEntity<?> sendOtp(@RequestBody EmailRequest email) {
//
//		try {
//			String otp = authService.sendOtp(email.getEmail());
//
//			return ResponseEntity.ok(otp);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.badRequest().body("Something went wrong");
//		}
//	}

//	@PostMapping("/verifyOtp")
//	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//	public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
//		try {
//			String email = request.get("email");
//			String otp = request.get("otp");
//
//			if (authService.verifyOtp(email, otp)) {
//				return ResponseEntity.ok("Password reseted succesfully");
//			} else {
//				return ResponseEntity.badRequest().body("Something Went Wrong");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.badRequest().body("Something Went Wrong");
//		}
//	}

//	@PostMapping("/resetPassword")
//	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
//		try {
//			String email = request.get("email");
//			String otp = request.get("newPassword");
//			System.out.println(authService.resetPassword(email, otp));
//			if (authService.resetPassword(email, otp)) {
//				Map<String, String> map = new HashMap<>();
//				map.put("message", "Password Changes Successfully");
//				return ResponseEntity.ok(map);
//			} else {
//				Map<String, String> map = new HashMap<>();
//				map.put("message", "Something Went Wrong");
//				return ResponseEntity.ok(map);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.badRequest().body("Something Went Wrong");
//		}
//
//	}
}
