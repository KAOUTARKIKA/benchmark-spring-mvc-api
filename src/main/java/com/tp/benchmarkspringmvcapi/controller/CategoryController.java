package com.tp.benchmarkspringmvcapi.controller;

import com.tp.benchmarkspringmvcapi.entity.Category;
import com.tp.benchmarkspringmvcapi.entity.Item;
import com.tp.benchmarkspringmvcapi.service.CategoryService;
import com.tp.benchmarkspringmvcapi.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ItemService itemService;

    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Category> result = categoryService.list(page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> get(@PathVariable Long id) {
        Category category = categoryService.get(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) {
        Category created = categoryService.create(category);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.created(URI.create("/categories/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(
            @PathVariable Long id,
            @Valid @RequestBody Category payload) {
        Category existing = categoryService.get(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        payload.setId(id);
        Category updated = categoryService.update(payload);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Category existing = categoryService.get(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        boolean deleted = categoryService.delete(existing);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint relationnel : GET /categories/{id}/items?page=&size=
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<List<Item>> listItems(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Item> result = itemService.listByCategory(id, page, size);
        return ResponseEntity.ok(result);
    }
}