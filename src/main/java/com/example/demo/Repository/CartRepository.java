package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.CartItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{

	@Query("SELECT c FROM cartitems c JOIN FETCH c.product p LEFT JOIN FETCH ProductImage pi ON pi.product = p WHERE c.user.userId = :userId")
	List<CartItem> findCartItemsWithProductDetails(@Param("userId") int userId);


	Optional<CartItem> findByUserAndProduct(User user, Product product);

	List<CartItem> findByUser(User user);

	 @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM cartitems c WHERE c.user = :user")
	    int countTotalItems(@Param("user") User user);

	 @Query("SELECT COUNT(c) FROM cartitems c WHERE c.user = :user")
		int countCartItemByUserId(@Param("user") User user);

	@Query("SELECT u.id FROM User u WHERE u.userName = :username")
	    int findUserIdByUsername(@Param("username") String username);


		@Query("UPDATE cartitems c SET c.quantity = :quantity WHERE c.id = :cartItemId")
		void updateCartItemQuantity(int cartItemId, int quantity);


		@Modifying
		@Transactional
		@Query("DELETE FROM cartitems c WHERE c.user.userId = :userId AND c.product.productId = :productId")
		void deletecartItem(int userId, int productId);

		 @Modifying
		    @Transactional
		    @Query("DELETE FROM cartitems c WHERE c.user.userId = :userId")
		    void deleteAllCartItemsByUserId(int userId);
}
