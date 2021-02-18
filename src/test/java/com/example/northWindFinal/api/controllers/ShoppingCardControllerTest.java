package com.example.northWindFinal.api.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import com.example.northWindFinal.business.abstracts.ICategoryService;
import com.example.northWindFinal.business.abstracts.IShoppingCardService;
import com.example.northWindFinal.entities.concretes.Category;
import com.example.northWindFinal.entities.concretes.Order;
import com.example.northWindFinal.entities.concretes.OrderDetail;
import com.example.northWindFinal.entities.concretes.ShoppingCard;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCardController.class)
public class ShoppingCardControllerTest extends TestBase {
	
	@Autowired private MockMvc mockMvc;
	@InjectMocks private ShoppingCardController shoppingCardController;
	@MockBean private IShoppingCardService shoppingCardService;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(shoppingCardController)
				.build();
		
		ShoppingCard shoppingCard1ToPost = new ShoppingCard("ALFKI", 1, 2, 10.5);
		ShoppingCard shoppingCard2ToPost = new ShoppingCard("ALFKI", 3, 3, 10.5);
		ShoppingCard shoppingCard3ToPost = new ShoppingCard("ALFKI", 3, 2, 10.5);
		shoppingCardService.addProductToCard(shoppingCard1ToPost);
		shoppingCardService.addProductToCard(shoppingCard2ToPost);
		shoppingCardService.addProductToCard(shoppingCard3ToPost);
	}

	@Test
	void testGetCartByCustomer() throws Exception {
		
		ShoppingCard shoppingCard1 = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
		ShoppingCard shoppingCard2 = new ShoppingCard(2, "ALFKI", 3, 3, 10.5);
		ShoppingCard shoppingCard3 = new ShoppingCard(3, "ALFKI", 3, 2, 10.5);
		
		List<ShoppingCard> cards = new ArrayList<ShoppingCard>();
		cards.add(shoppingCard1); 
		cards.add(shoppingCard2);
		cards.add(shoppingCard3);

		Mockito.doReturn(cards).when(shoppingCardService).getByCustomer("ALFKI");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/shoppingCard/{id}", "ALFKI")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expectedJson = asJsonString(cards);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testAdd() throws Exception {
		ShoppingCard shoppingCard3 = new ShoppingCard(3, "ALFKI", 3, 2, 10.5);
		ShoppingCard shoppingCard3ToPost = new ShoppingCard(3, "ALFKI", 3, 2, 10.5);
		Map<String, ShoppingCard> response = new HashMap<>();
		response.put("Eklendi!", shoppingCard3);
		Mockito.doReturn(response).when(shoppingCardService).addProductToCard(shoppingCard3ToPost);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/shoppingCard")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(shoppingCard3ToPost));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expected = asJsonString(response);
		String actual = result.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}

	@Test
	void testReduceTheQuantity() throws Exception {
		ShoppingCard cardToPut = new ShoppingCard("ALFKI", 1, 5, 10.5);
		ShoppingCard cardToReturnSave = new ShoppingCard(1, "ALFKI", 1, 4, 10.5);
		Mockito.doReturn(cardToReturnSave).when(shoppingCardService).reduceTheQuantity(Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/shoppingCard/{id}/{quantity}", 1, 1)
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cardToPut));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String expectedJson = asJsonString(cardToReturnSave);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testDeleteByProduct() throws Exception {
		Map<String, Boolean> response = new HashMap<>();
        response.put("The given category is deleted", Boolean.TRUE);
        String expectedJson = asJsonString(response);
		Mockito.doReturn(response).when(shoppingCardService).deleteByProduct(Mockito.anyInt());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/shoppingCard/{id}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(response));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

}
