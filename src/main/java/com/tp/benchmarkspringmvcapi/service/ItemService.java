package com.tp.benchmarkspringmvcapi.service;

import com.tp.benchmarkspringmvcapi.entity.Item;
import com.tp.benchmarkspringmvcapi.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;

    // Flag pour activer/désactiver le JOIN FETCH (anti-N+1)
    @Value("${benchmark.use-join-fetch:false}")
    private boolean useJoinFetch;

    @Autowired
    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Item> list(int page, int size) {
        int firstResult = Math.max(0, page) * Math.max(1, size);
        int maxResults = Math.max(1, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Item> result = repository.findAll(pageable);
        return result.getContent();
    }

    @Transactional(readOnly = true)
    public List<Item> listByCategory(Long categoryId, int page, int size) {
        int firstResult = Math.max(0, page) * Math.max(1, size);
        int maxResults = Math.max(1, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        Page<Item> result;
        if (useJoinFetch) {
            // Mode optimisé avec JOIN FETCH (anti-N+1)
            result = repository.findByCategoryIdWithJoinFetch(categoryId, pageable);
        } else {
            // Mode baseline sans JOIN FETCH (pour mesurer l'impact N+1)
            result = repository.findByCategoryId(categoryId, pageable);
        }

        return result.getContent();
    }

    @Transactional(readOnly = true)
    public Item get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Item create(Item item) {
        try {
            return repository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Item update(Item item) {
        try {
            return repository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean delete(Item item) {
        try {
            repository.delete(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}