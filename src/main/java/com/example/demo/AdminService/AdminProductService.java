package com.example.demo.AdminService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.ProductRepository;

@Service
public class AdminProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private CategoryRepository categoryRepository;


	public Product addProduct(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl) {
		
		System.out.println(categoryId);
		
		Optional<Category> category = categoryRepository.findById(categoryId);

		if(category.isEmpty()) {
			throw new RuntimeException("Invalid Category");
		}

		if(imageUrl==null) {
			throw new RuntimeException("Image Url cannot be empty...");
		}

		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setStock(stock);
		product.setPrice(BigDecimal.valueOf(price));
		product.setCategory(category.get());
		product.setCreated_at(LocalDateTime.now());
		product.setUpdated_at(LocalDateTime.now());

		Product savedProduct = productRepository.save(product);

		ProductImage productImage = new ProductImage();
		productImage.setProduct(savedProduct);
		productImage.setImage_url(imageUrl);

		imageRepository.save(productImage);

		return savedProduct;
	}



	public void deleteProduct(Integer prodId) {
			imageRepository.deleteByProductId(prodId);

			productRepository.deleteById(prodId);
	}

}
