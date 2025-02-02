package com.example.demo.AdminController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AdminService.AdminProductService;
import com.example.demo.Entity.Product;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

	@Autowired
	private AdminProductService adminProductService;

	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> product) {
		try {
		String name = (String) product.get("name");
		String description = (String) product.get("description");
		Double price = Double.valueOf(String.valueOf(product.get("price")));
		Integer stock = (Integer) product.get("stock");
		Integer categoryId = (Integer) product.get("categoryId");
		String imageUrl = (String) product.get("imageUrl");

		Product addedProduct = adminProductService.addProduct(name, description, price, stock, categoryId, imageUrl);

		return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
		}
	}


	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProduct(@RequestBody Map<String, Integer> prod) {
		try {
			Integer prodId = prod.get("productId");
			adminProductService.deleteProduct(prodId);
			return ResponseEntity.ok("Product Deleted Successfully...");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
