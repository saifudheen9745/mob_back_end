package com.shop.mob.categories.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import io.jsonwebtoken.io.IOException;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

@Service
public class AdminCategoryService {

    AdminCategoryRepository adminCategoryRepository;
    
    @Resource
    private Cloudinary cloudinary;

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

    public Category createNewCategory(MultipartFile image, String name) {
        if (isBlank(name) || image.isEmpty() ) {
            throw new IllegalStateException("All fields (name and image) are required");
        }

        Optional<Category> cat = adminCategoryRepository.findCategoryByName(name);
        if(cat.isPresent()){
            throw new IllegalStateException("Category with name "+name+" already exists");
        }

        String imageUrl = this.uploadFile(image, name);

        Category newCategory = new Category();
        newCategory.setName(name);
        if(!isBlank(imageUrl)){
            newCategory.setImage(imageUrl);
        }else{
            newCategory.setImage("");
        }
        
        return adminCategoryRepository.save(newCategory);
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
            this.deleteFile(category.get().getName());
            return true;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Category ID format"); // More specific exception type
        }
    }

    @Transactional
    public Category updateCategory(MultipartFile image, Long id, String name){
        if(isBlank(id.toString())){
            throw new IllegalStateException("Category ID is required");
        }
        Category category = adminCategoryRepository.findById(id).orElseThrow(() -> new IllegalStateException("No category found with the id " + id));
        if(!isBlank(name) && !Objects.equals(category.getName(), name) ){
            Optional<Category> nameExist = adminCategoryRepository.findCategoryByName(name);
            if(nameExist.isPresent() && !Objects.equals(nameExist.get().getId(), id)){
                throw new IllegalStateException("Category with same name already exists");
            }
            category.setName(name);
        }
        if(image != null && !image.isEmpty()){
            String imageUrl = this.uploadFile(image, name);
            category.setImage(imageUrl);
        }
        return category;
    }

    public String uploadFile(MultipartFile file, String imageName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "MOB/Categories");
            options.put("public_id", imageName);
            @SuppressWarnings("unchecked")
            Map<String, String> uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);

        }catch (IOException | java.io.IOException e){
            return null;
        }
    }

    public void deleteFile(String name){
        try {
            HashMap<Object, Object> options = new HashMap<>();
            cloudinary.uploader().destroy("MOB/Categories/"+name, options);
        } catch (java.io.IOException ex) {
        }
    }
    
}
