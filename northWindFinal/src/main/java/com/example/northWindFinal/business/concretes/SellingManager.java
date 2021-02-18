package com.example.northWindFinal.business.concretes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.northWindFinal.business.abstracts.ISellingService;
import com.example.northWindFinal.dataAccess.concretes.OrderDetailRepository;
import com.example.northWindFinal.dataAccess.concretes.OrderRepository;
import com.example.northWindFinal.dataAccess.concretes.ShoppingCardRepository;
import com.example.northWindFinal.entities.concretes.Order;
import com.example.northWindFinal.entities.concretes.OrderDetail;
import com.example.northWindFinal.entities.concretes.ShoppingCard;
import com.example.northWindFinal.exception.ApiErrorException;


@Service
public class SellingManager implements ISellingService{
	
	@Autowired private ShoppingCardRepository shoppingCardRepository;
	@Autowired private OrderDetailRepository orderDetailRepository;
	@Autowired private OrderRepository orderRepository;
	
	@Override
	public Order getOrderById(Integer orderId) {
	    return orderRepository
	            .findById(orderId)
	            .orElseThrow(() -> new ApiErrorException("No order with id:" + orderId));
	}
	
	@Override
	public Order addOrder(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public Order updateOrder(Integer orderId, Order order) {
		Order ordertoUpdate = 
				orderRepository
				.findById(orderId)
				.orElseThrow(() -> new ApiErrorException("No order with id:"+orderId));
		ordertoUpdate.setCustomerId(order.getCustomerId());
		ordertoUpdate.setOrderStatus(order.getOrderStatus());
		return orderRepository.save(ordertoUpdate);
	}
	
	@Override
	public List<OrderDetail> getByOrderId(Integer orderId) {
		if(orderDetailRepository.findByOrderId(orderId).isEmpty()) {
			throw new ApiErrorException("Card belongs to customer cannot be found in the cache");
		}
		return orderDetailRepository.findByOrderId(orderId);
	}

	@Override
	public Map<String, List<OrderDetail>> addCardToOrderDetail(String customerId) {
		List<ShoppingCard> cards = shoppingCardRepository.findByCustomer(customerId);
		OrderDetail orderDetail = new OrderDetail(); 
		int orderId = orderDetailRepository.findMaxOrderId()+1;
		int id = 0; orderDetailRepository.findMaxId();
		if(cards.isEmpty()) {
			throw new ApiErrorException("Card cannot be found in the cache");
		}
		
		else {
			for (int i = 0; i < cards.size(); i++) {
				id = orderDetailRepository.findMaxId();
				orderDetail.setId(id+1);
				orderDetail.setOrderId(orderId);
				orderDetail.setProductId(cards.get(i).getProductId());
				orderDetail.setQuantity(cards.get(i).getQuantity());
				orderDetail.setUnitPrice(cards.get(i).getUnitPrice());
				orderDetail.setDiccount(0);
				orderDetailRepository.save(orderDetail);
				shoppingCardRepository.delete(cards.get(i));
			}
		}
		
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		orderDetails.addAll(orderDetailRepository.findByOrderId(orderDetail.getOrderId()));
		Map<String, List<OrderDetail>> response = new HashMap<>();
		response.put("Siparisiniz Alindi!", orderDetails);
		return response;
	}
}
