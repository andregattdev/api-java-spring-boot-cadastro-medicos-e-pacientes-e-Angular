package com.ag.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ag.app.model.StatusUsuario;
import com.ag.app.model.TipoUsuario;
import com.ag.app.model.Usuario;
import com.ag.app.repository.UsuarioRepository;
import com.ag.app.model.Doutor;
import com.ag.app.repository.DoutorRepository;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository usuarioRepository,
                                       DoutorRepository doutorRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = System.getenv().getOrDefault("ADMIN_EMAIL", "admin@email.com");
            String adminSenha = System.getenv().getOrDefault("ADMIN_SENHA", "Senha@123");
            if (usuarioRepository.findByEmail(adminEmail) == null) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador Mestre");
                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode(adminSenha));
                admin.setTipo(TipoUsuario.ADMINISTRADOR);
                admin.setStatus(StatusUsuario.ATIVO);
                usuarioRepository.save(admin);
                log.info("Usuário mestre criado: {} (senha temporária)", adminEmail);
            } else {
                log.info("Usuário mestre já existe: {}", adminEmail);
            }

            // Migrar doutores retroativos que não tem usuario atrelado
            List<Doutor> doutores = doutorRepository.findAll();
            for (Doutor d : doutores) {
                if (d.getUsuario() == null) {
                    if (usuarioRepository.findByEmail(d.getEmail()) == null) {
                        Usuario u = new Usuario();
                        u.setNome(d.getNome());
                        u.setEmail(d.getEmail());
                        u.setSenha(passwordEncoder.encode(d.getCrm()));
                        u.setTipo(TipoUsuario.MEDICO);
                        u.setStatus(StatusUsuario.ATIVO);
                        
                        Usuario salvo = usuarioRepository.save(u);
                        d.setUsuario(salvo);
                        doutorRepository.save(d);
                        log.info("Usuário gerado para o Doutor retroativo: {}", d.getEmail());
                    }
                }
            }
        };
    }
}