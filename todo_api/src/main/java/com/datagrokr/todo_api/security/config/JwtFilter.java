package com.datagrokr.todo_api.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final String SECRET_KEY = "PrL3gvHFZv";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("FILTER");

        Cookie tokenCookie = getCookieWithAccessToken(request.getCookies());
        if (tokenCookie != null) {
            cookieAuthentication(tokenCookie);
        }

        chain.doFilter(request, response);
        log.info("chain: {}, request: {}, response: {}", chain, request, response);

    }

    private void cookieAuthentication(Cookie cookie) {
        UsernamePasswordAuthenticationToken auth = getTokenAuthentication(cookie.getValue());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private UsernamePasswordAuthenticationToken getTokenAuthentication(String token) {

        DecodedJWT decodedJWT = decodeAndVerifyJwt(token);
        String subject = decodedJWT.getSubject();
        Set<SimpleGrantedAuthority> simpleGrantedAuthority = Collections.singleton(new SimpleGrantedAuthority("USER"));
        return new UsernamePasswordAuthenticationToken(subject, null, simpleGrantedAuthority);

    }

    private Cookie getCookieWithAccessToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        Cookie tokenCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                tokenCookie = cookie;
                break;
            }
        }

        return tokenCookie;
    }

    private DecodedJWT decodeAndVerifyJwt(String token) {
        DecodedJWT decodedJWT = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build();

            decodedJWT = verifier.verify(token);

        } catch (JWTVerificationException e) {
            // Invalid signature/token expired
            e.printStackTrace();
        }

        return decodedJWT;
    }
}
