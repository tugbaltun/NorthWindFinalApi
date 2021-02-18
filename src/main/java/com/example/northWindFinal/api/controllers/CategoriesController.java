package com.example.northWindFinal.api.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import com.example.northWindFinal.business.abstracts.ICategoryService;
import com.example.northWindFinal.entities.concretes.Category;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

@RestController
@RequestMapping("/api/v1")
public class CategoriesController {
  ICategoryService categoryService;

  @Autowired
  public void setCategoryService(ICategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/categories/{id}")
  public ResponseEntity<Category> getById(@PathVariable(value = "id") Integer id) {
    return Optional.ofNullable(categoryService.getById(id))
        .map(category -> ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(category))
        .orElse(notFound().build());
  }

  @GetMapping("/categories")
  public ResponseEntity<List<Category>> getAll() {
    return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(categoryService.getAll());
  }

  @PostMapping("/categories")
  public ResponseEntity<Category> add(@RequestBody Category category) {
    return created(fromCurrentRequest().build().toUri())
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(categoryService.add(category));
  }

  @PutMapping("/categories/{id}")
  public ResponseEntity<Category> update(
      @PathVariable(value = "id") Integer id, @RequestBody Category category) {
    return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(categoryService.update(id, category));
  }

  @DeleteMapping("/categories/{id}")
  public ResponseEntity<Map<String, Boolean>> delete(@PathVariable(value = "id") Integer id) {
  
    return ok().body(categoryService.delete(id));
  }
}
