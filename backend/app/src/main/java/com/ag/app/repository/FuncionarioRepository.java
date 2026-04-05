package com.ag.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ag.app.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Funcionario findByEmail(String email);
    Funcionario findByCargo(String cargo);
    Funcionario findByNome(String nome);
    List<Funcionario> findByEmpresaId(Long empresaId);
    long count();


}
