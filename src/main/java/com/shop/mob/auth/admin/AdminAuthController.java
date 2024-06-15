package com.shop.mob.auth.admin;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shop.mob.auth.AuthResponse;
import com.shop.mob.auth.JwtService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping(path="api/v1/admin", produces=MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
public class AdminAuthController {
    
    private final AdminAuthService adminAuthService;
    private final JwtService jwtService;

    public AdminAuthController(AdminAuthService adminAuthService, JwtService jwtService) {
        this.adminAuthService = adminAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Admin adminDetails) {
        try {
            
            boolean hasRegitered = this.adminAuthService.doRgister(adminDetails);
            if(hasRegitered){
                return new ResponseEntity<>(new AuthResponse("Registration Successful", true), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new AuthResponse("Registration Failed", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(e.getMessage(),false), HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> adminLogin(@RequestBody Admin adminDetails,HttpServletResponse response) {
        try {
            Optional<Admin> adminData = adminAuthService.doAdminLogin(adminDetails);
            if(!adminData.isEmpty()){
                String refreshToken = this.jwtService.createJwtRefreshToken(adminData.get().getName(), adminData.get().getEmail());
                ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .build();
                response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());
                return new ResponseEntity<>(new AuthResponse("Login Successful", true, this.jwtService.createJwtAccessToken(adminData.get().getName(), adminData.get().getEmail())), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new AuthResponse("Login Failed", false, ""), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(e.getMessage(),false,""), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/renewToken")
    public ResponseEntity<AuthResponse> renewAdminToken(HttpServletRequest request) {
        try {
            String newAccessToken = adminAuthService.createNewAccessToken(request.getCookies());
            if(newAccessToken != null && !"".equals(newAccessToken)){
                return new ResponseEntity<>(new AuthResponse("New Access Token Created Successfully", true, newAccessToken), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new AuthResponse("New Token Creation Failed",false,""), HttpStatus.BAD_GATEWAY);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(e.getMessage(),false,""), HttpStatus.BAD_GATEWAY);
        }  
    }
    
    
}
