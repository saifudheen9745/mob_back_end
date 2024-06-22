package com.shop.mob;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.mob.auth.JwtService;
import com.shop.mob.auth.admin.Admin;
import com.shop.mob.auth.admin.AdminAuthRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
                response.setStatus(HttpStatus.UNAUTHORIZED.value()); 
                response.setContentType("application/json");  
                response.setCharacterEncoding("UTF-8");  

                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Token Expired");  
                errorResponse.put("message", "Your access token has expired. Please refresh your token and try again.");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
                return false;
            } catch (JwtException e) { 
               
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid Token");  
                errorResponse.put("message", "The provided token is invalid. Please check your token and try again.");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
                return false;
            }
        }
        return false;
    }
}
