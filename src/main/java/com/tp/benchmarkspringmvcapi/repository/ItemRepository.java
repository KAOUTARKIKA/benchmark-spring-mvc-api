package com.tp.benchmarkspringmvcapi.repository;

import com.tp.benchmarkspringmvcapi.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Mode baseline sans JOIN FETCH (N+1 possible)
    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId ORDER BY i.id")
    Page<Item> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // Mode optimisé avec JOIN FETCH (anti-N+1)
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId ORDER BY i.id")
    Page<Item> findByCategoryIdWithJoinFetch(@Param("categoryId") Long categoryId, Pageable pageable);
}