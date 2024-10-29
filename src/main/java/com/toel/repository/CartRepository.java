package com.toel.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.toel.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	@Query(value = "SELECT c FROM carts  c WHERE c.product.id = ?1 AND c.account.id = ?2 ")
	Cart findCartByAccountIdAndProductId(Integer productId, Integer accountId);

}
