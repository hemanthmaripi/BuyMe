package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByCategory_CategoryId(Integer categoryId);

	List<Product> findByNameContainingIgnoringCaseOrDescriptionContainingIgnoringCase(String name, String description);

	@Query("SELECT p FROM Product p WHERE p.name LIKE :name OR p.description LIKE :desc")
	List<Product> fuzzySearch(@Param("name") String name, @Param("desc") String desc);

	@Query("SELECT p.category.categoryName FROM Product p WHERE p.productId = :productId")
	String findCategoryNameByProductId(int productId);
}
