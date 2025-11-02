package com.tp.benchmarkspringmvcapi.service;

import com.tp.benchmarkspringmvcapi.dto.PageResponse;
import com.tp.benchmarkspringmvcapi.entity.Category;
import com.tp.benchmarkspringmvcapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PageResponse<Category> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> result = repository.findAll(pageable);
        return new PageResponse<>(
                result.getContent(),
                result.getTotalElements(),
                page,
                size
        );
    }

    @Transactional(readOnly = true)
    public Optional<Category> get(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Category create(Category category) {
        return repository.save(category);
    }

    @Transactional
    public Optional<Category> update(Long id, Category payload) {
        Optional<Category> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Category existing = existingOpt.get();
        existing.setCode(payload.getCode());
        existing.setName(payload.getName());

        Category updated = repository.save(existing);
        return Optional.of(updated);
    }

    @Transactional
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}