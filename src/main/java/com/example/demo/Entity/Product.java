package com.example.demo.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_id")
	private Integer productId;

	@Column(nullable = false, name = "name")
	private String name;

	@Column(columnDefinition = "TEXT", name = "description")
	private String description;

	@Column(nullable = false, precision = 10, scale = 2, name = "price")
	private BigDecimal price;

	@Column(nullable = false, name = "stock")
	private Integer stock;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name = "created_at", updatable = false)
	private LocalDateTime created_at;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", name = "updated_at")
	private LocalDateTime updated_at;

	public Product() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
		this.updated_at = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}

	// Getters and setters
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	// Constructor for new products
	public Product(String name, String description, BigDecimal price, Integer stock, Category category) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
	}
}
