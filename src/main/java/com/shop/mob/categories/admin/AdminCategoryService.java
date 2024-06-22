package com.shop.mob.categories.admin;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AdminCategoryService {

    AdminCategoryRepository adminCategoryRepository;

    public AdminCategoryService(AdminCategoryRepository adminCategoryRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
    }
    // Optional helper method for cleaner null/empty check
    private static boolean isBlank(String value) {
      return value == null || value.isEmpty();
    }

    public List<Category> getAllCategories(){
        return adminCategoryRepository.findAll();
    }

    public Category createNewCategory(Category categoryDetails) {
        if (isBlank(categoryDetails.getName()) || isBlank(categoryDetails.getImage())) {
            throw new IllegalStateException("All fields (name and image) are required");
        }

        Optional<Category> cat = adminCategoryRepository.findCategoryByName(categoryDetails.getName());
        if(cat.isPresent()){
            throw new IllegalStateException("Category with name "+categoryDetails.getName()+" already exists");
        }
        return adminCategoryRepository.save(categoryDetails);
    }

    public boolean deleteCategory(String categoryId) {
        if (isBlank(categoryId)) {
            throw new IllegalStateException("Category ID is required");
        }
        try {
            Long id = Long.valueOf(categoryId);
            Optional<Category> category = adminCategoryRepository.findById(id);
            if (!category.isPresent()) {
                throw new IllegalStateException("Category with ID " + categoryId + " not found");
            }
            adminCategoryRepository.deleteById(id);
            return true;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Category ID format"); // More specific exception type
        }
    }

    @Transactional
    public Category updateCategory(Category categoryDetails){
        if(isBlank(categoryDetails.getId().toString())){
            throw new IllegalStateException("Category ID is required");
        }
        Category category = adminCategoryRepository.findById(categoryDetails.getId()).orElseThrow(() -> new IllegalStateException("No category found with the id " + categoryDetails.getId()));
        if(!isBlank(categoryDetails.getName()) && !Objects.equals(category.getName(), categoryDetails.getName())){
            Optional<Category> nameExist = adminCategoryRepository.findCategoryByName(categoryDetails.getName());
            if(nameExist.isPresent()){
                throw new IllegalStateException("Category with same name already exists");
            }
            category.setName(categoryDetails.getName());
        }
        return category;
    }
    
}
