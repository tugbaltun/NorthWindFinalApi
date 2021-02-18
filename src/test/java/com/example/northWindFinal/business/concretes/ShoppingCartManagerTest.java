package com.example.northWindFinal.business.concretes;

import static org.junit.jupiter.api.Assertions.*;
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
import com.example.northWindFinal.dataAccess.concretes.ShoppingCardRepository;
import com.example.northWindFinal.entities.concretes.ShoppingCard;
import com.example.northWindFinal.exception.ApiErrorException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShoppingCartManagerTest extends TestBase{

	@InjectMocks private ShoppingCardManager shoppingCartManager;
	@MockBean private ShoppingCardRepository shoppingCardRepository;
	public List<ShoppingCard> cards;
	public List<ShoppingCard> cardALFKI;
	@BeforeEach
	void setUp() throws Exception {
		cards = Arrays.asList(
				new ShoppingCard(1, "ALFKI", 1, 2, 10.5),
				new ShoppingCard(2, "ALFKI", 2, 3, 15),
				new ShoppingCard(3, "ALFKI", 2, 1, 15),
				new ShoppingCard(4, "ALFKI", 3, 5, 12),
				new ShoppingCard(5, "BOLDI", 10, 2, 10),
				new ShoppingCard(6, "BOLDI", 11, 3, 7),
				new ShoppingCard(7, "BOLDI", 12, 2, 15),
				new ShoppingCard(8, "BOLDI", 12, 1, 15)
			);
		shoppingCardRepository.saveAll(cards);
		
		cardALFKI = Arrays.asList(
				new ShoppingCard(1, "ALFKI", 1, 2, 10.5),
				new ShoppingCard(2, "ALFKI", 2, 3, 15),
				new ShoppingCard(3, "ALFKI", 2, 1, 15),
				new ShoppingCard(4, "ALFKI", 3, 5, 12)
			);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		shoppingCardRepository.deleteAll();
	}
	
	@Test
	void testGetByCustomer() {
		
		Mockito.when(shoppingCardRepository.findByCustomer("ALFKI")).thenReturn(cardALFKI);
		String expectedJson = asJsonString(cardALFKI.size());
		String actualInJson = asJsonString(shoppingCartManager.getByCustomer("ALFKI").size());
		
		assertEquals(expectedJson, actualInJson);
	}
	
	@Test
	void testGetByCustomerException() {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> shoppingCartManager.getByCustomer("ALF")
		);
		assertTrue(exception.getMessage().contains("Card belongs to customer cannot be found in the cache"));
	}

	@Test
	void testAddProductToCard() {

		ShoppingCard shoppingCard = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
		ShoppingCard shoppingCardToPost = new ShoppingCard("ALFKI", 1, 2, 10.5);
		ShoppingCard shoppingCardToReturn = new ShoppingCard(1, "ALFKI", 1, 3, 10.5);
		
		for(int i=0; i<2; i++) {
			if(i==0) {
				shoppingCardToReturn = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
				Mockito.when(shoppingCardRepository.findByProduct("ALFKI", 1)).thenReturn(null);
				Mockito.when(shoppingCardRepository.save(shoppingCardToPost)).thenReturn(shoppingCard);
			}
			else{
				shoppingCardToReturn = new ShoppingCard(1, "ALFKI", 1, 3, 10.5);
				Mockito.when(shoppingCardRepository.findByProduct("ALFKI", 1)).thenReturn(shoppingCard);
				Mockito.when(shoppingCardRepository.save(shoppingCardToPost)).thenReturn(shoppingCard);
			}
			Map<String, ShoppingCard> response = new HashMap<>();
			response.put("Ürün sepete eklendi!", shoppingCardToReturn);
			String expectedJson = asJsonString(response);
			String actualInJson = asJsonString(shoppingCartManager.addProductToCard(shoppingCard));
			assertEquals(expectedJson, actualInJson);
		}	
	}
	
	@Test
	void testAddProductToCardThrowException() {
		ShoppingCard shoppingCard = new ShoppingCard(1, "ALFKI", 2, 2, 10.5);
		Mockito.when(shoppingCardRepository.findByProduct("ALFKI", 2)).thenThrow(new ApiErrorException("No cart with id:"+2));
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> shoppingCartManager.addProductToCard(shoppingCard)
				);
		assertTrue(exception.getMessage().contains("No cart with id:"+2));
	}

	@Test
	void testReduceTheQuantity() {
		
		ShoppingCard cardToPut = new ShoppingCard("ALFKI", 1, 3, 10.5);
		ShoppingCard cardToReturnFindById = new ShoppingCard(1, "ALFKI", 1, 3, 10.5);
		ShoppingCard cardToReturnSave ;
		
		for(int i=0; i<2; i++) {
			if(i==0) {
				cardToReturnSave = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
				Mockito.when(shoppingCardRepository.findById(1)).thenReturn(Optional.ofNullable(cardToReturnFindById));
				Mockito.when(shoppingCardRepository.save(cardToReturnFindById)).thenReturn(cardToReturnFindById);
				String expectedJson = asJsonString(cardToReturnSave);
				String actualInJson = asJsonString(shoppingCartManager.reduceTheQuantity(1, 1, cardToPut));
				assertEquals(expectedJson, actualInJson);
			}
			else{
				Mockito.when(shoppingCardRepository.findById(1)).thenReturn(Optional.ofNullable(cardToReturnFindById));
				Mockito.when(shoppingCardRepository.save(cardToReturnFindById)).thenReturn(cardToReturnFindById);
				Mockito.doThrow(new ApiErrorException("Card belongs to customer removed from the cache")).
						when(shoppingCardRepository).delete(cardToReturnFindById);
				ApiErrorException exception = assertThrows(ApiErrorException.class, 
						()-> shoppingCartManager.reduceTheQuantity(1, 2, cardToPut)
						);
				assertTrue(exception.getMessage().contains("Card belongs to customer removed from the cache"));
			}
		}
	}
	
	@Test
	void testReduceTheQuantityThrowException() throws Exception {
		ShoppingCard shoppingCard = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()-> shoppingCartManager.reduceTheQuantity(1, 2, shoppingCard)
				);
		assertTrue(exception.getMessage().contains("No card with id:"+1));
	}

	@Test
	void testDeleteByProduct() {
		
		ShoppingCard card = new ShoppingCard(1, "ALFKI", 1, 2, 10.5);
		Mockito.when(shoppingCardRepository.findById(1)).thenReturn(Optional.ofNullable(card));
		
		Map<String, Boolean> response = new HashMap<>();
        response.put("The given product is deleted from card", Boolean.TRUE);
        String expected = asJsonString(response);
		String actual = asJsonString(shoppingCartManager.deleteByProduct(1));
		assertEquals(expected, actual);
	}
	
	@Test
	void testDeleteByProductThrowException() throws Exception {
		ApiErrorException exception = assertThrows(ApiErrorException.class, 
				()->shoppingCartManager.deleteByProduct(1)
				);
		assertTrue(exception.getMessage().contains("No product with id in card:"+1));
	}

	@Test
	void testDeleteCard() {
		Mockito.when(shoppingCardRepository.findByCustomer("ALFKI")).thenReturn(cardALFKI);
		Map<String, Boolean> response = new HashMap<>();
        response.put("The given card is deleted from table", Boolean.TRUE);
        String expected = asJsonString(response);
		String actual = asJsonString(shoppingCartManager.deleteCard("ALFKI"));
		assertEquals(expected, actual);
	}
	
	@Test
	void testDeleteCardExceptionThrow() {
		ApiErrorException  exception = assertThrows(ApiErrorException.class, 
				()-> shoppingCartManager.deleteCard("ALFKI")
				);
		assertTrue(exception.getMessage().contains("Card belongs to customer cannot be found in the cache"));
	}


}
