package com.example.northWindFinal.dataAccess.concretes;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.northWindFinal.entities.concretes.ShoppingCard;;

@Repository
public interface ShoppingCardRepository extends JpaRepository<ShoppingCard, Integer>{
	
	@Query(value="SELECT * FROM shopping_cards WHERE (customer_id = :customerId)", nativeQuery = true)
	List<ShoppingCard> findByCustomer(@Param("customerId") String customerId);
	
	@Query(value="SELECT * FROM shopping_cards WHERE (customer_id = :customerId) AND (product_id = :productId)", nativeQuery = true)
	ShoppingCard findByProduct(@Param("customerId") String customerId, @Param("productId") Integer productId);
	
//	@Query(value="SELECT * FROM cart_items where (cart_id = :#{#cartItem.cartId}) AND (product_id = :#{#cartItem.customerId})", nativeQuery=true)
//	List<CartItem> findByCustomer(@Param("cartItem") CartItem cartItem);

}