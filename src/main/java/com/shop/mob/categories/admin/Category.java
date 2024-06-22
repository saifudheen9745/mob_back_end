package com.shop.mob.categories.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table
@Entity
public class Category {

    @Id
    @SequenceGenerator(
        initialValue=1,
        sequenceName = "category_id_sequence", 
        name = "category_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy=GenerationType.SEQUENCE,
        generator="category_id_sequence"
    )
    private Long id;
    private String name;
    private String image;

    public Category(){}

    public Category(Long id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public Category(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
