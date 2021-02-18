package com.example.northWindFinal.business.concretes;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.northWindFinal.TestBase;
import com.example.northWindFinal.dataAccess.concretes.OrderDetailRepository;
import com.example.northWindFinal.dataAccess.concretes.OrderRepository;
import com.example.northWindFinal.dataAccess.concretes.ShoppingCardRepository;
import com.example.northWindFinal.entities.concretes.Order;
import com.example.northWindFinal.entities.concretes.OrderDetail;
import com.example.northWindFinal.entities.concretes.ShoppingCard;
import com.example.northWindFinal.exception.ApiErrorException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SellingManagerTest extends TestBase{
	
	@InjectMocks private SellingManager sellingManager;
	@MockBean private OrderRepository orderRepository;
	@MockBean private OrderDetailRepository orderDetailRepository;
	@MockBean private ShoppingCardRepository shoppingCardRepository;

	@Test
	void testAddCardToOrderDetail() {
		List<ShoppingCard> shoppingCards = new ArrayList<ShoppingCard>();
		ShoppingCard shoppingCard1 = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
		ShoppingCard shoppingCard2 = new ShoppingCard(2, "ALFKI", 3, 3, 10.5);
		ShoppingCard shoppingCard3 = new ShoppingCard(3, "ALFKI", 3, 2, 10.5);
		shoppingCards.add(shoppingCard1);
		shoppingCards.add(shoppingCard2);
		shoppingCards.add(shoppingCard3);
		
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		OrderDetail orderDetail1 = new OrderDetail(1, 100, 1, 10.5, 2 );
		OrderDetail orderDetail2 = new OrderDetail(2, 100, 3, 10.5, 3 );
		OrderDetail orderDetail3 = new OrderDetail(3, 100, 3, 10.5, 2 );
		orderDetails.add(orderDetail1);
		orderDetails.add(orderDetail2);
		orderDetails.add(orderDetail3);
		OrderDetail orderDetailSave = new OrderDetail();
		Mockito.when(shoppingCardRepository.findByCustomer("ALFKI")).thenReturn(shoppingCards);
		Mockito.when(orderDetailRepository.findMaxOrderId()).thenReturn(3);
		Mockito.when(orderDetailRepository.findMaxId()).thenReturn(1);
		Mockito.when(orderDetailRepository.findByOrderId(Mockito.anyInt())).thenReturn(orderDetails);
		Mockito.when(orderDetailRepository.save(orderDetailSave)).thenReturn(orderDetailSave);
		
		Map<String, List<OrderDetail>> response = new HashMap<>();
		response.put("Siparisiniz Alindi!", orderDetails);
		String expected = asJsonString(response);
		assertEquals(expected,asJsonString(sellingManager.addCardToOrderDetail("ALFKI")) );
		
	}
	
	@Test
	void testAddCardToOrderDetailThrowException() {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> sellingManager.addCardToOrderDetail("ALFKI")
				);
		assertTrue(exception.getMessage().contains("Card cannot be found in the cache"));
	}

	@Test
	void testAddOrder() {
		Order order = new Order(1, "ALFKI", "Sipariş Hazırlanıyor");
		Mockito.when(orderRepository.save(order)).thenReturn(order);
		String expectedJson = asJsonString(order);
		String actualInJson = asJsonString(sellingManager.addOrder(order));
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testUpdateOrder() {
		Order orderToUpdate = new Order("ALFKI", "Sipariş Tamamlandı");
		Order orderToReturnFindBy = new Order(1, "ALFKI", "Sipariş Hazırlanıyor");
		Order orderToReturnSave = new Order(1, "ALFKI", "Sipariş Tamamlandı");
		Mockito.when(orderRepository.findById(1)).thenReturn(Optional.ofNullable(orderToReturnFindBy));
		Mockito.when(orderRepository.save(orderToReturnSave)).thenReturn(orderToReturnSave);
		String expectedJson = asJsonString(orderToReturnSave);
		String actualInJson = asJsonString(sellingManager.updateOrder(1,orderToUpdate));
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testUpdateOrderThrowException() {
		Order order = new Order(1, "ALFKI", "Sipariş Tamamlandı");
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> sellingManager.updateOrder(1, order)
				);
		assertTrue(exception.getMessage().contains("No order with id:"+1));
	}
	

}
