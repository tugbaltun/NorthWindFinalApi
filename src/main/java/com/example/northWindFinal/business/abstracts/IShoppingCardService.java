package com.example.northWindFinal.business.abstracts;
import java.util.List;
import java.util.Map;
import com.example.northWindFinal.entities.concretes.ShoppingCard;

public interface IShoppingCardService {

	List<ShoppingCard> getByCustomer(String customerId); // Müşteriye ait sepeti listele
	Map<String, ShoppingCard> addProductToCard(ShoppingCard shoppingCard); //Sepete ürün ekle
	ShoppingCard reduceTheQuantity(Integer cardId, Integer quantity, ShoppingCard shoppingCard); //Ürünün miktarını azalt
	Map<String,Boolean> deleteByProduct(Integer cardId); //Ürünü sepetten sil
	Map<String, Boolean> deleteCard(String customerId); //Sepeti tablodan sil

}
