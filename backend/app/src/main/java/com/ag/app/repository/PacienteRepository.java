package com.ag.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ag.app.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByCpf(String cpf);
    Paciente findByEmail(String email);
    Paciente findByNome(String nome);
    List<Paciente> findByEmpresaId(Long empresaId);
    long count();

}
