package com.example.northWindFinal.entities.concretes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.northWindFinal.entities.abstracts.IEntity;

import lombok.Data;

@Data
@Entity
@Table(name="shopping_cards")
public class ShoppingCard implements IEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="card_id")
	private int cardId;
	@Column(name="customer_id")
	private String customerId;
	@Column(name="product_id")
	private int productId;
	@Column(name="quantity")
	private int quantity;
	@Column(name="unit_price")
	private double unitPrice;
	
	public ShoppingCard(int cardId, String customerId, int productId,  int quantity, double unitPrice) {
		this.cardId = cardId;
		this.customerId = customerId;
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}
	public ShoppingCard(String customerId, int productId,  int quantity, double unitPrice) {
		this.customerId = customerId;
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}

	public ShoppingCard() {}
	
}
