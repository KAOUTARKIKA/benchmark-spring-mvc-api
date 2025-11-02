package com.tp.benchmarkspringmvcapi.service;

import com.tp.benchmarkspringmvcapi.dto.PageResponse;
import com.tp.benchmarkspringmvcapi.entity.Category;
import com.tp.benchmarkspringmvcapi.entity.Item;
import com.tp.benchmarkspringmvcapi.repository.CategoryRepository;
import com.tp.benchmarkspringmvcapi.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository repository;
    private final CategoryRepository categoryRepository;

    // Flag pour activer/désactiver le JOIN FETCH (anti-N+1)
    @Value("${benchmark.use-join-fetch:true}")
    private boolean useJoinFetch;

    @Autowired
    public ItemService(ItemRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<Item> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> result = repository.findAll(pageable);
        return new PageResponse<>(
                result.getContent(),
                result.getTotalElements(),
                page,
                size
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<Item> listByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Item> result;
        if (useJoinFetch) {
            // Mode optimisé avec JOIN FETCH (anti-N+1)
            result = repository.findByCategoryIdWithJoinFetch(categoryId, pageable);
        } else {
            // Mode baseline sans JOIN FETCH (pour mesurer l'impact N+1)
            result = repository.findByCategoryId(categoryId, pageable);
        }

        return new PageResponse<>(
                result.getContent(),
                result.getTotalElements(),
                page,
                size
        );
    }

    @Transactional(readOnly = true)
    public Optional<Item> get(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Item create(Item item) {
        // Résoudre la catégorie si fournie via categoryId
        Long categoryId = item.getCategoryId();
        if (categoryId != null && item.getCategory() == null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
            item.setCategory(category);
        }

        return repository.save(item);
    }

    @Transactional
    public Optional<Item> update(Long id, Item payload) {
        Optional<Item> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Item existing = existingOpt.get();
        existing.setSku(payload.getSku());
        existing.setName(payload.getName());
        existing.setPrice(payload.getPrice());
        existing.setStock(payload.getStock());

        // Mettre à jour la catégorie si fournie
        Long categoryId = payload.getCategoryId();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
            existing.setCategory(category);
        }

        Item updated = repository.save(existing);
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