package com.animephix.backend.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.animephix.backend.config.TokenJwtConfig.*;

//Clase para la validación del token
public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        //Obtengo el token que se envia desde el cliente
        String header = request.getHeader(HEADER_AUTHORIZATION);
        //Si el endpoint es de acceso publico NO hay que hacer nada
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            //Se continua con la secuencia de los filtros
            chain.doFilter(request, response);
            return;
        }
        //Quito el prefijo del token
        String token = header.replace(PREFIX_TOKEN, "");
        //Obtengo los claims
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) SECRET_KEY)
                    .build().parseSignedClaims(token).getPayload();
            //Obtengo el email
            String email = claims.getSubject();
            // Obtengo los roles como lista
            List<String> roles = (List<String>) claims.get("authorities");
            // Convierto los roles en authorities
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            //Creo el token de autenticación
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            //Autentico
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //Continuo con la cadena de filtros
            chain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token Jwt es inválido");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        }
    }
}
