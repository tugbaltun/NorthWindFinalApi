package com.example.northWindFinal.business.abstracts;

import java.util.List;
import java.util.Map;
import com.example.northWindFinal.entities.concretes.Order;
import com.example.northWindFinal.entities.concretes.OrderDetail;

public interface ISellingService {
	
	Order getOrderById(Integer orderId);
	Order addOrder(Order order);
	Order updateOrder(Integer orderId, Order order);
	List<OrderDetail> getByOrderId(Integer customerId); 
	Map<String, List<OrderDetail>> addCardToOrderDetail(String customerId);

}
