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
@Table(name="categories")
public class Category implements IEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="category_id")
	private int id;
	@Column(name="category_name")
	private String name;
	@Column(name="description")
	private String description;
	
	public Category(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public Category(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Category() {}
	
}
