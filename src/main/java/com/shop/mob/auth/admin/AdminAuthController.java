package com.shop.mob.auth.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shop.mob.auth.AuthResponse;
import com.shop.mob.auth.JwtService;


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
    public ResponseEntity<AuthResponse> adminLogin(@RequestBody Admin adminDetails) {
        try {
            
            boolean isValidUser = adminAuthService.doAdminLogin(adminDetails);
            if(isValidUser){
                return new ResponseEntity<>(new AuthResponse("Login Successful", true, this.jwtService.createJwtToken(adminDetails.getName(), adminDetails.getEmail())), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new AuthResponse("Login Failed", false, ""), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(e.getMessage(),false,""), HttpStatus.BAD_GATEWAY);
        }
    }
    
}
