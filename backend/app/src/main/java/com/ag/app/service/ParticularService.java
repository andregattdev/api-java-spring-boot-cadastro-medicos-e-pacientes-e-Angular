package com.ag.app.service;

import com.ag.app.dto.particular.ParticularCreateDTO;
import com.ag.app.dto.particular.ParticularResponseDTO;
import com.ag.app.dto.particular.ParticularUpdateDTO;
import com.ag.app.exception.BusinessException;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Particular;
import com.ag.app.model.StatusUsuario;
import com.ag.app.repository.ParticularRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParticularService {

    private final ParticularRepository particularRepository;

    public ParticularService(ParticularRepository particularRepository) {
        this.particularRepository = particularRepository;
    }

    public List<ParticularResponseDTO> listarTodos() {
        log.info("Listando todos os particulares");
        try {
            List<ParticularResponseDTO> particulares = particularRepository.findAll().stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            log.debug("Total de particulares encontrados: {}", particulares.size());
            return particulares;
        } catch (Exception e) {
            log.error("Erro ao listar particulares", e);
            throw e;
        }
    }

    public List<ParticularResponseDTO> buscarPorNome(String nome) {
        log.info("Buscando particulares por nome: {}", nome);
        try {
            List<ParticularResponseDTO> particulares = particularRepository.findByNomeContainingIgnoreCase(nome).stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            log.debug("Total de particulares encontrados com nome: {}", particulares.size());
            return particulares;
        } catch (Exception e) {
            log.error("Erro ao buscar particulares por nome: {}", nome, e);
            throw e;
        }
    }

    public Optional<ParticularResponseDTO> buscarPorCpf(String cpf) {
        log.info("Buscando particular por CPF: {}", cpf);
        try {
            Optional<ParticularResponseDTO> particular = particularRepository.findByCpf(cpf).map(this::toResponseDTO);
            if (particular.isPresent()) {
                log.debug("Particular encontrado com CPF: {}", cpf);
            } else {
                log.warn("Nenhum particular encontrado com CPF: {}", cpf);
            }
            return particular;
        } catch (Exception e) {
            log.error("Erro ao buscar particular por CPF: {}", cpf, e);
            throw e;
        }
    }

    public Optional<ParticularResponseDTO> buscarPorEmail(String email) {
        log.info("Buscando particular por email: {}", email);
        try {
            Optional<ParticularResponseDTO> particular = particularRepository.findByEmail(email).map(this::toResponseDTO);
            if (particular.isPresent()) {
                log.debug("Particular encontrado com email: {}", email);
            } else {
                log.warn("Nenhum particular encontrado com email: {}", email);
            }
            return particular;
        } catch (Exception e) {
            log.error("Erro ao buscar particular por email: {}", email, e);
            throw e;
        }
    }

    @Transactional
    public ParticularResponseDTO salvar(ParticularCreateDTO dto) {
        log.info("Salvando novo particular: {}", dto.getNome());
        try {
            // Validação: impede CPFs duplicados
            particularRepository.findByCpf(dto.getCpf()).ifPresent(p -> {
                log.warn("Tentativa de salvar particular com CPF duplicado: {}", dto.getCpf());
                throw new BusinessException("CPF_DUPLICADO", "O CPF informado já está cadastrado.");
            });

            log.debug("CPF validado. Criando novo particular");
            Particular particular = new Particular();
            particular.setNome(dto.getNome());
            particular.setCpf(dto.getCpf());
            particular.setEmail(dto.getEmail());
            particular.setEndereco(dto.getEndereco());
            particular.setStatus(StatusUsuario.ATIVO); // Define status inicial

            Particular salvo = particularRepository.save(particular);
            log.info("Particular salvo com ID: {}", salvo.getId());
            return toResponseDTO(salvo);
        } catch (BusinessException e) {
            log.error("Erro de negócio ao salvar particular", e);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao salvar particular: {}", dto.getNome(), e);
            throw e;
        }
    }

    @Transactional
    public ParticularResponseDTO atualizar(Long id, ParticularUpdateDTO dto) {
        log.info("Atualizando particular com ID: {}", id);
        try {
            return particularRepository.findById(id).map(particularExistente -> {
                log.debug("Particular encontrado: {}. Atualizando dados", particularExistente.getNome());
                particularExistente.setNome(dto.getNome());
                particularExistente.setEmail(dto.getEmail());
                particularExistente.setEndereco(dto.getEndereco());

                Particular atualizado = particularRepository.save(particularExistente);
                log.info("Particular ID {} atualizado com sucesso", id);
                return toResponseDTO(atualizado);
            }).orElseThrow(() -> new ResourceNotFoundException("Particular", "id", id));
        } catch (ResourceNotFoundException e) {
            log.warn("Particular com ID {} não encontrado para atualização", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar particular com ID {}", id, e);
            throw e;
        }
    }

    @Transactional
    public void excluir(Long id) {
        log.info("Excluindo particular com ID: {}", id);
        try {
            if (!particularRepository.existsById(id)) {
                log.warn("Particular com ID {} não encontrado para exclusão", id);
                throw new ResourceNotFoundException("Particular", "id", id);
            }
            particularRepository.deleteById(id);
            log.info("Particular ID {} excluído com sucesso", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao excluir particular com ID {}", id, e);
            throw e;
        }
    }

    @Transactional
    public ParticularResponseDTO alterarStatus(Long id, StatusUsuario novoStatus) {
        log.info("Alterando status do particular ID {} para {}", id, novoStatus);
        try {
            return particularRepository.findById(id).map(particular -> {
                log.debug("Particular encontrado: {}", particular.getNome());
                particular.setStatus(novoStatus);
                Particular atualizado = particularRepository.save(particular);
                log.info("Status do particular ID {} alterado para {} com sucesso", id, novoStatus);
                return toResponseDTO(atualizado);
            }).orElseThrow(() -> new ResourceNotFoundException("Particular", "id", id));
        } catch (ResourceNotFoundException e) {
            log.warn("Particular com ID {} não encontrado para alterar status", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao alterar status do particular com ID {}", id, e);
            throw e;
        }
    }

    // --- MAPPERS ---

    private ParticularResponseDTO toResponseDTO(Particular particular) {
        ParticularResponseDTO dto = new ParticularResponseDTO();
        dto.setId(particular.getId());
        dto.setNome(particular.getNome());
        dto.setCpf(particular.getCpf());
        dto.setEmail(particular.getEmail());
        dto.setEndereco(particular.getEndereco());
        dto.setStatus(particular.getStatus() != null ? particular.getStatus().name() : null);
        return dto;
    }
}