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
import com.ag.app.model.StatusConsulta;
import com.ag.app.model.Doutor;
import com.ag.app.model.Paciente;
import com.ag.app.repository.ConsultaRepository;
import com.ag.app.repository.DoutorRepository;
import com.ag.app.repository.PacienteRepository;

import com.ag.app.model.TipoConsulta;
import com.ag.app.model.TipoExameOcupacional;

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
        if (consulta.getStatus() != null) {
            dto.setStatus(consulta.getStatus().name());
        }
        if (consulta.getTipoConsulta() != null) {
            dto.setTipoConsulta(consulta.getTipoConsulta().name());
        }
        if (consulta.getTipoExameOcupacional() != null) {
            dto.setTipoExameOcupacional(consulta.getTipoExameOcupacional().name());
        }

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
    public ConsultaResponseDTO salvar(ConsultaCreateDTO consultaCreateDTO, String emailAutor, boolean isPacienteRole) {
        if (isPacienteRole) {
            Paciente pacienteAutenticado = pacienteRepository.findByEmail(emailAutor);
            if (pacienteAutenticado != null) {
                if (pacienteAutenticado.getCpf() == null || pacienteAutenticado.getCpf().isEmpty() ||
                    pacienteAutenticado.getTelefone() == null || pacienteAutenticado.getTelefone().isEmpty()) {
                    throw new IllegalStateException("Para agendar consultas, conclua os dados de CPF e Telefone do seu cadastro!");
                }
                consultaCreateDTO.setPacienteId(pacienteAutenticado.getId());
            }
        }
        
        Doutor doutor = doutorRepository.findById(consultaCreateDTO.getDoutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doutor", "id", consultaCreateDTO.getDoutorId()));
        Paciente paciente = pacienteRepository.findById(consultaCreateDTO.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "id", consultaCreateDTO.getPacienteId()));

        Consulta consulta = new Consulta();
        consulta.setDataHora(consultaCreateDTO.getDataHora());
        consulta.setObservacoes(consultaCreateDTO.getObservacoes());
        consulta.setDoutor(doutor);
        consulta.setPaciente(paciente);
        consulta.setStatus(StatusConsulta.PENDENTE);

        if (consultaCreateDTO.getTipoConsulta() != null && !consultaCreateDTO.getTipoConsulta().trim().isEmpty()) {
            consulta.setTipoConsulta(TipoConsulta.valueOf(consultaCreateDTO.getTipoConsulta()));
        }
        if (consultaCreateDTO.getTipoExameOcupacional() != null && !consultaCreateDTO.getTipoExameOcupacional().trim().isEmpty()) {
            consulta.setTipoExameOcupacional(TipoExameOcupacional.valueOf(consultaCreateDTO.getTipoExameOcupacional()));
        }

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
        
        if (consultaUpdateDTO.getTipoConsulta() != null && !consultaUpdateDTO.getTipoConsulta().trim().isEmpty()) {
            consulta.setTipoConsulta(TipoConsulta.valueOf(consultaUpdateDTO.getTipoConsulta()));
        }
        if (consultaUpdateDTO.getTipoExameOcupacional() != null && !consultaUpdateDTO.getTipoExameOcupacional().trim().isEmpty()) {
            consulta.setTipoExameOcupacional(TipoExameOcupacional.valueOf(consultaUpdateDTO.getTipoExameOcupacional()));
        }

        Consulta atualizada = consultaRepository.save(consulta);
        return toResponseDTO(atualizada);
    }

    @Transactional
    public ConsultaResponseDTO atualizarStatus(Long id, StatusConsulta novoStatus) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "id", id));
        consulta.setStatus(novoStatus);
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

    public List<ConsultaResponseDTO> listarPorPacienteEmail(String email) {
        Paciente paciente = pacienteRepository.findByEmail(email);
        if (paciente == null) {
            throw new ResourceNotFoundException("Paciente", "email", email);
        }
        return consultaRepository.findByPacienteId(paciente.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ConsultaResponseDTO> listarPorDoutorEmail(String email) {
        Doutor doutor = doutorRepository.findByEmail(email);
        if (doutor == null) {
            throw new ResourceNotFoundException("Doutor", "email", email);
        }
        return consultaRepository.findByDoutorId(doutor.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ConsultaResponseDTO> listarPorDoutorEmailEPeriodo(String email, LocalDateTime inicio, LocalDateTime fim) {
        Doutor doutor = doutorRepository.findByEmail(email);
        if (doutor == null) {
            throw new ResourceNotFoundException("Doutor", "email", email);
        }
        return consultaRepository.findByDoutorIdAndDataHoraBetweenOrderByDataHoraAsc(doutor.getId(), inicio, fim)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void desmarcar(Long id, String email, boolean isRolePaciente) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", "id", id));

        if (isRolePaciente) {
            Paciente paciente = pacienteRepository.findByEmail(email);
            if (paciente == null || !consulta.getPaciente().getId().equals(paciente.getId())) {
                throw new SecurityException("Acesso negado.");
            }
            if (LocalDateTime.now().plusHours(2).isAfter(consulta.getDataHora())) {
                throw new IllegalArgumentException("O cancelamento não é permitido pois falta menos de 2 horas para a consulta.");
            }
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }
}
