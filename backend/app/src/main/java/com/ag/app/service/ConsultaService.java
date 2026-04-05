package com.ag.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ag.app.dto.consulta.ConsultaCreateDTO;
import com.ag.app.dto.consulta.ConsultaResponseDTO;
import com.ag.app.dto.consulta.ConsultaUpdateDTO;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Consulta;
import com.ag.app.model.Doutor;
import com.ag.app.model.Paciente;
import com.ag.app.repository.ConsultaRepository;
import com.ag.app.repository.DoutorRepository;
import com.ag.app.repository.PacienteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final DoutorRepository doutorRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
                           DoutorRepository doutorRepository,
                           PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.doutorRepository = doutorRepository;
        this.pacienteRepository = pacienteRepository;
    }

    // Converte entidade -> ResponseDTO
    private ConsultaResponseDTO toResponseDTO(Consulta consulta) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        dto.setId(consulta.getId());
        dto.setDataHora(consulta.getDataHora());
        dto.setObservacoes(consulta.getObservacoes());

        Doutor doutor = consulta.getDoutor();
        if (doutor != null) {
            dto.setDoutorId(doutor.getId());
            dto.setDoutorNome(doutor.getNome());
        }

        Paciente paciente = consulta.getPaciente();
        if (paciente != null) {
            dto.setPacienteId(paciente.getId());
            dto.setPacienteNome(paciente.getNome());
        }

        return dto;
    }

    public List<ConsultaResponseDTO> listarTodas() {
        log.info("Listando todas as consultas");
        return consultaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ConsultaResponseDTO verPorId(Long id) {
        log.info("Buscando consulta com ID: {}", id);
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "id", id));
        return toResponseDTO(consulta);
    }

    @Transactional
    public ConsultaResponseDTO salvar(ConsultaCreateDTO consultaCreateDTO) {
        Doutor doutor = doutorRepository.findById(consultaCreateDTO.getDoutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doutor", "id", consultaCreateDTO.getDoutorId()));
        Paciente paciente = pacienteRepository.findById(consultaCreateDTO.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", consultaCreateDTO.getPacienteId()));

        Consulta consulta = new Consulta();
        consulta.setDataHora(consultaCreateDTO.getDataHora());
        consulta.setObservacoes(consultaCreateDTO.getObservacoes());
        consulta.setDoutor(doutor);
        consulta.setPaciente(paciente);

        Consulta salva = consultaRepository.save(consulta);
        return toResponseDTO(salva);
    }

    @Transactional
    public ConsultaResponseDTO atualizar(Long id, ConsultaUpdateDTO consultaUpdateDTO) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "id", id));

        Doutor doutor = doutorRepository.findById(consultaUpdateDTO.getDoutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doutor", "id", consultaUpdateDTO.getDoutorId()));
        Paciente paciente = pacienteRepository.findById(consultaUpdateDTO.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", consultaUpdateDTO.getPacienteId()));

        consulta.setDataHora(consultaUpdateDTO.getDataHora());
        consulta.setObservacoes(consultaUpdateDTO.getObservacoes());
        consulta.setDoutor(doutor);
        consulta.setPaciente(paciente);

        Consulta atualizada = consultaRepository.save(consulta);
        return toResponseDTO(atualizada);
    }

    // 🔹 Listar consultas por período (ordenadas por horário)
    public List<ConsultaResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByDataHoraBetweenOrderByDataHoraAsc(inicio, fim)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Listar consultas de um dia específico
    public List<ConsultaResponseDTO> listarPorDia(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return listarPorPeriodo(start, end);
    }

    // 🔹 Listar consultas de uma semana (entre duas datas)
    public List<ConsultaResponseDTO> listarPorSemana(LocalDate start, LocalDate end) {
        return listarPorPeriodo(start.atStartOfDay(), end.atTime(23, 59, 59));
    }

    // 🔹 Listar consultas de um mês específico
    public List<ConsultaResponseDTO> listarPorMes(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return listarPorPeriodo(start.atStartOfDay(), end.atTime(23, 59, 59));
    }
}
