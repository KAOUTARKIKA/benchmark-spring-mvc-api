package com.tp.benchmarkspringmvcapi.service;

import com.tp.benchmarkspringmvcapi.entity.Category;
import com.tp.benchmarkspringmvcapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Category> list(int page, int size) {
        int firstResult = Math.max(0, page) * Math.max(1, size);
        int maxResults = Math.max(1, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Category> result = repository.findAll(pageable);
        return result.getContent();
    }

    @Transactional(readOnly = true)
    public Category get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Category create(Category category) {
        try {
            return repository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Category update(Category category) {
        try {
            return repository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean delete(Category category) {
        try {
            repository.delete(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}