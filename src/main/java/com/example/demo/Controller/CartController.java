package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserRepository userRepository;


	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> getCart(HttpServletRequest request) {

		User user = (User)request.getAttribute("authenticatedUser");

		Map<String, Object> cartItems = cartService.getCartItems(user);

		return ResponseEntity.ok(cartItems);

	}


	@GetMapping("/items/count")
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	public ResponseEntity<Integer> getCartCount(@RequestParam String username) {
		Integer count = cartService.getCartCount(username);
		return ResponseEntity.ok(count);
	}


	@PostMapping("/add")
	 @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	public ResponseEntity<Void> addItemToCart(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");

		User user = userRepository
				.findByUserName(username)
				.orElseThrow(()-> new RuntimeException("Usernot found by this username")) ;

		int productId = (int) request.get("productId");

		int quantity = request.containsKey("quantity") ? (int)request.get("quantity") : 1;

		cartService.addToCart(user.getUserId(), productId, quantity);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/update")
	@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
	public ResponseEntity<Void> updateCart(@RequestBody Map<String, Object> request) {

		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = (int) request.get("quantity");

		User user = userRepository.findByUserName(username).orElseThrow(()-> new RuntimeException("Usernot Found"));

		cartService.updateCartItemQuantity(user.getUserId(), productId, quantity);

		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@DeleteMapping("/delete")
	@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
	public ResponseEntity<Void> delteCartItem(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");

		User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException("User not found"));

		cartService.deleteCartItem(user.getUserId(), productId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
