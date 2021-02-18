package com.example.northWindFinal.business.concretes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.northWindFinal.business.abstracts.IShoppingCardService;
import com.example.northWindFinal.dataAccess.concretes.ShoppingCardRepository;
import com.example.northWindFinal.entities.concretes.ShoppingCard;
import com.example.northWindFinal.exception.ApiErrorException;

@Service
public class ShoppingCardManager implements IShoppingCardService{
	
	@Autowired private ShoppingCardRepository shoppingCardRepository;

	@Override
	public List<ShoppingCard> getByCustomer(String customerId) {
		if(shoppingCardRepository.findByCustomer(customerId).isEmpty()) {
			throw new ApiErrorException("Card belongs to customer cannot be found in the cache");
		}
		return shoppingCardRepository.findByCustomer(customerId);
	}

	@Override
	public Map<String, ShoppingCard> addProductToCard(ShoppingCard shoppingCard) {
		ShoppingCard selectedCard ;
		selectedCard = shoppingCardRepository.findByProduct(shoppingCard.getCustomerId(), shoppingCard.getProductId());
		String message = "Ürün sepete eklendi!";
		Map<String, ShoppingCard> response = new HashMap<>();
		if(selectedCard == null) {
			shoppingCardRepository.save(shoppingCard);
			response.put(message, shoppingCard);
		}
		else {
			selectedCard.setQuantity(shoppingCard.getQuantity()+1);
			shoppingCardRepository.save(selectedCard);
			response.put(message, selectedCard);
		}
		return response;
	}

	@Override
	public ShoppingCard reduceTheQuantity(Integer cardId, Integer quantity, ShoppingCard shoppingCard) {
		ShoppingCard cardToUpdate = shoppingCardRepository.
				findById(cardId)
				.orElseThrow(()-> new ApiErrorException("No card with id:"+cardId));
		if(cardToUpdate.getQuantity()-quantity == 0) {
			shoppingCardRepository.delete(cardToUpdate);
			throw new ApiErrorException("Card belongs to customer removed from the cache");
		}
		else {
			cardToUpdate.setQuantity(cardToUpdate.getQuantity()-quantity);
			return shoppingCardRepository.save(cardToUpdate);
		}
	}

	@Override
	public Map<String, Boolean> deleteByProduct(Integer cardId) {
		return shoppingCardRepository
				.findById(cardId)
				.map(
					card->{
							shoppingCardRepository.delete(card);
							Map<String, Boolean> response = new HashMap<>();
							response.put("The given product is deleted from card", Boolean.TRUE);
							return response;
					})
				.orElseThrow(()-> new ApiErrorException("No product with id in card:"+cardId));
	}

	@Override
	public Map<String, Boolean> deleteCard(String customerId) {
		if(shoppingCardRepository.findByCustomer(customerId).isEmpty()) {
			throw new ApiErrorException("Card belongs to customer cannot be found in the cache");
		}
		
		else {
			shoppingCardRepository.deleteAll(shoppingCardRepository.findByCustomer(customerId));
			Map<String, Boolean> response = new HashMap<>();
			response.put("The given card is deleted from table", Boolean.TRUE);
			return response;
		}
	}

}
