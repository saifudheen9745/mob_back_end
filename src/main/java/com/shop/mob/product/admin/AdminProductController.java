package com.shop.mob.product.admin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.mob.HttpResponseModel;


@RestController
@RequestMapping(path="api/v1/admin/product", produces=MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class AdminProductController {

    AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping()
    public ResponseEntity<HttpResponseModel> getAllCategories() {
        try {
            List<Product> products =  this.adminProductService.getAllProducts();
            return new ResponseEntity<>(new HttpResponseModel<>(products, "Product List", true),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponseModel> createNewProduct(@RequestParam HashMap<String, Object> formData, @RequestPart MultipartFile image) {
         try {
            Product product = this.adminProductService.createNewProduct(formData, image);
            if(product != null){
                return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(product), "Product created successfully", true),HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), "Product creation failed", false),HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpResponseModel> deleteCategory(@PathVariable String productId) {
        try {
            boolean isDeleted = this.adminProductService.deleteProduct(productId);
            if(isDeleted){
                return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), "Product deleted successfully", true),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), "Product deletion failed", false),HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PutMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponseModel> updateProduct(@RequestBody MultipartFile image, @RequestParam HashMap<String, Object> formData) {
        try {
            Product product = this.adminProductService.updateProduct(formData, image);
            if(product != null){
                return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(product), "Product updated successfully", true),HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), "Product updation failed", false),HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpResponseModel(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }
    
}
