package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Token;

import jakarta.transaction.Transactional;


@Repository
public interface JWTTokenRepository extends JpaRepository<Token, Integer> {
	Optional<Token> findByJwttoken(String jwttoken);
	
	 // Custom query to find tokens by user ID
    @Query("SELECT t FROM Token t WHERE t.user.id = :id")
    Token findByUserId(@Param("id") int id);

    // Custom query to delete tokens by user ID
    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.user.id = :id")
    void deleteByUserId(@Param("id") int id);
}
