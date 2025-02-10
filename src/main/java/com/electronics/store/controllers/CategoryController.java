package com.electronics.store.controllers;


import com.electronics.store.dtos.*;
import com.electronics.store.service.CategoryService;
import com.electronics.store.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    // create category
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    // update category
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('NORMAL')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId,  @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedUserDto = categoryService.update(categoryId, categoryDto);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    // delete category
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.delete(categoryId);
        ApiResponseMessage mess = new ApiResponseMessage();
        mess.setMessage("Used Deleted is Successfully");
        mess.setSuccess(true);
        mess.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(mess, HttpStatus.OK);
    }

    // get all category
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    public ResponseEntity <PageableResponse<CategoryDto>> getAllCategory(
           @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<CategoryDto> category = categoryService.getAll(pageSize, pageNumber, sortBy, sortDir);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    // get single category
    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    public ResponseEntity<CategoryDto> getcategory(@PathVariable  String categoryId) {
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);     
    }

    // create product with category Id
    @PostMapping("/{categoryId}/products")
    public ResponseEntity <ProductDto> createProductWithcategory(
            @RequestBody ProductDto productDto,
            @PathVariable  String categoryId
    ){
        ProductDto productWithCategory = productService.createWithCategory(categoryId, productDto);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    // update karenge products ko or uska id leke ek category ke id me dalnege

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity <ProductDto> updateCategoryOfProducts(
            @PathVariable String productId,
            @PathVariable String categoryId
    ){
        ProductDto updatecategory = productService.updatecategory(productId, categoryId);
        return new ResponseEntity<>(updatecategory,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public  ResponseEntity<PageableResponse <ProductDto>> getAllOfCategory(
        @PathVariable String categoryId ,
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ){
        PageableResponse<ProductDto> allOfCategory = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allOfCategory,HttpStatus.OK);
    }

}
