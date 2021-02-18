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
@Table(name="order_details")
public class OrderDetail implements IEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="order_id")
	private int orderId;
	@Column(name="product_id")
	private int productId;
	@Column(name="unit_price")
	private double unitPrice;
	@Column(name="quantity")
	private int quantity;
	@Column(name="discount")
	private double diccount;
	
	public OrderDetail(int id, int orderId, int productId, double unitPrice, int quantity) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}

	public OrderDetail() {}


}

