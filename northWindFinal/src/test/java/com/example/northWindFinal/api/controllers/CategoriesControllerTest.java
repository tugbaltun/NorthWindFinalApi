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
import com.example.northWindFinal.business.abstracts.ICategoryService;
import com.example.northWindFinal.entities.concretes.Category;
import com.example.northWindFinal.exception.ApiErrorException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@WebMvcTest(CategoriesController.class)
public class CategoriesControllerTest extends TestBase {

	@Autowired private MockMvc mockMvc;
	@InjectMocks private CategoriesController categoriesController;
	@MockBean private ICategoryService categoryService;
	
	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(categoriesController)
				.build();
	}

	@Test
	void testGetById() throws Exception {
		Category category = new Category(1,"Category","Description");
		
		Mockito.doReturn(category).when(categoryService).getById(Mockito.anyInt());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/categories/{id}",1)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expectedJson = asJsonString(category);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testGetByIdNotFound() throws Exception {
		Mockito.when(categoryService.getById(1)).thenThrow(new ApiErrorException("No category with id:" + 1));
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", 2))
        .andExpect(status().isNotFound()) ;
		
	}

	@Test
	void testGetAll() throws Exception {
		List<Category> category = new ArrayList<Category>();
		Category category1 = new Category(1,"Category1","Description1");
		Category category2 = new Category(2,"Category2","Description2");
		Category category3 = new Category(3,"Category3","Description3");
		category.add(category1); category.add(category2); category.add(category3);
		
		Mockito.doReturn(category).when(categoryService).getAll();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/categories")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		String expectedJson = asJsonString(category);
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testAdd() throws Exception {
		Category categoryToPost = new Category("Category","Description");
		Category categoryToReturn = new Category(1,"Category","Description");
		String expectedJson = asJsonString(categoryToReturn);
		Mockito.doReturn(categoryToReturn).when(categoryService).add(Mockito.any());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/categories")
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(categoryToPost));
		
		MvcResult result = mockMvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String actualInJson = result.getResponse().getContentAsString();
		assertEquals(expectedJson, actualInJson);
	}

	@Test
	void testUpdate() throws Exception {
		Category categoryToPut = new Category("CategoryUpdated","Description");
		Category categoryToReturnSave = new Category(1,"CategoryUpdated","Description");
		String expectedJson = asJsonString(categoryToReturnSave);
		Mockito.doReturn(categoryToReturnSave).when(categoryService).update(Mockito.anyInt(), Mockito.any());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/categories/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(categoryToPut));
		
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
        response.put("The given category is deleted", Boolean.TRUE);
        String expectedJson = asJsonString(response);
		Mockito.doReturn(response).when(categoryService).delete(Mockito.anyInt());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/categories/{id}",1)
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
