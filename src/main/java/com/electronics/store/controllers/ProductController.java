package com.electronics.store.controllers;

import com.electronics.store.dtos.*;
import com.electronics.store.service.FileService;
import com.electronics.store.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/products")
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image}")
    private String imageUploadPath ;


    // create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        // random id
        String Id = UUID.randomUUID().toString();
        productDto.setProductId(Id);

        // current date
        productDto.setAddedDate(new Date());
        ProductDto productDto1 = productService.create(productDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody  ProductDto productDto,@PathVariable  String productId){
        ProductDto update = productService.update(productDto, productId);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    // get product by id
    @GetMapping("/Id/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable  String productId){
        ProductDto productDto = productService.get(productId);
        logger.info("get product by Id : {} ",productDto);
        return new ResponseEntity<>( productDto , HttpStatus.OK);
    }

    // get all products
    @GetMapping
    public ResponseEntity <PageableResponse<ProductDto>> getAllProducts(

            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> productServiceAll = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>( productServiceAll , HttpStatus.OK);

    }

    // get all live product
    @GetMapping("/live")
    public ResponseEntity <PageableResponse<ProductDto>> getLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allLive = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>( allLive, HttpStatus.OK);
    }

    // search product
    @GetMapping("/search/{query}")
    public ResponseEntity <PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allLive = productService.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>( allLive, HttpStatus.OK);
    }


    // delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.delete(productId);
        ApiResponseMessage mess = new ApiResponseMessage();
        mess.setMessage("Product Deleted is Successfully");
        mess.setSuccess(true);
        mess.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(mess, HttpStatus.OK);
    }


    // upload Image
    @PostMapping("/image/{productId}")
    public ResponseEntity <ImageResponse> uploadImages(@RequestParam("productImage") MultipartFile image, @PathVariable String productId) throws IOException {
        String fileName = fileService.uploadFile(image, imageUploadPath);

        ProductDto productName = productService.get(productId);
        productName.setProductImageName(fileName);
        ProductDto update = productService.update(productName, productId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(fileName)
                .success(true)
                .message("Image Uploaded is Successful !!")
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    @GetMapping("image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        ProductDto productName = productService.get(productId);
        logger.info("User Image name: {} ",productName.getProductImageName());
        InputStream resource = fileService.getResource(imageUploadPath, productName.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
