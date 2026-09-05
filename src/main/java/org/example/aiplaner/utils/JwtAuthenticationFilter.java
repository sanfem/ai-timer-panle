package org.example.aiplaner.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils =jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String Path=request.getServletPath();

        if(Path.equals("/api/Login/login")||Path.equals("/api/Login/register")||Path.equals("/api/Login/sendVerificationCode")){
            filterChain.doFilter(request,response);
            return;
        }


        Cookie[] cookies = request.getCookies();

        if(cookies==null){
            response.sendError(401);
            return;
        }
        String token=" ";
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                token=cookie.getValue();
                break;
            }
        }
        if(null==token||token.isEmpty()){
            response.sendError(401);
            return;
        }

        if(token.isEmpty()){
            response.sendError(401);
            return;
        }


        Claims claims=null;
        try {
            claims = jwtUtils.verifyToken(token);
        }catch (RuntimeException e){
            System.out.printf(e.getMessage());
        }

        if(!claims.isEmpty()){
            long id=jwtUtils.getUserId(token);
            request.setAttribute("userId",id);

        }
        else{
            response.sendError(401);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
