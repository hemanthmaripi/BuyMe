package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
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
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ImageRepository imageRepository;

	public List<Product> getProductsByCategory(String categoryName) {

		if(categoryName != null && !categoryName.trim().isEmpty()) {
			Optional<Category> optCategory = categoryRepository.findByCategoryName(categoryName);

			if(optCategory.isPresent()) {
				Category category = optCategory.get();
				return productRepository.findByCategory_CategoryId(category.getCategoryId());
			} else {
				throw new RuntimeException("We dont have any products with this category...");
			}
		} else {
			return productRepository.findAll();
		}

	}

	public List<String> getProductImages(Integer productId) {
		List<ProductImage> productImages = imageRepository.findByProduct_ProductId(productId);

		List<String> imageUrls = new ArrayList<>();

		for(ProductImage image : productImages) {
			imageUrls.add(image.getImage_url());
		}

		return imageUrls;
	}

	public List<Product> search(String key) {

		return productRepository.findByNameContainingIgnoringCaseOrDescriptionContainingIgnoringCase(key, key);

	}

}
