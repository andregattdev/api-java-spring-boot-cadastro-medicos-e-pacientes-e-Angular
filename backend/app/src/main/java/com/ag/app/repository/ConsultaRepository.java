package com.ag.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ag.app.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    List<Consulta> findByDoutorId(Long doutorId);
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByEmpresaId(Long empresaId);

    // Buscar consultas entre dois horários
    List<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    

    List<Consulta> findByDoutorIdAndDataHoraBetweenOrderByDataHoraAsc(Long doutorId, LocalDateTime inicio, LocalDateTime fim);

    // Contagem total
    long count();

    // Contagem entre dois horários (dia, semana, mês)
    long countByDataHoraBetween(LocalDateTime start, LocalDateTime end);

    List<Consulta> findByDataHoraBetweenOrderByDataHoraAsc(LocalDateTime inicio, LocalDateTime fim);

}
