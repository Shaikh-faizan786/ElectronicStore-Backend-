package com.electronics.store.service.impl;
import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.dtos.ProductDto;
import com.electronics.store.entities.Category;
import com.electronics.store.entities.Product;
import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.helper.Helper;
import com.electronics.store.repositories.CategoryRepository;
import com.electronics.store.repositories.ProductRepository;
import com.electronics.store.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.image}")
    private String imageUploadPath ;

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcseNotFoundException("Product not found with ID: " + productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.isStock());
        product.setLive(productDto.isLive());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setProductImageName(productDto.getProductImageName());
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcseNotFoundException("Product not found with ID: " + productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        return Helper.getPageableResponse(productPage, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> liveProducts = productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(liveProducts, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String query, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> searchResults = productRepository.findByTitleContaining(query, pageable);
        return Helper.getPageableResponse(searchResults, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcseNotFoundException("Product not found with ID: " + productId));

        String fullPath = imageUploadPath + product.getProductImageName();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("User Image not Found in folder");
            e.printStackTrace();
        }catch(IOException e ) {
            e.printStackTrace();
        }

        // pehle image ka path lunga
        productRepository.delete(product);
    }

    // create product with category Id or iska controller Category ke class me bani hai
    @Override
    public ProductDto createWithCategory(String categoryId,ProductDto productDto) {
        // pehle Category fetch karna hoga ki ye id ka koi category hai ya nhi

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcseNotFoundException("Category is not Found With the Given Id"));
        Product product = modelMapper.map(productDto, Product.class);
        // product id
        String product1 = UUID.randomUUID().toString();
        product.setProductId(product1);
        // added
        product.setAddedDate(new Date());
        // set karna hoga category ko
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);


    }

    @Override
    public ProductDto updatecategory(String produtId, String categoryId) {
        // product bhi fetch karna padega nad category bhi fetch karna padega
        Product product = productRepository.findById(produtId).orElseThrow(() -> new ResourcseNotFoundException("product is not Found With the Given Id"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcseNotFoundException("Category is not Found With the Given Id"));

        product.setCategory(category);
        Product savedproduct = productRepository.save(product);


       return modelMapper.map(savedproduct,ProductDto.class);
    }

    //    category ke sare product ko get karenge
    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcseNotFoundException("Category is not Found With the Given Id"));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page =  productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);

    }
}
