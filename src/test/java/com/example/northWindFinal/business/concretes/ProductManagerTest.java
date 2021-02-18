package com.example.northWindFinal.business.concretes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.northWindFinal.TestBase;
import com.example.northWindFinal.dataAccess.concretes.ProductRepository;
import com.example.northWindFinal.entities.concretes.Product;
import com.example.northWindFinal.exception.ApiErrorException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductManagerTest extends TestBase{

	@InjectMocks private ProductManager productManager;
	@MockBean private ProductRepository productRepository;
	
	public List<Product> products;

	@BeforeEach
	void setUp() throws Exception {
		products = Arrays.asList(
				new Product(1, "Name1", 1, "10 boxes", 10.0, 0),
				new Product(2, "Name2", 2, "12 boxes", 15, 0),
				new Product(3, "Name3", 2, "2 boxes", 15, 0)
				);
		productRepository.saveAll(products);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		productRepository.deleteAll();
	}

	@Test
	void testGetById() {
		Product product = new Product(3, "Name3", 2, "2 boxes", 15, 0);
		Mockito.when(productRepository.findById(3)).thenReturn(Optional.ofNullable(product));
		
		String expectedJson = asJsonString(product);
		String actualInJson = asJsonString(productManager.getById(3));
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testGetByIdThrwException() {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> productManager.getById(1)
				);
		assertTrue(exception.getMessage().contains("No product with id:" + 1));
	}


	@Test
	void testGetAll() {
		
		List<Product> product = new ArrayList<Product>();
		Product product1 = new Product(1, "Name1", 1, "10 boxes", 10.0, 0);
		Product product2 = new Product(2, "Name2", 2, "12 boxes", 15, 0);
		Product product3 = new Product(3, "Name3", 2, "2 boxes", 15, 0);
		
		product.add(product1);
		product.add(product2);
		product.add(product3);
		
		Mockito.when(productRepository.findAll()).thenReturn(product);
		assertEquals(3, productManager.getAll().size());		
	}
	
	@Test
	void testGetAllThrowException() {
		
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> productManager.getAll()
				);
		assertTrue(exception.getMessage().contains("Product cannot be found in the cache"));
	}
	
	@Test
	void testAdd() {
		
		Product product = new Product(4, "Name4", 3, "5 boxes", 20, 0);
		Mockito.when(productRepository.findAll()).thenReturn(products);
		Mockito.when(productRepository.save(product)).thenReturn(product);
		
		Map<String, Product> response = new HashMap<>();
		response.put("Product basariyla eklendi", product);
		
		String expectedJson = asJsonString(response);
		String actualInJson = asJsonString(productManager.add(product));
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testAddThrowException() {
		Product productToPost = new Product("N", 3, "5 boxes", 20, 0);
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> productManager.add(productToPost));
		assertTrue(exception.getMessage().contains("ProductName en az 2 karakterden oluşmalı ve Kategori tablosuna 10'dan fazla aynı tür ürün eklenemez!"));
	}

	@Test
	void testUpdate() {
		Product product = new Product(4, "Name5", 5, "5 boxes", 20, 0);
		Product productToFindBy = new Product(4, "Name4", 3, "5 boxes", 20, 0);
		Product productToUpdate = new Product(4, "Name5", 5, "5 boxes", 20, 0);
		Mockito.when(productRepository.findById(4)).thenReturn(Optional.ofNullable(productToFindBy));
		Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productToUpdate);
		
		String expectedJson = asJsonString(productToUpdate);
		String actualInJson = asJsonString(productManager.update(4, product));
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testUpdateThrowException() {
		Product product = new Product(4, "Name4", 3, "5 boxes", 20, 0);
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> productManager.update(4, product)
				);
		assertTrue(exception.getMessage().contains("No product with id:" + 4));
	}

	@Test
	void testDelete() {
		Product product = new Product(1, "Name1", 1, "10 boxes", 10.0, 0);
		Mockito.when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
		Map<String, Boolean> response = new HashMap<>();
        response.put("The given product is deleted", Boolean.TRUE);
        String expected = asJsonString(response);
		String actual = asJsonString(productManager.delete(1));
		assertEquals(expected, actual);
	}
	
	@Test
	void testDeleteThrowException() {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> productManager.delete(1)
				);
		assertTrue(exception.getMessage().contains("No product with id:" + 1));
	}

}
