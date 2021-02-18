package com.example.northWindFinal.api.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.northWindFinal.TestBase;
import com.example.northWindFinal.business.abstracts.ISellingService;
import com.example.northWindFinal.entities.concretes.Order;
import com.example.northWindFinal.entities.concretes.OrderDetail;
import com.example.northWindFinal.entities.concretes.ShoppingCard;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@WebMvcTest(SellingController.class)
public class SellingControllerTest extends TestBase{
	
	@Autowired private MockMvc mockMvc;
	@InjectMocks private SellingController sellingController;
	@MockBean private ISellingService sellingService;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(sellingController)
				.build();
	}

	@Test
	void testAddOrderDetail() throws Exception {
		List<ShoppingCard> shoppingCards = new ArrayList<ShoppingCard>();
		ShoppingCard shoppingCard1 = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
		ShoppingCard shoppingCard2 = new ShoppingCard(2, "ALFKI", 3, 3, 10.5);
		ShoppingCard shoppingCard3 = new ShoppingCard(3, "ALFKI", 3, 2, 10.5);
		
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		OrderDetail orderDetail1 = new OrderDetail(1, 100, 1, 10.5, 2 );
		OrderDetail orderDetail2 = new OrderDetail(2, 100, 3, 10.5, 3 );
		OrderDetail orderDetail3 = new OrderDetail(3, 100, 3, 10.5, 2 );
		orderDetails.add(orderDetail1);
		orderDetails.add(orderDetail2);
		orderDetails.add(orderDetail3);
		
		OrderDetail orderDetail = new OrderDetail(1, 100, 1, 10.5, 1 );
		
		Map<String, List<OrderDetail>> response = new HashMap<>();
		response.put("Siparisiniz alindi!", orderDetails);
		String expected = asJsonString(response);
		
		Mockito.doReturn(response).when(sellingService).addCardToOrderDetail("ALFKI");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/selling/orderDetail/{id}","ALFKI")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		

		String actual = result.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}

	@Test
	void testAddOrder() throws Exception {
		Order orderToPost = new Order("ALFKI", "Siparis Hazirlaniyor");
		Order orderToReturn = new Order(1, "ALFKI", "Siparis Hazirlaniyor");
		Mockito.doReturn(orderToReturn).when(sellingService).addOrder(Mockito.any());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/selling/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(orderToPost));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expectedJson = asJsonString(orderToReturn);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testUpdateOrder() throws Exception {
		Order orderToPut = new Order("ALFKI", "Siparis Tamamlandi");
		Order orderToReturnSave = new Order(1, "ALFKI", "Siparis Tamamlandi");
		Mockito.doReturn(orderToReturnSave).when(sellingService).updateOrder(Mockito.anyInt(), Mockito.any());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/selling/order/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(orderToPut));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String expectedJson = asJsonString(orderToReturnSave);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}
	
}
