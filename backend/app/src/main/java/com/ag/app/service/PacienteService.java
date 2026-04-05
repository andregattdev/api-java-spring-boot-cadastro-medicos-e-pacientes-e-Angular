package com.ag.app.service;

import com.ag.app.dto.paciente.PacienteCreateDTO;
import com.ag.app.dto.paciente.PacienteResponseDTO;
import com.ag.app.dto.paciente.PacienteUpdateDTO;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Paciente;
import com.ag.app.model.StatusUsuario;
import com.ag.app.repository.EmpresaRepository;
import com.ag.app.repository.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final EmpresaRepository empresaRepository;

    public PacienteService(PacienteRepository pacienteRepository, EmpresaRepository empresaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.empresaRepository = empresaRepository;
    }

    // Converte entidade -> ResponseDTO
    private PacienteResponseDTO toResponseDTO(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setEmail(paciente.getEmail());
        dto.setTelefone(paciente.getTelefone());
        dto.setCpf(paciente.getCpf());
        dto.setStatus(paciente.getStatus().name());

        if (paciente.getEmpresa() != null) {
            dto.setEmpresaId(paciente.getEmpresa().getId());
            dto.setEmpresaNome(paciente.getEmpresa().getNome());
        }

        return dto;
    }

    public List<PacienteResponseDTO> listarTodos() {
        log.debug("Iniciando listagem de todos os pacientes");
        List<PacienteResponseDTO> pacientes = pacienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Listagem concluída. Total: {} pacientes", pacientes.size());
        return pacientes;
    }

    public PacienteResponseDTO verPorId(Long id) {
        log.debug("Buscando paciente com ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Paciente com ID {} não encontrado", id);
                    return new ResourceNotFoundException("Paciente", "id", id);
                });
        log.info("Paciente encontrado: {} (ID: {})", paciente.getNome(), id);
        return toResponseDTO(paciente);
    }

    @Transactional
    public PacienteResponseDTO salvar(PacienteCreateDTO pacienteCreateDTO) {
        log.info("Criando novo paciente: {} (CPF: {})", pacienteCreateDTO.getNome(), pacienteCreateDTO.getCpf());
        Paciente paciente = new Paciente();
        paciente.setNome(pacienteCreateDTO.getNome());
        paciente.setEmail(pacienteCreateDTO.getEmail());
        paciente.setTelefone(pacienteCreateDTO.getTelefone());
        paciente.setCpf(pacienteCreateDTO.getCpf());
        paciente.setStatus(StatusUsuario.ATIVO);

        if (pacienteCreateDTO.getEmpresaId() != null) {
            paciente.setEmpresa(empresaRepository.findById(pacienteCreateDTO.getEmpresaId())
                    .orElseThrow(() -> {
                        log.warn("Empresa com ID {} não encontrada", pacienteCreateDTO.getEmpresaId());
                        return new ResourceNotFoundException("Empresa", "id", pacienteCreateDTO.getEmpresaId());
                    }));
        }

        Paciente salvo = pacienteRepository.save(paciente);
        log.info("Paciente criado com sucesso: {} (ID: {})", paciente.getNome(), salvo.getId());
        return toResponseDTO(salvo);
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteUpdateDTO pacienteUpdateDTO) {
        log.info("Atualizando paciente com ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Paciente com ID {} não encontrado para atualização", id);
                    return new ResourceNotFoundException("Paciente", "id", id);
                });

        log.debug("Paciente encontrado: {}. Atualizando dados", paciente.getNome());
        paciente.setNome(pacienteUpdateDTO.getNome());
        paciente.setEmail(pacienteUpdateDTO.getEmail());
        paciente.setTelefone(pacienteUpdateDTO.getTelefone());

        if (pacienteUpdateDTO.getEmpresaId() != null) {
            paciente.setEmpresa(empresaRepository.findById(pacienteUpdateDTO.getEmpresaId())
                    .orElseThrow(() -> {
                        log.warn("Empresa com ID {} não encontrada", pacienteUpdateDTO.getEmpresaId());
                        return new ResourceNotFoundException("Empresa", "id", pacienteUpdateDTO.getEmpresaId());
                    }));
        }

        Paciente atualizado = pacienteRepository.save(paciente);
        log.info("Paciente ID {} atualizado com sucesso", id);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public PacienteResponseDTO atualizarStatus(Long id, StatusUsuario status) {
        log.info("Atualizando status do paciente ID: {} para {}", id, status);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Paciente com ID {} não encontrado", id);
                    return new ResourceNotFoundException("Paciente", "id", id);
                });
        paciente.setStatus(status);
        Paciente atualizado = pacienteRepository.save(paciente);
        log.info("Status atualizado com sucesso para paciente ID: {}", id);
        return toResponseDTO(atualizado);
    }
}