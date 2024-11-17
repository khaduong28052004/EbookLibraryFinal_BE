package com.toel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toel.model.Account;
import com.toel.model.Cart;
import com.toel.model.Product;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	@Query(value = "SELECT c.* \r\n" + "FROM carts c " + "JOIN products p ON p.id = c.product_id "
			+ "JOIN accounts a ON a.id = c.account_id "
			+ "WHERE c.product_id = :productId AND c.account_id = :accountId", nativeQuery = true)
	Cart findCartByAccountIdAndProductId(@Param("productId") Integer productId, @Param("accountId") Integer accountId);

	List<Cart> findAllByAccount(Account user);

	Cart findByProductAndAccount(Product product, Account account);

}
