package com.shop.mob;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.shop.mob.auth.JwtService;
import com.shop.mob.auth.admin.Admin;
import com.shop.mob.auth.admin.AdminAuthRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiInterceptor implements HandlerInterceptor{

    private final JwtService jwtService;
    private final AdminAuthRepository adminAuthRepository;
    
    public ApiInterceptor(AdminAuthRepository adminAuthRepository, JwtService jwtService){
        this.adminAuthRepository = adminAuthRepository;
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7, authHeader.length());
            try{
                Jws<Claims> data = this.jwtService.decodeJwtToken(token);
                String email = data.getBody().get("email").toString();
                Optional<Admin> cmp = this.adminAuthRepository.findAdminByEmail(email);
                if(cmp.isPresent()){
                    return true;
                }
            }catch(ExpiredJwtException e){
                response.getWriter().write("Token Expired");
                response.setStatus(401);
            }
        }
        return false;
    }
}
