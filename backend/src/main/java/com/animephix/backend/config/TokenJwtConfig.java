package com.animephix.backend.config;

import io.jsonwebtoken.Jwts;

import java.security.Key;

public class TokenJwtConfig {
    //Clave secreta para generar el token
    public static final Key SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
