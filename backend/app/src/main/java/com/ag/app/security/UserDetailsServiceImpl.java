package com.ag.app.security;

import com.ag.app.model.Usuario;
import com.ag.app.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        // Mapeia TipoUsuario para ROLE_*
        String role = mapTipoToRole(usuario.getTipo().name());

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getStatus() != null && usuario.getStatus().name().equals("ATIVO"),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    private String mapTipoToRole(String tipoUsuario) {
        return switch (tipoUsuario) {
            case "ADMINISTRADOR" -> "ROLE_ADMINISTRADOR";
            case "MEDICO" -> "ROLE_MEDICO";
            case "PACIENTE" -> "ROLE_PACIENTE";
            case "FUNCIONARIO" -> "ROLE_FUNCIONARIO";
            case "EMPRESA_CONVENIO" -> "ROLE_EMPRESA_CONVENIO";
            case "PARTICULAR" -> "ROLE_PARTICULAR";
            default -> "ROLE_USER";
        };
    }
}