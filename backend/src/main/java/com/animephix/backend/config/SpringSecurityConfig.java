package com.animephix.backend.config;

import com.animephix.backend.config.filter.JwtAuthenticationFilter;
import com.animephix.backend.config.filter.JwtValidationFilter;
import com.animephix.backend.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class SpringSecurityConfig {

    private AuthenticationConfiguration authenticationConfiguration;
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JpaUserDetailsService jpaUserDetailsService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    //Codificador de contraseñas
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Metodo para dejar public la ruta
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        DefaultSecurityFilterChain build = http
                //Gestión de rutas publicas
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/solicitar-password-olvidada").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comentarios/listar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reportes/crear").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animes/en-emision").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animes/tarjeta-directorio").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animes/filtros-directorio").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animes/establecer-filtros").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animes/buscar-anime").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animes/anime-datos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/episodios/recientes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/episodios/especifico").permitAll()
                        .requestMatchers(HttpMethod.GET, "/episodios/total-anime").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tipos-problemas/listado-nombres").permitAll()
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)))
                .addFilterBefore(new JwtValidationFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class)
                .csrf(config -> config.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
        return build;
    }

    //Permite generar y obtener el authenticationManager de la aplicacion de Spring Security
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuración CORS para permitir acceso a Angular
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Se indican las rutas para que quede claro que CorsFilter se debe importar
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        // Se une el filtro con la configuracion creada en el metido corsConfigurationSource()
        FilterRegistrationBean<org.springframework.web.filter.CorsFilter> corsBean = new FilterRegistrationBean<>(new org.springframework.web.filter.CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}
