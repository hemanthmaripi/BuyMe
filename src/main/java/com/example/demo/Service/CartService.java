package com.example.demo.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.CartItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.User;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ImageRepository imageRepository;


	public Map<String, Object> getCartItems(User user) {


		List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(user.getUserId());

		Map<String, Object> response = new HashMap<>();

		//User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found by this id"));

		response.put("username", user.getUserName());
		response.put("role", user.getRole());

		List<Map<String, Object>> products = new ArrayList<>();
		int overalltotalPrice = 0;

		for(CartItem cartItem : cartItems) {
			Map<String, Object> productDetails = new HashMap<>();

			Product product = cartItem.getProduct();

			List<ProductImage> productImages = imageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = (productImages != null && !productImages.isEmpty()) ? productImages.get(0).getImage_url(): "default-image-url";

			productDetails.put("product_id", product.getProductId());
			productDetails.put("image_url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity()*product.getPrice().doubleValue());

			overalltotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();

			products.add(productDetails);
		}

		Map<String, Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("over_all_total_price", overalltotalPrice);

		response.put("cart", cart);

		return response;

	}

	public int getCartCount(String username) {
		User user = userRepository
				.findByUserName(username)
				.orElseThrow(()-> new RuntimeException("User not found on this username"));
		return cartRepository.countTotalItems(user);
	}


	public void addToCart(int userId, int productId, int quantity) {
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new RuntimeException("User not found with this id"));

		Product product = productRepository.findById(productId)
				.orElseThrow(()-> new RuntimeException("Product not found wiht this id"));

		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(user, product);

		if(existingItem.isPresent()) {
			CartItem item = existingItem.get();
			item.setQuantity(item.getQuantity()+quantity);
			cartRepository.save(item);
		} else {
			CartItem item = new CartItem(user, product, quantity);
			cartRepository.save(item);
		}
	}


	public void updateCartItemQuantity(int userId, int productId, int quantity) {
		User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found by this id"));

		Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found by this id"));

		Optional<CartItem> cartItem = cartRepository.findByUserAndProduct(user, product);

		if(cartItem.isPresent()) {
			CartItem existingItem = cartItem.get();
			if(quantity==0) {
				// deleteCartItem(existingItem);
			} else {
				existingItem.setQuantity(quantity);
				cartRepository.save(existingItem);
			}
		}
	}

	public void deleteCartItem(int userId, int productId) {
		Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));

		cartRepository.deletecartItem(userId, product.getProductId());
	}

}
