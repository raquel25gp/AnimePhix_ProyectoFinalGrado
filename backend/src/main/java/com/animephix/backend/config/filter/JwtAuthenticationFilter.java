package com.animephix.backend.config.filter;

import com.animephix.backend.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.animephix.backend.config.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Usuario user = null;
        String email = null;
        String password = null;

        //Se intenta leer el cuerpo de la solicitud HTTP y convertirlo en un objeto Usuario
        //usando ObjectMapper de la biblioteca Jackson
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            email = user.getEmail();
            password = user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Se crea un objeto UsernamePasswordAuthenticationToken con el email del usuario y la contraseña obtenidos
        //del objeto Usuario. Este token es una implementación de Authentication que será usado por el
        //AuthenticationManager para realizar la autenticación.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        //El authenticate llama al JpaUserDetailService para que se autentique mediante la base de datos
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        //Obtengo el usuario
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        //Obtengo el email, llamado username en este tipo de clase
        String email = user.getUsername();
        //Obtengo todos los roles como lista
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        //Guardo los roles como lista en los claims
        Claims claims = Jwts.claims()
                .add("authorities", roles)
                .add("email", email)
                .build();
        //Genero el token con la llave secreta
        String token = Jwts.builder()
                .subject(email)
                .claims(claims) //añado los claims que contiene el rol
                .expiration(new Date(System.currentTimeMillis() + 86400000)) //fecha de expiracion (24 horas)
                .issuedAt(new Date()) //fecha actual
                .signWith(SECRET_KEY)
                .compact();
        //Devuelvo el token al cliente
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        //Lo paso tambien como una respuesta JSON
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("email", email);
        body.put("message", "Se ha iniciado sesión con éxito.");
        //Escribo el JSON en la respuesta
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Error al iniciar sesión: Email o contraseña incorrectos.");
        body.put("error", failed.getMessage());

        //Escribo el JSON en la respuesta
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);
    }
}
