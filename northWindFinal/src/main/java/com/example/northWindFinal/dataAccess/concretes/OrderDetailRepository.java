package com.example.northWindFinal.dataAccess.concretes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.northWindFinal.entities.concretes.OrderDetail;



public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	
	//INSERT INTO order_details VALUES (10537, 51, 53, 6, 0);
	//@Query(value="SELECT * FROM order_details where (order_id = :#{#order.orderId}) AND (product_id = :#{#order.productId})", nativeQuery=true)
	//void addByCustomer(@Param("order") OrderDetail order);
	
//	@Query(value="INSERT INTO order_details VALUES (:#{#order.orderId},:#{#order.productId},"
//			+ " :#{#order.quantity}, :#{#order.unitPrice} )", nativeQuery = true)
//	void addByCustomer(@Param("order") OrderDetail orderDetail);
	
	@Query(value="SELECT MAX(order_id) FROM order_details", nativeQuery = true)
	int findMaxOrderId();
	
	@Query(value="SELECT MAX(id) FROM order_details", nativeQuery = true)
	int findMaxId();
	
	@Query(value="SELECT * FROM order_details WHERE (order_id = :orderId)", nativeQuery = true)
	List<OrderDetail> findByOrderId(@Param("orderId") Integer orderId);

}
