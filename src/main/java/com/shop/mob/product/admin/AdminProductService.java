package com.shop.mob.product.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import io.jsonwebtoken.io.IOException;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

@Service
public class AdminProductService {
    private final AdminProductRepository adminProductRepository;

    @Resource
    private Cloudinary cloudinary;

    public AdminProductService(AdminProductRepository adminProductRepository) {
        this.adminProductRepository = adminProductRepository;
    }

    public List<Product> getAllProducts(){
        return adminProductRepository.findAll();
    }

    public Product createNewProduct (HashMap<String, Object> formData, MultipartFile image){

        Product newProduct = new Product();
        try {
            newProduct.setName((String) formData.get("name"));
            newProduct.setDescription((String) formData.get("description"));
            newProduct.setPrice(Float.valueOf((String) formData.get("price")));
            newProduct.setQuantity(Long.valueOf((String) formData.get("quantity")));
            newProduct.setCategory((String) formData.get("category"));

            // Checking if the disable contains any value
            Optional<Boolean> isDisabledOptional = Optional.ofNullable(formData.get("disabled"))
            .map(value -> Boolean.valueOf(value.toString()));
            if (isDisabledOptional.isPresent()) {
                boolean isDisabled = isDisabledOptional.get();
                newProduct.setDisabled(isDisabled);
            } else {
                throw new IllegalArgumentException("The 'disabled' field is not provided.");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Something went wrong, Please make sure if all the fields are provided");
        }

        if (isBlank(newProduct.getName()) || isBlank(newProduct.getPrice()) ||
        isBlank(newProduct.getDescription()) || isBlank(newProduct.getQuantity())) {
        throw new IllegalArgumentException("Missing product properties. Please ensure all fields are populated.");
        }
        if(image == null){
            throw new IllegalArgumentException("Missing product properties. Please ensure all fields are populated {Image}.");
        }
        Optional<Product> prod = adminProductRepository.findProductByName(newProduct.getName());
        if(prod.isPresent()){
            throw new IllegalStateException("Product with name "+newProduct.getName()+" already exists");
        }
        String imageUrl = this.uploadFile(image, newProduct.getName());
        newProduct.setImage(imageUrl);
        return adminProductRepository.save(newProduct);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Transactional
    public Product updateProduct(HashMap<String, Object> formData,@RequestBody MultipartFile image){
        Product newProduct = new Product();
        try {
            newProduct.setId(Long.valueOf(formData.get("id").toString()));
            newProduct.setName((String) formData.get("name"));
            newProduct.setDescription((String) formData.get("description"));
            newProduct.setPrice(Float.valueOf((String) formData.get("price")));
            newProduct.setQuantity(Long.valueOf((String) formData.get("quantity")));
            newProduct.setCategory((String) formData.get("category"));

            // Checking if the disable contains any value
            Optional<Boolean> isDisabledOptional = Optional.ofNullable(formData.get("disabled"))
            .map(value -> Boolean.valueOf(value.toString()));
            if (isDisabledOptional.isPresent()) {
                boolean isDisabled = isDisabledOptional.get();
                newProduct.setDisabled(isDisabled);
            } else {
                throw new IllegalArgumentException("The 'disabled' field is not provided.");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Something went wrong, Please make sure if all the fields are provided");
        }

        if(isBlank(newProduct.getName())){
            throw new IllegalStateException("Product ID is required");
        }

        Product product = adminProductRepository.findById(newProduct.getId()).orElseThrow(() -> new IllegalStateException("No category found with the id " + newProduct.getName()));

        // Checking for duplicate name.
        if(!isBlank(formData.get("name").toString()) && !Objects.equals(product.getName(), formData.get("name")) ){
            Optional<Product> nameExist = adminProductRepository.findProductByName(formData.get("name").toString());
            if(nameExist.isPresent() && !Objects.equals(nameExist.get().getId(), newProduct.getName())){
                throw new IllegalStateException("Category with same name already exists");
            }
            product.setName(formData.get("name").toString());
        }
        // Checking if the updatedFormData is different from existing data. if so , it updates else not.
        if(!newProduct.getCategory().isEmpty() && !Objects.equals(product.getCategory(), newProduct.getCategory())){
            product.setCategory(newProduct.getCategory());
        }
        if(newProduct.getPrice() != null && !Objects.equals(product.getPrice(), newProduct.getPrice())){
            product.setPrice(newProduct.getPrice());
        }
        if(newProduct.getQuantity() != null && !Objects.equals(product.getQuantity(), newProduct.getQuantity())){
            product.setQuantity(newProduct.getQuantity());
        }
        if(!Objects.equals(product.isDisabled(), newProduct.isDisabled())){
            product.setDisabled(newProduct.isDisabled());
        }
        if(!newProduct.getDescription().isEmpty() && !Objects.equals(product.getDescription(), newProduct.getDescription())){
            product.setDescription(newProduct.getDescription());
        }

        if(image != null && !image.isEmpty()){
            this.deleteFile(product.getName());
            String imageUrl = this.uploadFile(image, newProduct.getName());
            product.setImage(imageUrl);
        }
        return product;
    }

    public boolean deleteProduct(String productId) {
        if (isBlank(productId)) {
            throw new IllegalStateException("Product ID is required");
        }
        try {
            Long id = Long.valueOf(productId);
            Optional<Product> product = adminProductRepository.findById(id);
            if (!product.isPresent()) {
                throw new IllegalStateException("Product with ID " + productId + " not found");
            }
            adminProductRepository.deleteById(id);
            this.deleteFile(product.get().getName());
            return true;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Product ID format"); // More specific exception type
        }
    }

    public String uploadFile(MultipartFile file, String imageName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", "MOB/Products");
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
            cloudinary.uploader().destroy("MOB/Products/"+name, options);
        } catch (java.io.IOException ex) {
            
        }
    }

    public boolean isBlank(String data){
        return data.isEmpty() || data.isBlank();
    }

    public boolean isBlank(Number data){
        return data == null;
    }

}
