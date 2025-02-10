package com.electronics.store.service;

import com.electronics.store.dtos.CategoryDto;
import com.electronics.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(String categoryId,CategoryDto categoryDto);

    void delete(String categoryId);

     PageableResponse<CategoryDto> getAll(int pageSize, int pageNumber, String sortBy, String sortDir);

    CategoryDto get(String categoryId);
}
