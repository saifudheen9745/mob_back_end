package com.shop.mob.auth.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table
@Entity
public class Admin {    

    @Id
    @SequenceGenerator(
        initialValue = 1,
        sequenceName = "admin_id_sequence",
        name = "admin_id_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy=GenerationType.SEQUENCE,
        generator="admin_id_sequence"
    )
    private Long id;
    private String name;
    private String email;
    private String password;

    public Admin(){
        
    }
       
    public Admin(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Admin(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

          
}
