package com.ag.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ag.app.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Empresa findByCnpj(String cnpj);
    Empresa findByEmail(String email);
    long count();

}
