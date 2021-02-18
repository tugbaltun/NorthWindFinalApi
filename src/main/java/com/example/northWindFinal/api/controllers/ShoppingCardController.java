package com.example.northWindFinal.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import java.util.List;
import java.util.Map;
import com.example.northWindFinal.business.abstracts.IShoppingCardService;
import com.example.northWindFinal.entities.concretes.ShoppingCard;

@RestController
@RequestMapping("/api/v1")
public class ShoppingCardController {

	IShoppingCardService shoppingCardService;

	@Autowired
	public void setShoppingCardService(IShoppingCardService shoppingCardService) {
		this.shoppingCardService = shoppingCardService;
	}
	
	@GetMapping("/shoppingCard/{id}")
	public ResponseEntity<List<ShoppingCard>> getCardByCustomer(@PathVariable(value="id") String customerId){
		return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(shoppingCardService.getByCustomer(customerId));
	}
	
	@PostMapping("/shoppingCard")
	public ResponseEntity<Map<String, ShoppingCard>> add(@RequestBody ShoppingCard shoppingCard) {
		return created(fromCurrentRequest().build().toUri()).header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.body(shoppingCardService.addProductToCard(shoppingCard));
	}

	@PutMapping("/shoppingCard/{id}/{quantity}")
	public ResponseEntity<ShoppingCard> reduceTheQuantity(@PathVariable(value = "id") Integer cardId,
			@PathVariable(value = "quantity") Integer quantity, ShoppingCard shoppingCard) {
		return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.body(shoppingCardService.reduceTheQuantity(cardId, quantity, shoppingCard));
	}

	@DeleteMapping("shoppingCard/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteByProduct(@PathVariable(name = "id") Integer cardId) {
		return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(shoppingCardService.deleteByProduct(cardId));
	}

}
