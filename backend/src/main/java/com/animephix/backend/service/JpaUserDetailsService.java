package com.animephix.backend.service;

import com.animephix.backend.model.Usuario;
import com.animephix.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public JpaUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Este método es utilizado por Spring Security para cargar los detalles del usuario durante el proceso de autenticación
    @Transactional(readOnly = true)
    @Override
    //Security core utiliza estos métodos ya cargados, aunque se utilice Username yo lo he configurado para que utilice el email
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(email);

        // Si no se encuentra el usuario, se lanza una excepción indicando que el email no existe
        if (!optionalUser.isPresent())
            throw new UsernameNotFoundException(String.format("El email %s no existe en el sistema", email));

        // Si se encuentra, obtengo el objeto usuario
        Usuario user = optionalUser.get();

        // Creo una autoridad para el usuario basada en su rol (cada usuario tiene un solo rol, por lo tanto una sola autoridad)
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRol().getNombre());

        // Retorno un objeto User de Spring Security, donde se pasa el email, la contraseña, si está habilitado, y las autoridades (roles) del usuario
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                 // Email del usuario
                user.getPassword(),              // Contraseña del usuario
                user.isHabilitado(),             // Indica si el usuario está habilitado o no (si puede iniciar sesión)
                true,                            // La cuenta no está expirada
                true,                            // La contraseña no está expirada
                true,                            // La cuenta no está bloqueada
                Collections.singletonList(authority) // Lista de autoridades (roles) del usuario (en este caso, solo un rol)
        );

    }
}
