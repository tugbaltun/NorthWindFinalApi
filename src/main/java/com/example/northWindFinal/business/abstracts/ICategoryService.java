package com.example.northWindFinal.business.abstracts;

import com.example.northWindFinal.entities.concretes.Category;
import java.util.List;
import java.util.Map;

public interface ICategoryService {

  List<Category> getAll();

  Category getById(Integer id);

  Category add(Category category);

  Category update(Integer id, Category category);

  Map<String, Boolean> delete(Integer id);
}
