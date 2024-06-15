package com.shop.mob.auth.admin;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.mob.auth.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@Service
public class AdminAuthService {

    private final AdminAuthRepository adminAuthRepository;
    private final JwtService jwtService;

    public AdminAuthService(AdminAuthRepository adminAuthRepository,  JwtService jwtService) {
        this.adminAuthRepository = adminAuthRepository;
        this.jwtService = jwtService;
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
        return admin.getId() != null;
    }

    Optional<Admin> doAdminLogin(Admin data) {
        if(data.getEmail().isEmpty() || data.getPassword().isEmpty()){
            throw new IllegalStateException("Must provide all the fileds {email , password}");
        }
        Optional<Admin> admin = adminAuthRepository.findAdminByEmail(data.getEmail());
        if(!admin.isPresent()){
            throw new IllegalStateException("Admin with that email already exists");
        }
        boolean isCorrectPassword = comparePassword(data.getPassword(), admin.get().getPassword());
        if(!isCorrectPassword){
            throw new IllegalStateException("Incorrect Credential");
        }
        return admin;
    }

    String hashPassword(String password){
        PasswordEncoder passEncoder = new BCryptPasswordEncoder();
        return passEncoder.encode(password);
    }

    Boolean comparePassword(String password, String hashedPass){
        return new BCryptPasswordEncoder().matches(password, hashedPass);
    }

    String createNewAccessToken(Cookie[] cookies){
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if(token != null){
            Jws<Claims> data =  this.jwtService.decodeJwtToken(token);
            return this.jwtService.createJwtRefreshToken(data.getBody().get("name").toString(), data.getBody().get("email").toString());
        }
        return "";
    }

}
