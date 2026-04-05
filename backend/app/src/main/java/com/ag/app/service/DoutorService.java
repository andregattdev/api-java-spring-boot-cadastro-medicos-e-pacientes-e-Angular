package com.ag.app.service;

import com.ag.app.dto.doutor.DoutorCreateDTO;
import com.ag.app.dto.doutor.DoutorResponseDTO;
import com.ag.app.dto.doutor.DoutorUpdateDTO;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Doutor;
import com.ag.app.model.EspecialidadeMedica;
import com.ag.app.model.StatusUsuario;
import com.ag.app.repository.DoutorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoutorService {

    private final DoutorRepository doutorRepository;

    public DoutorService(DoutorRepository doutorRepository) {
        this.doutorRepository = doutorRepository;
    }

    // Converte entidade -> ResponseDTO
    private DoutorResponseDTO toResponseDTO(Doutor doutor) {
        DoutorResponseDTO dto = new DoutorResponseDTO();
        dto.setId(doutor.getId());
        dto.setNome(doutor.getNome());
        dto.setEmail(doutor.getEmail());
        dto.setTelefone(doutor.getTelefone());
        dto.setCrm(doutor.getCrm());
        dto.setEspecialidade(doutor.getEspecialidade() != null ? doutor.getEspecialidade().name() : null);
        dto.setStatus(doutor.getStatus() != null ? doutor.getStatus().name() : null);
        return dto;
    }

    public List<DoutorResponseDTO> listarTodos() {
        log.info("Listando todos os doutores");
        try {
            List<DoutorResponseDTO> doutores = doutorRepository.findAll()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            log.debug("Total de doutores encontrados: {}", doutores.size());
            return doutores;
        } catch (Exception e) {
            log.error("Erro ao listar doutores", e);
            throw e;
        }
    }

    public DoutorResponseDTO verPorId(Long id) {
        log.info("Buscando doutor com ID: {}", id);
        try {
            Doutor doutor = doutorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Doutor", "id", id));
            log.debug("Doutor encontrado: {}", doutor.getNome());
            return toResponseDTO(doutor);
        } catch (ResourceNotFoundException e) {
            log.warn("Doutor com ID {} não encontrado", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar doutor com ID {}", id, e);
            throw e;
        }
    }

    @Transactional
    public DoutorResponseDTO salvar(DoutorCreateDTO doutorCreateDTO) {
        log.info("Salvando novo doutor: {}", doutorCreateDTO.getNome());
        try {
            Doutor doutor = new Doutor();
            doutor.setNome(doutorCreateDTO.getNome());
            doutor.setEmail(doutorCreateDTO.getEmail());
            doutor.setTelefone(doutorCreateDTO.getTelefone());
            doutor.setCrm(doutorCreateDTO.getCrm());
            doutor.setEspecialidade(EspecialidadeMedica.valueOf(doutorCreateDTO.getEspecialidade().toUpperCase()));
            doutor.setStatus(StatusUsuario.ATIVO);

            log.debug("Doutor criado com CRM: {}", doutor.getCrm());
            Doutor salvo = doutorRepository.save(doutor);
            log.info("Doutor salvo com ID: {}", salvo.getId());
            return toResponseDTO(salvo);
        } catch (Exception e) {
            log.error("Erro ao salvar doutor: {}", doutorCreateDTO.getNome(), e);
            throw e;
        }
    }

    @Transactional
    public DoutorResponseDTO atualizar(Long id, DoutorUpdateDTO doutorUpdateDTO) {
        log.info("Atualizando doutor com ID: {}", id);
        try {
            Doutor doutor = doutorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Doutor", "id", id));

            log.debug("Doutor encontrado: {}. Atualizando dados", doutor.getNome());
            doutor.setNome(doutorUpdateDTO.getNome());
            doutor.setEmail(doutorUpdateDTO.getEmail());
            doutor.setTelefone(doutorUpdateDTO.getTelefone());
            doutor.setCrm(doutorUpdateDTO.getCrm());

            Doutor atualizado = doutorRepository.save(doutor);
            log.info("Doutor ID {} atualizado com sucesso", id);
            return toResponseDTO(atualizado);
        } catch (ResourceNotFoundException e) {
            log.warn("Doutor com ID {} não encontrado para atualização", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar doutor com ID {}", id, e);
            throw e;
        }
    }

    @Transactional
    public DoutorResponseDTO atualizarStatus(Long id, StatusUsuario status) {
        log.info("Atualizando status do doutor ID {} para {}", id, status);
        try {
            Doutor doutor = doutorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Doutor", "id", id));
            doutor.setStatus(status);
            Doutor atualizado = doutorRepository.save(doutor);
            log.info("Status do doutor atualizado com sucesso");
            return toResponseDTO(atualizado);
        } catch (ResourceNotFoundException e) {
            log.warn("Doutor com ID {} não encontrado", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar status do doutor ID {}", id, e);
            throw e;
        }
    }
}