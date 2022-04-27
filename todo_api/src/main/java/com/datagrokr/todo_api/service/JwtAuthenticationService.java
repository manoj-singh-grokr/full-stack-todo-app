package com.datagrokr.todo_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.datagrokr.todo_api.security.jwt.JwtTokenRequest;
import com.datagrokr.todo_api.security.jwt.JwtTokenResponse;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

@Service
@AllArgsConstructor
public class JwtAuthenticationService {

    private AuthenticationManager authenticationManager;

    private static final String SECRET_KEY = "PrL3gvHFZv";

    public JwtTokenResponse authenticate(JwtTokenRequest tokenRequest, String url, TimeZone timeZone)
            throws AuthenticationException {
        UserDetails userDetails = managerAuthentication(tokenRequest.getUsername(), tokenRequest.getPassword());
        System.out.println(userDetails);
        String token = generateToken(userDetails.getUsername(), url, timeZone);

        return new JwtTokenResponse(token);
    }

    public JwtTokenResponse generateRefreshToken(String subject, String url, TimeZone timeZone) {
        try {
            Instant now = Instant.now();

            ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            String token = JWT.create()
                    .withIssuer(url)
                    .withSubject(subject)
                    .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                    .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(60).toInstant()))
                    .sign(algorithm);

            return new JwtTokenResponse(token);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            throw new JWTCreationException("Exception creating token", e);
        }
    }

    public JwtTokenResponse refreshAccessToken(Cookie cookie, String url, TimeZone timeZone)
            throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(cookie.getValue());

        return new JwtTokenResponse(generateToken(decodedJWT.getSubject(), url, timeZone));
    }

    private String generateToken(String username, String url, TimeZone timeZone) {
        try {
            Instant now = Instant.now();

            ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of(timeZone.getID()));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(url)
                    .withSubject(username)
                    .withIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                    .withExpiresAt(Date.from(zonedDateTimeNow.plusMinutes(10).toInstant()))
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            e.printStackTrace();
            throw new JWTCreationException("Exception creating token", e);
        }
    }

    private UserDetails managerAuthentication(String username, String password) throws AuthenticationException {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return (UserDetails) authenticate.getPrincipal();
    }
}
