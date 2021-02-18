package com.example.northWindFinal.business.concretes;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.example.northWindFinal.dataAccess.concretes.CategoryRepository;
import com.example.northWindFinal.entities.concretes.Category;
import com.example.northWindFinal.exception.ApiErrorException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryManagerTest extends TestBase{

	@InjectMocks private  CategoryManager categoryManager;
	@MockBean private  CategoryRepository categoryRepository;

	public List<Category> categories;
	
	@BeforeEach
	void setUp() throws Exception {
		categories = Arrays.asList(
				new Category(1,"Category1","Description1"),
				new Category(2,"Category2","Description2"),
				new Category(3,"Category3","Description3")
			);
		categoryRepository.saveAll(categories);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		categoryRepository.deleteAll();
	}
	
	@Test
	void testGetById() throws Exception {
		Category category3 = new Category(3,"Category3","Description3");
		Mockito.when(categoryRepository.findById(3)).thenReturn(Optional.ofNullable(category3));
		String expectedJson = asJsonString(category3);
		String actualInJson = asJsonString(categoryManager.getById(3));
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testGetByIdThrowException() throws Exception {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> {categoryManager.getById(1);}
				);
		assertTrue(exception.getMessage().contains("No category with id:" + 1));
	}

	@Test
	void testGetAll() {
		List<Category> category = new ArrayList<Category>();
		Category category1 = new Category(1,"Category1","Description1");
		Category category2 = new Category(2,"Category2","Description2");
		Category category3 = new Category(3,"Category3","Description3");
		category.add(category1); 
		category.add(category2);
		category.add(category3);
		Mockito.when(categoryRepository.findAll()).thenReturn(category);
		assertEquals(3, categoryManager.getAll().size());
	}
	
	@Test
	void testGetAllException() {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> categoryManager.getAll());
		assertTrue(exception.getMessage().contains("Category cannot be found in the cache"));
	}

	@Test
	void testAdd() {
		Category category = new Category(1,"Category","Description");
		Mockito.when(categoryRepository.save(category)).thenReturn(category);
		assertEquals(category, categoryManager.add(category));
	}

	@Test
	void testUpdate() {
		Category category = new Category(4,"Category4","Description4");
		Mockito.when(categoryRepository.findById(4)).thenReturn(Optional.ofNullable(category));
		Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);
		Category categoryUpdate = new Category(4,"Category5","Description4");
		String expectedJson = asJsonString(categoryUpdate);
		String actualInJson = asJsonString(categoryManager.update(4, categoryUpdate));
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testUpdateThrowException() throws Exception {
		Category category = new Category("Category4","Description4");
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> categoryManager.update(1, category)
				);
		assertTrue(exception.getMessage().contains("No category with id:" + 1));
	}

	@Test
	void testDelete() {
		Category category = new Category(1,"Category","Description");
		Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
		Map<String, Boolean> response = new HashMap<>();
        response.put("The given category is deleted", Boolean.TRUE);
        String expected = asJsonString(response);
		String actual = asJsonString(categoryManager.delete(1));
		assertEquals(expected, actual);
	}
	
	@Test
	void testDeleteThrowException() throws Exception {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> categoryManager.delete(1)
				);
		assertTrue(exception.getMessage().contains("No category with id:" + 1));
	}

}
