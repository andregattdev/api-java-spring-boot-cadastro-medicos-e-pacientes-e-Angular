package com.ag.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ag.app.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    Usuario findByNome(String nome);
    
    long count();

}
