package com.animephix.backend.service;

import com.animephix.backend.dto.*;
import com.animephix.backend.model.Rol;
import com.animephix.backend.model.Usuario;
import com.animephix.backend.repository.RolRepository;
import com.animephix.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private RolRepository rolRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.rolRepository = rolRepository;
    }

    // Configuración del registro
    @Transactional
    public void registrar(RegistroDTO registroDTO) {
        //Compruebo si el email está registrado
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        //Guardo los datos del nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDTO.getNombre());
        nuevoUsuario.setEmail(registroDTO.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(registroDTO.getPassword())); //Encripto la contraseña antes de guardarla
        nuevoUsuario.setUrlImagen("/Imagenes/FotosPerfil/default.jpg"); //Foto de perfil por defecto
        nuevoUsuario.setHabilitado(true);

        //Asigno por defecto que el usuario es normal
        Rol rolPorDefecto = new Rol();
        rolPorDefecto.setIdRol(2L);
        nuevoUsuario.setRol(rolPorDefecto);

        usuarioRepository.save(nuevoUsuario);

        //Envio del mensaje de bienvenida
        String subject = "¡Bienvenido/a a AnimePhix!";
        String body = """
                ¡Bienvenido/a a AnimePhix! 💫
                
                Gracias por unirte a nuestra comunidad de amantes del anime. 🎌✨
                
                Ya puedes dejar tus opiniones en los episodios, guardar tus favoritos para no perderte ningún capítulo,
                y gestionar tu calendario personalizado con tus animes semanales. ¡Hasta puedes crear tus eventos personales!
                
                ¡Nos alegra tenerte por aquí! 🧡
                
                El equipo de AnimePhix.
                """;
        emailService.enviarCorreo(nuevoUsuario.getEmail(), subject, body);
    }

    @Transactional(readOnly = true)
    public Usuario devolverDatosUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado al usuario indicado."));
    }

    // Solicitud del usuario para obtener una nueva contraseña
    @Transactional
    public void solicitarPassword(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        String nuevaPassword = generarPasswordAleatoria();
        user.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(user);

        String subject = "Nueva contraseña";
        String body = """
                Hola,

                Hemos recibido una solicitud para restablecer tu contraseña en AnimePhix.

                Tu nueva contraseña es:""" + " " + nuevaPassword + """

                Puedes modificarla en tu página de perfil cuando inicies sesión.

                ¡Gracias por usar AnimePhix! 🦡

                El equipo de AnimePhix.
                """;
        emailService.enviarCorreo(email, subject, body);
    }

    // Solicitud del usuario para modificar la contraseña
    @Transactional
    public void modificarPassword(ActualizarPasswordDTO datos) {
        Usuario user = usuarioRepository.findByEmail(datos.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        user.setPassword(passwordEncoder.encode(datos.getNuevaPassword()));
        usuarioRepository.save(user);
    }

    // Solicitud del usuario para modificar nombre de usuario y email
    @Transactional
    public void modificarNombre(ActualizarNombreUsuarioDTO datos) {
        Usuario user = usuarioRepository.findByEmail(datos.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (user.getNombre().equals(datos.getNuevoNombre())) {
            throw new RuntimeException("Ese nombre de usuario ya está registrado.");
        }

        user.setNombre(datos.getNuevoNombre());
        usuarioRepository.save(user);
    }

    // Solicitud del usuario para modificar la imagen de perfil
    @Transactional
    public void modificarImagen(ActualizarImagenPerfilDTO datos) {
        Usuario user = usuarioRepository.findByEmail(datos.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        user.setUrlImagen(datos.getUrlImagen());
        usuarioRepository.save(user);
    }

    // Solicitud del usuario para darse de baja
    @Transactional
    public void eliminarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Ese usuario no existe"));

        usuarioRepository.deleteById(usuario.getIdUsuario());

        String subject = "Te echaremos de menos";
        String body = """
                Hola,

                Lamentamos saber que has decidido darte de baja de AnimePhix. 😢

                Tu cuenta ha sido eliminada correctamente y ya no recibirás más notificaciones ni correos de nuestra parte.

                Gracias por haber formado parte de nuestra comunidad de anime.
                Si en el futuro decides volver, estaremos encantados de recibirte de nuevo.

                ¡Te deseamos lo mejor!

                El equipo de AnimePhix.
                """;
        emailService.enviarCorreo(email, subject, body);
    }

    @Transactional(readOnly = true)
    public List<Usuario> devolverTodosUsuarios() {
        List<Usuario> listado = usuarioRepository.findAll();
        if (listado.isEmpty()) {
            throw new RuntimeException("No hay usuarios registrados.");
        }
        return listado;
    }

    @Transactional
    public void cambiarEstadoHabilitado(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        usuario.setHabilitado(!usuario.isHabilitado());
        usuarioRepository.save(usuario);

        // Si el usuario es deshabilitado se le envia un mensaje
        if (!usuario.isHabilitado()) {
            String subject = "Cuenta deshabilitada";
            String body = """
                Tu cuenta ha sido deshabilitada ❌
                
                Hemos detectado que tu actividad ha infringido las normas de uso de AnimePhix. 🚫
                
                Por este motivo, tu cuenta ha sido deshabilitada temporal o permanentemente, según la gravedad de la infracción.
                Si crees que se trata de un error o deseas más información, puedes ponerte en contacto con nuestro equipo de soporte.
                
                Cuidar el buen ambiente de la comunidad es una prioridad para nosotros. 🛡️
                
                El equipo de AnimePhix.
                """;
            emailService.enviarCorreo(usuario.getEmail(), subject, body);
        } else { //En caso de que se le vuelva a habilitar la cuenta, se le notifica por un mensaje
            String subject = "¡Bienvenido/a de vuelta!";
            String body = """
                ¡Tu cuenta ha sido reactivada! ✅

                Nos alegra informarte que tu cuenta en AnimePhix vuelve a estar habilitada. 🧡

                Te recordamos que es importante seguir las normas de la comunidad para mantener un espacio seguro y agradable para todos. 📝✨

                Gracias por tu comprensión y por formar parte de nuestra comunidad de amantes del anime. ¡Te damos nuevamente la bienvenida!

                El equipo de AnimePhix.
                """;
            emailService.enviarCorreo(usuario.getEmail(), subject, body);
        }

    }

    @Transactional
    public void habilitarAdminitrador(Long id, String emailAdmin) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        Usuario admin = usuarioRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado."));

        if (!admin.getEmail().equals("admin@gmail.com")) {
            throw new RuntimeException("Solo el SuperAdministrador puede habilitar o deshabilitar cuentas administradoras.");
        }

        String nuevoRol = usuario.getRol().getNombre().equals("ROLE_USER") ? "ROLE_ADMIN" : "ROLE_USER";
        usuario.setRol(rolRepository.findByNombre(nuevoRol));

        if (nuevoRol.equals("ROLE_ADMIN")) {
            usuario.setUrlImagen("/Imagenes/FotosPerfil/admin.jpg");
        }

        usuarioRepository.save(usuario);
    }

    //Metodo privado auxiliar
    private String generarPasswordAleatoria() {
        String mayus = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minus = "abcdefghijklmnopqrstuvwxyz";
        String num = "0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            password.append(mayus.charAt(random.nextInt(mayus.length())));
            password.append(minus.charAt(random.nextInt(minus.length())));
            password.append(num.charAt(random.nextInt(num.length())));
        }

        return password.toString();
    }

}
