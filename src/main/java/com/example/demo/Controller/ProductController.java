package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	AuthService authService;

	@GetMapping("/products")
	public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String category,
			HttpServletRequest request) {
		try {
			System.out.println("entered into products controller");
			User authenticatedUser = (User) request.getAttribute("authenticatedUser");

			Map<String, Object> response = new HashMap<>();

			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("name", authenticatedUser.getUserName());
			userInfo.put("role", authenticatedUser.getRole().name());

			response.put("user", userInfo);
			List<Product> products = productService.getProductsByCategory(category);

			List<Map<String, Object>> productList = new ArrayList<>();

			for (Product product : products) {

				Map<String, Object> prod = new HashMap<>();

				prod.put("product_id", product.getProductId());
				prod.put("images", productService.getProductImages(product.getProductId()));
				System.out.println(productService.getProductImages(product.getProductId()));
				prod.put("name", product.getName());
				prod.put("description", product.getDescription());
				prod.put("price", product.getPrice());
				prod.put("stock", product.getStock());

				productList.add(prod);

			}
			response.put("products", productList);

			System.out.println(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}


	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam("keyword") String key, HttpServletRequest request) {
		try {
			List<Product> matchedProducts = productService.search(key);

			User user = (User) request.getAttribute("authenticatedUser");

			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("name", user.getUserName());
			userInfo.put("role", user.getRole());

			Map<String, Object> response = new HashMap<>();
			response.put("user", userInfo);

			List<Map<String, Object>> productList = new ArrayList<>();
			for(Product product : matchedProducts) {
				Map<String, Object> prod = new HashMap<>();
				prod.put("product_id", product.getProductId());
				prod.put("images", productService.getProductImages(product.getProductId()));
				prod.put("name", product.getName());
				prod.put("description", product.getDescription());
				prod.put("price", product.getPrice());
				prod.put("stock", product.getStock());

				productList.add(prod);

			}

			response.put("products", productList);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}
	}

}
