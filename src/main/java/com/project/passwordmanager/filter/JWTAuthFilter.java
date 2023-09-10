package com.project.passwordmanager.filter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class JWTAuthFilter extends HttpFilter {

    private JWTVerifier jwtVerifier;

    public JWTAuthFilter(JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtToken = request.getHeader("authorization").split(" ")[1];

        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);

        request.setAttribute("username", decodedJWT.getClaim("username").asString());
        chain.doFilter(request, response);
    }

}
