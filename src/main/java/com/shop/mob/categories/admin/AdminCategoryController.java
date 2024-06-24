package com.shop.mob.categories.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping(path="api/v1/admin/category", produces=MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
public class AdminCategoryController {

    AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping()
    public ResponseEntity<AdminCategoryResponse> getAllCategories() {
        try {
            List<Category> categories =  this.adminCategoryService.getAllCategories();
            return new ResponseEntity<>(new AdminCategoryResponse(categories, "Category List", true),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping()
    public ResponseEntity<AdminCategoryResponse> createNewCategory(@RequestBody MultipartFile image, String name) {
        try {
            Category category = this.adminCategoryService.createNewCategory(image, name);
            if(category != null){
                return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(category), "Category created successfully", true),HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(new Category()), "Category creation failed", false),HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<AdminCategoryResponse> deleteCategory(@PathVariable String categoryId) {
        try {
            boolean isDeleted = this.adminCategoryService.deleteCategory(categoryId);
            if(isDeleted){
                return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(), "Category deleted successfully", true),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(), "Category deletion failed", false),HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PutMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminCategoryResponse> updateCategory(@RequestBody MultipartFile image, Long id, String name) {
        try {
            Category category = this.adminCategoryService.updateCategory(image, id, name);
            if(category != null){
                return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(category), "Category updated successfully", true),HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(new Category()), "Category updation failed", false),HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AdminCategoryResponse(Arrays.asList(), e.getMessage(), false),HttpStatus.EXPECTATION_FAILED);
        }
    }

    // @PostMapping("/test")
    // public String test(@RequestBody MultipartFile image, String name) {
    //     return adminCategoryService.uploadFile(image, "Mob", name);
    // }
    
    
}
