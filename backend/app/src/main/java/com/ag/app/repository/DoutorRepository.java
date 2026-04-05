package com.ag.app.repository;

import com.ag.app.model.Doutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoutorRepository extends JpaRepository<Doutor, Long> {
    Doutor findByCrm(String crm);
    Doutor findByEmail(String email);
    Doutor findByNome(String nome);
    long count();
}


