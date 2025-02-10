package com.electronics.store.service;

import com.electronics.store.dtos.CategoryDto;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.dtos.ProductDto;

public interface ProductService {

    // create
    ProductDto create (ProductDto productDto);

    // update
    ProductDto update (ProductDto productDto,String productId);

    // delete
    void  delete(String productId);

    // get single product
    ProductDto get(String productId);

    // get all product
   PageableResponse <ProductDto> getAll (int pageNumber,int pageSize,String sortBy,String sortDir);

   // get all live product
   PageableResponse <ProductDto> getAllLive (int pageNumber,int pageSize,String sortBy,String sortDir);

   // get all product by searching keywords
   PageableResponse <ProductDto> searchByTitle (String  subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);

   // create Product With category

    ProductDto createWithCategory(String categoryId,ProductDto productDto);

    // existing product ke andar category add karenge
    ProductDto updatecategory(String produtId,String categoryId);

    // category ke sare product ko get karenge
    PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);



}
