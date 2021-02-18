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
@Table(name="orders")
public class Order implements IEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private int orderId;
	@Column(name="customer_id")
	private String customerId;
	@Column(name="order_status")
	private String orderStatus;
	
	public Order(int orderId, String customerId, String orderStatus) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.orderStatus = orderStatus;
	}
	
	public Order(String customerId, String orderStatus) {
		this.customerId = customerId;
		this.orderStatus = orderStatus;
	}

	public Order() {}
}
	

