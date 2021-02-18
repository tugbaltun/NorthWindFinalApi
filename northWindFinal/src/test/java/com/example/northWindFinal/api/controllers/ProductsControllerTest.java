package com.example.northWindFinal.api.controllers;

import static org.junit.jupiter.api.Assertions.*;
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
import com.example.northWindFinal.business.abstracts.IProductService;
import com.example.northWindFinal.entities.concretes.Product;
import com.example.northWindFinal.exception.ApiErrorException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@WebMvcTest(ProductsController.class)
public class ProductsControllerTest extends TestBase{
	
	@Autowired private MockMvc mockMvc;
	@InjectMocks private ProductsController productsController;
	@MockBean private IProductService productService;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(productsController)
				.build();
	}

	@Test
	void testGetById() throws Exception {
		Product product = new Product(1, "Name1", 1, "10 boxes", 10.0, 0);
		
		Mockito.doReturn(product).when(productService).getById(1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expectedJson = asJsonString(product);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
		
	}
	
	@Test
	void testGetByIdNotFound() throws Exception {
		Mockito.when(productService.getById(1)).thenThrow(new ApiErrorException("No product with id:" + 1));
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", 2))
        .andExpect(status().isNotFound()) ;
		
	}

	@Test
	void testGetAll() throws Exception {
		List<Product> product = new ArrayList<Product>();
		Product product1 = new Product(1, "Name1", 1, "10 boxes", 10.0, 0);
		Product product2 = new Product(2, "Name2", 2, "12 boxes", 15, 0);
		Product product3 = new Product(3, "Name3", 2, "2 boxes", 15, 0);
		product.add(product1); product.add(product2); product.add(product3);
		
		Mockito.doReturn(product).when(productService).getAll();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expectedJson = asJsonString(product);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testAdd() throws Exception {
		Product productToPost = new Product("Name1", 1, "10 boxes", 10.0, 0);
		Product productToReturn = new Product(1, "Name1", 1, "10 boxes", 10.0, 0);
		Map<String, Product> response = new HashMap<>();
        response.put("Product basariyla eklendi", productToReturn);
		Mockito.doReturn(response).when(productService).add(productToPost);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToPost));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		

        String expectedJson = asJsonString(response);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testUpdate() throws Exception {
		Product productToPut = new Product("NameUpdated", 1, "10 boxes", 10.0, 0);
		Product productToReturnSave = new Product("NameUpdated", 1, "10 boxes", 10.0, 0);
		String expectedJson = asJsonString(productToReturnSave);
		Mockito.doReturn(productToReturnSave).when(productService).update(Mockito.anyInt(), Mockito.any());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/products/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToPut));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testDelete() throws Exception {
		Map<String, Boolean> response = new HashMap<>();
        response.put("The given product is deleted", Boolean.TRUE);
        String expectedJson = asJsonString(response);
		Mockito.doReturn(response).when(productService).delete(Mockito.anyInt());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/products/{id}",1)
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
