package com.phonebook.PhoneBook.services;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phonebook.PhoneBook.util.Constants;
import com.phonebook.PhoneBook.util.JwtUtil;
import com.phonebook.PhoneBook.util.Utility;
import com.phonebook.PhoneBook.util.responses.ErrorResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class jwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService uServices;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                final String authorizationHeader = request.getHeader("token");
                String email = null;
                String token = null;
                if(authorizationHeader!=null){
                    token = authorizationHeader;
                    try {
                        email = jwtUtil.extractEmail(token);
                    } catch (Exception e) {
                        log.info("Error in extraction token=> "+e.getLocalizedMessage()+" ");
                        ErrorResponse<?> errorResponse = new ErrorResponse<>();
                        errorResponse.setCode(403);
                        errorResponse.setHttpStatus(403);
                        errorResponse.setMessage(Constants.INVALID_TOKEN);
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getOutputStream().println(new Utility().convertObjectToJson(errorResponse));
                        response.getOutputStream().close();
                        return;
                    }
                }

                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    System.out.println("[Here]: "+email);
                    UserDetails userDetails = this.uServices.loadUserByUsername(email);
                    if(jwtUtil.validateToken(token, userDetails)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
                
                filterChain.doFilter(request, response);
        
    }
    

    
}
