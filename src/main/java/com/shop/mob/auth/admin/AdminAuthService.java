package com.shop.mob.auth.admin;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private final AdminAuthRepository adminAuthRepository;

    public AdminAuthService(AdminAuthRepository adminAuthRepository) {
        this.adminAuthRepository = adminAuthRepository;
    }

    boolean doRgister(Admin adminDetails){
        if(adminDetails.getEmail().isEmpty() || adminDetails.getPassword().isEmpty() || adminDetails.getName().isEmpty()){
            throw new IllegalStateException("Must provide all the fileds {name, email, password}");
        }
        Optional<Admin> companyAlreadyExist = this.adminAuthRepository.findAdminByEmail(adminDetails.getEmail());
        if(companyAlreadyExist.isPresent()){
            throw new IllegalStateException("Admin with that email already exists");
        }
        adminDetails.setPassword(hashPassword(adminDetails.getPassword()));
        Admin admin = this.adminAuthRepository.save(adminDetails);
        if(admin.getId() != null){
            return true;
        }else{
            return false;
        }
    }

    boolean doAdminLogin(Admin data) {
        if(data.getEmail().isEmpty() || data.getPassword().isEmpty()){
            throw new IllegalStateException("Must provide all the fileds {email , password}");
        }
        Optional<Admin> admin = adminAuthRepository.findAdminByEmail(data.getEmail());
        if(!admin.isPresent()){
            return false;
        }
        return comparePassword(data.getPassword(), admin.get().getPassword());
    }

    String hashPassword(String password){
        PasswordEncoder passEncoder = new BCryptPasswordEncoder();
        return passEncoder.encode(password);
    }

    Boolean comparePassword(String password, String hashedPass){
        return new BCryptPasswordEncoder().matches(password, hashedPass);
    }

}
