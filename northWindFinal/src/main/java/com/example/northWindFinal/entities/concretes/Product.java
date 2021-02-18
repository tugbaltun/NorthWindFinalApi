package com.example.northWindFinal.entities.concretes;

import com.example.northWindFinal.entities.abstracts.IEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product implements IEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private int productId;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "category_id")
  private int categoryId;

  @Column(name = "quantity_per_unit")
  private String quantityPerUnit;

  @Column(name = "unit_price")
  private double unitPrice;

  @Column(name = "discontinued")
  private int discountinued;

  public Product(int productId, String productName, int categoryId, String quantityPerUnit, double unitPrice, int discountinued) {
    this.productId = productId;
    this.productName = productName;
    this.categoryId = categoryId;
    this.quantityPerUnit = quantityPerUnit;
    this.unitPrice = unitPrice;
    this.discountinued = discountinued;
  }
  
  public Product(String productName, int categoryId, String quantityPerUnit, double unitPrice, int discountinued) {
	    this.productName = productName;
	    this.categoryId = categoryId;
	    this.quantityPerUnit = quantityPerUnit;
	    this.unitPrice = unitPrice;
	    this.discountinued = discountinued;
	  }

  public Product() {}
}
