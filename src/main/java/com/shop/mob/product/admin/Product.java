package com.shop.mob.product.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table
@Entity
public class Product {
    @Id
    @SequenceGenerator(
        initialValue=1,
        sequenceName = "product_id_sequence", 
        name = "product_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy=GenerationType.SEQUENCE,
        generator="product_id_sequence"
    )
    private Long Id;
    @Column(unique=true)
    private String name;
    private String description;
    private Float price;
    private String image;
    private Long quantity;
    private boolean disabled = false;
    private String category;

    public Product(){}

    public Product(Long Id, String description, String image, String name, Float price, Long quantity, boolean disabled, String category) {
        this.Id = Id;
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.disabled = disabled;
        this.category = category;
    }

    public Product(String description, String image, String name, Float price, Long quantity, boolean disabled, String category) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.disabled = disabled;
        this.category = category;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
