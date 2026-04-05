package com.ag.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ag.app.model.Particular;

public interface ParticularRepository extends JpaRepository<Particular, Long> {

    // Busca exata por nome
    List<Particular> findByNome(String nome);

    // Busca aproximada por nome (útil para nomes parciais)
    List<Particular> findByNomeContainingIgnoreCase(String nome);

    // Busca por CPF (geralmente único, então retorna um Optional ou o objeto direto)
    Optional<Particular> findByCpf(String cpf);

    // Busca por e-mail
    Optional<Particular> findByEmail(String email);

    // Busca por Nome OU CPF OU Email (caso queira um campo de busca única)
    List<Particular> findByNomeContainingOrCpfOrEmail(String nome, String cpf, String email);
    
    long count();
}
