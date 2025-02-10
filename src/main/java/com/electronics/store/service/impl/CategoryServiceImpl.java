package com.electronics.store.service.impl;

import com.electronics.store.dtos.CategoryDto;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.entities.Category;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.helper.Helper;
import com.electronics.store.repositories.CategoryRepository;
import com.electronics.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        // create random Id
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = mapper.map(categoryDto, Category.class);
        Category savedcategory = categoryRepository.save(category);
        return mapper.map(savedcategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(String categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcseNotFoundException("Category Not Found With The Given Id !!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcseNotFoundException("Category Not Found With The Given Id !!"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response = Helper.getPageableResponse(categoryPage, CategoryDto.class);
        return response;
    }


    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcseNotFoundException("Category Not Found With The Given Id !!"));
        return mapper.map(category,CategoryDto.class);
    }
}
