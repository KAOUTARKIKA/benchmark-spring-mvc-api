package com.tp.benchmarkspringmvcapi.repository;

import com.tp.benchmarkspringmvcapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}