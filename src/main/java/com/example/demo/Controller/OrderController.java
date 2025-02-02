package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;
import com.example.demo.Service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins="http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/")
	@CrossOrigin(origins="http://localhost:5173", allowCredentials = "true")
	public ResponseEntity<Map<String, Object>> getOrders(HttpServletRequest request) {

		try {

			User user = (User) request.getAttribute("authenticatedUser");

			if(user==null) {
				return ResponseEntity.status(401).body(Map.of("error", "Unauthenticated user"));
			}

			Map<String, Object> response = orderService.getOrders(user);

			return ResponseEntity.ok(response);

		}catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}


	}

}
