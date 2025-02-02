package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.ProductImage;

import jakarta.transaction.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Integer> {
	 List<ProductImage> findByProduct_ProductId(Integer productId);

	  @Modifying
	    @Transactional
	    @Query("DELETE FROM ProductImage pi WHERE pi.product.productId = :productId")
	    void deleteByProductId(Integer productId);
}
