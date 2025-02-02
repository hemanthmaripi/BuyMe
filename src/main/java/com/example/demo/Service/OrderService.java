package com.example.demo.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.ProductRepository;

@Service
public class OrderService {

	public OrderService(OrderItemRepository orderItemRepository, ProductRepository productRepository,
			ImageRepository imageRepository) {
		super();
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.imageRepository = imageRepository;
	}

	private OrderItemRepository orderItemRepository;

	private ProductRepository productRepository;

	private ImageRepository imageRepository;

	public Map<String, Object> getOrders(User user) {

		List<OrderItem> orderItems = orderItemRepository.findSuccessfulOrderItemsByUserId(user.getUserId());

		Map<String, Object> response = new HashMap<>();

		response.put("username", user.getUserName());
		response.put("role", user.getRole());

		List<Map<String, Object>> products = new ArrayList<>();

		for(OrderItem oi : orderItems) {
			Product product = productRepository.findById(oi.getProductId()).orElse(null);

			if(product==null) {
				continue;
			}

			List<ProductImage> images = imageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = images.get(0).getImage_url();

			Map<String, Object> productDetails = new HashMap<>();

			productDetails.put("order_id", oi.getOrder().getOrderId());
			productDetails.put("quantity", oi.getQuantity());
			productDetails.put("total_price", oi.getTotalPrice());
			productDetails.put("image_url", imageUrl);
			productDetails.put("product_id", product.getProductId());
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());

			products.add(productDetails);

		}

		response.put("products", products);

		return response;

	}


}
