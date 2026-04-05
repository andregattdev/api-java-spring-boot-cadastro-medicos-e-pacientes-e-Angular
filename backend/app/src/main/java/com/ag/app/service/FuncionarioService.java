package com.ag.app.service;

import com.ag.app.dto.funcionario.FuncionarioCreateDTO;
import com.ag.app.dto.funcionario.FuncionarioResponseDTO;
import com.ag.app.dto.funcionario.FuncionarioUpdateDTO;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Empresa;
import com.ag.app.model.Funcionario;
import com.ag.app.model.StatusUsuario;
import com.ag.app.repository.EmpresaRepository;
import com.ag.app.repository.FuncionarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FuncionarioService {

    private final EmpresaRepository empresaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              EmpresaRepository empresaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.empresaRepository = empresaRepository;
    }

    // Converte entidade -> ResponseDTO
    private FuncionarioResponseDTO toResponseDTO(Funcionario funcionario) {
        if (funcionario == null) {
            return null;
        }

        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(funcionario.getId());
        dto.setNome(funcionario.getNome());
        dto.setEmail(funcionario.getEmail());
        dto.setTelefone(funcionario.getTelefone());
        dto.setCargo(funcionario.getCargo());

        // Se status for enum, converte para String com segurança
        dto.setStatus(funcionario.getStatus() != null ? funcionario.getStatus().name() : null);

        // Popula dados da empresa vinculada
        if (funcionario.getEmpresa() != null) {
            dto.setEmpresaId(funcionario.getEmpresa().getId());
            dto.setEmpresaNome(funcionario.getEmpresa().getNome());
        }

        // Popula dados do usuário vinculado (se quiser mostrar no DTO)
        if (funcionario.getUsuario() != null) {
            dto.setUsuarioId(funcionario.getUsuario().getId());
        }

        return dto;
    }

    public List<FuncionarioResponseDTO> listarTodos() {
        log.info("Listando todos os funcionários");
        try {
            List<FuncionarioResponseDTO> funcionarios = funcionarioRepository.findAll()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            log.debug("Total de funcionários encontrados: {}", funcionarios.size());
            return funcionarios;
        } catch (Exception e) {
            log.error("Erro ao listar funcionários", e);
            throw e;
        }
    }

    public FuncionarioResponseDTO verPorId(Long id) {
        log.info("Buscando funcionário com ID: {}", id);
        try {
            Funcionario funcionario = funcionarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));
            log.debug("Funcionário encontrado: {}", funcionario.getNome());
            return toResponseDTO(funcionario);
        } catch (ResourceNotFoundException e) {
            log.warn("Funcionário com ID {} não encontrado", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar funcionário com ID {}", id, e);
            throw e;
        }
    }

    @Transactional
    public FuncionarioResponseDTO salvar(FuncionarioCreateDTO funcionarioCreateDTO) {
        log.info("Salvando novo funcionário: {}", funcionarioCreateDTO.getNome());
        try {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(funcionarioCreateDTO.getNome());
            funcionario.setEmail(funcionarioCreateDTO.getEmail());
            funcionario.setTelefone(funcionarioCreateDTO.getTelefone());
            funcionario.setCargo(funcionarioCreateDTO.getCargo());
            funcionario.setStatus(StatusUsuario.ATIVO);

            if (funcionarioCreateDTO.getEmpresaId() != null) {
                log.debug("Vinculando funcionário à empresa ID: {}", funcionarioCreateDTO.getEmpresaId());
                Empresa empresa = empresaRepository.findById(funcionarioCreateDTO.getEmpresaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", funcionarioCreateDTO.getEmpresaId()));
                funcionario.setEmpresa(empresa);
            }

            Funcionario salvo = funcionarioRepository.save(funcionario);
            log.info("Funcionário salvo com ID: {}", salvo.getId());
            return toResponseDTO(salvo);
        } catch (ResourceNotFoundException e) {
            log.warn("Erro ao salvar funcionário: recurso não encontrado");
            throw e;
        } catch (Exception e) {
            log.error("Erro ao salvar funcionário: {}", funcionarioCreateDTO.getNome(), e);
            throw e;
        }
    }

    @Transactional
    public FuncionarioResponseDTO atualizarStatus(Long id, StatusUsuario status) {
        log.info("Atualizando status do funcionário ID {} para {}", id, status);
        try {
            Funcionario funcionario = funcionarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));
            log.debug("Funcionário encontrado: {}", funcionario.getNome());
            funcionario.setStatus(status);
            Funcionario atualizado = funcionarioRepository.save(funcionario);
            log.info("Status do funcionário ID {} atualizado com sucesso", id);
            return toResponseDTO(atualizado);
        } catch (ResourceNotFoundException e) {
            log.warn("Funcionário com ID {} não encontrado para atualizar status", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar status do funcionário ID {}", id, e);
            throw e;
        }
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioUpdateDTO funcionarioUpdateDTO) {
        log.info("Atualizando funcionário com ID: {}", id);
        try {
            Funcionario funcionario = funcionarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));

            log.debug("Funcionário encontrado: {}. Atualizando dados", funcionario.getNome());
            
            funcionario.setNome(funcionarioUpdateDTO.getNome());
            funcionario.setEmail(funcionarioUpdateDTO.getEmail());
            funcionario.setTelefone(funcionarioUpdateDTO.getTelefone());
            funcionario.setCargo(funcionarioUpdateDTO.getCargo());

            if (funcionarioUpdateDTO.getEmpresaId() != null) {
                log.debug("Vinculando funcionário à empresa ID: {}", funcionarioUpdateDTO.getEmpresaId());
                Empresa empresa = empresaRepository.findById(funcionarioUpdateDTO.getEmpresaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", funcionarioUpdateDTO.getEmpresaId()));
                funcionario.setEmpresa(empresa);
            }

            Funcionario atualizado = funcionarioRepository.save(funcionario);
            log.info("Funcionário ID {} atualizado com sucesso", id);
            return toResponseDTO(atualizado);
        } catch (ResourceNotFoundException e) {
            log.warn("Erro ao atualizar funcionário ID {}: recurso não encontrado", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar funcionário com ID {}", id, e);
            throw e;
        }
    }

    public List<FuncionarioResponseDTO> listarPorEmpresa(Long empresaId) {
        return funcionarioRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

}
