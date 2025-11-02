package com.tp.benchmarkspringmvcapi.dto;

import java.util.List;

/**
 * Classe DTO pour encapsuler les résultats paginés.
 * Même structure que la classe Page de Jersey pour comparabilité.
 */
public class PageResponse<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int page;
    private final int size;

    public PageResponse(List<T> content, long totalElements, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
        this.totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / (double) size);
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
