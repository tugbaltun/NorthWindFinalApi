package com.example.northWindFinal.business.abstracts;

import com.example.northWindFinal.entities.concretes.Product;
import java.util.List;
import java.util.Map;

public interface IProductService {

  Product getById(Integer productId);
  
  List<Product> getAll();

  Map<String, Product> add(Product product);

  Product update(Integer productId, Product product);

  Map<String, Boolean> delete(Integer productId);
}
