package com.ag.app.service;

import com.ag.app.dto.empresa.EmpresaCreateDTO;
import com.ag.app.dto.empresa.EmpresaResponseDTO;
import com.ag.app.dto.empresa.EmpresaUpdateDTO;
import com.ag.app.dto.funcionario.FuncionarioResponseDTO;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Empresa;
import com.ag.app.model.Funcionario;
import com.ag.app.model.StatusUsuario;
import com.ag.app.model.Usuario;
import com.ag.app.repository.EmpresaRepository;
import com.ag.app.repository.FuncionarioRepository;
import com.ag.app.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;

    public EmpresaService(EmpresaRepository empresaRepository,
                          FuncionarioRepository funcionarioRepository,
                          UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Converte entidade Empresa -> ResponseDTO
    private EmpresaResponseDTO toResponseDTO(Empresa empresa) {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setCnpj(empresa.getCnpj());
        dto.setEmail(empresa.getEmail());
        dto.setTelefone(empresa.getTelefone());
        dto.setStatus(empresa.getStatus().name());

        return dto;
    }

    // Converte entidade Funcionario -> ResponseDTO
    private FuncionarioResponseDTO toFuncionarioResponseDTO(Funcionario funcionario) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(funcionario.getId());
        dto.setNome(funcionario.getNome());
        dto.setEmail(funcionario.getEmail());
        dto.setTelefone(funcionario.getTelefone());
        dto.setCargo(funcionario.getCargo());
        dto.setStatus(funcionario.getStatus() != null ? funcionario.getStatus().name() : null);

        if (funcionario.getEmpresa() != null) {
            dto.setEmpresaId(funcionario.getEmpresa().getId());
            dto.setEmpresaNome(funcionario.getEmpresa().getNome());
        }

        if (funcionario.getUsuario() != null) {
            dto.setUsuarioId(funcionario.getUsuario().getId());
        }

        return dto;
    }

    // Listar todas as empresas
    public List<EmpresaResponseDTO> listarTodas() {
        log.info("Listando todas as empresas");
        try {
            List<EmpresaResponseDTO> empresas = empresaRepository.findAll()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            log.debug("Total de empresas encontradas: {}", empresas.size());
            return empresas;
        } catch (Exception e) {
            log.error("Erro ao listar empresas", e);
            throw e;
        }
    }

    // Ver empresa por ID
    public EmpresaResponseDTO verPorId(Long id) {
        log.info("Buscando empresa com ID: {}", id);
        try {
            Empresa empresa = empresaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
            log.debug("Empresa encontrada: {}", empresa.getNome());
            return toResponseDTO(empresa);
        } catch (ResourceNotFoundException e) {
            log.warn("Empresa com ID {} não encontrada", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar empresa com ID {}", id, e);
            throw e;
        }
    }

    // Salvar nova empresa
    @Transactional
    public EmpresaResponseDTO salvar(EmpresaCreateDTO empresaCreateDTO) {
        log.info("Salvando nova empresa: {}", empresaCreateDTO.getNome());
        try {
            Empresa empresa = new Empresa();
            empresa.setNome(empresaCreateDTO.getNome());
            empresa.setCnpj(empresaCreateDTO.getCnpj());
            empresa.setEmail(empresaCreateDTO.getEmail());
            empresa.setTelefone(empresaCreateDTO.getTelefone());
            empresa.setStatus(StatusUsuario.ATIVO);

            log.debug("Empresa criada com CNPJ: {}", empresa.getCnpj());
            Empresa salva = empresaRepository.save(empresa);
            log.info("Empresa salva com ID: {}", salva.getId());
            return toResponseDTO(salva);
        } catch (Exception e) {
            log.error("Erro ao salvar empresa: {}", empresaCreateDTO.getNome(), e);
            throw e;
        }
    }

    // Atualizar status da empresa
    @Transactional
    public EmpresaResponseDTO atualizarStatus(Long id, StatusUsuario status) {
        log.info("Atualizando status da empresa ID {} para {}", id, status);
        try {
            Empresa empresa = empresaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
            log.debug("Empresa encontrada: {}", empresa.getNome());
            empresa.setStatus(status);
            Empresa atualizada = empresaRepository.save(empresa);
            log.info("Status da empresa ID {} atualizado com sucesso", id);
            return toResponseDTO(atualizada);
        } catch (ResourceNotFoundException e) {
            log.warn("Empresa com ID {} não encontrada para atualizar status", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar status da empresa ID {}", id, e);
            throw e;
        }
    }

    // Atualizar dados da empresa
    @Transactional
    public EmpresaResponseDTO atualizar(Long id, EmpresaUpdateDTO empresaUpdateDTO) {
        log.info("Atualizando empresa com ID: {}", id);
        try {
            Empresa empresa = empresaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));

            log.debug("Empresa encontrada: {}. Atualizando dados", empresa.getNome());
            
            empresa.setNome(empresaUpdateDTO.getNome());
            empresa.setCnpj(empresaUpdateDTO.getCnpj());
            empresa.setEmail(empresaUpdateDTO.getEmail());
            empresa.setTelefone(empresaUpdateDTO.getTelefone());

            Empresa atualizada = empresaRepository.save(empresa);
            log.info("Empresa ID {} atualizada com sucesso", id);
            return toResponseDTO(atualizada);
        } catch (ResourceNotFoundException e) {
            log.warn("Empresa com ID {} não encontrada para atualização", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar empresa com ID {}", id, e);
            throw e;
        }
    }

    // Listar funcionários de uma empresa
    public List<FuncionarioResponseDTO> listarPorEmpresa(Long empresaId) {
        log.info("Listando funcionários da empresa ID: {}", empresaId);
        try {
            List<FuncionarioResponseDTO> funcionarios = funcionarioRepository.findByEmpresaId(empresaId)
                    .stream()
                    .map(this::toFuncionarioResponseDTO)
                    .collect(Collectors.toList());
            log.debug("Total de funcionários encontrados: {}", funcionarios.size());
            return funcionarios;
        } catch (Exception e) {
            log.error("Erro ao listar funcionários da empresa ID {}", empresaId, e);
            throw e;
        }
    }

    // Vincular usuário à empresa
    @Transactional
    public EmpresaResponseDTO vincularUsuario(Long empresaId, Long usuarioId) {
        log.info("Vinculando usuário ID {} à empresa ID {}", usuarioId, empresaId);
        try {
            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", empresaId));

            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", usuarioId));

            log.debug("Vinculando usuário {} à empresa {}", usuario.getNome(), empresa.getNome());
            
            usuario.setEmpresa(empresa);
            usuarioRepository.save(usuario);

            Empresa atualizada = empresaRepository.save(empresa);
            log.info("Usuário ID {} vinculado à empresa ID {} com sucesso", usuarioId, empresaId);
            return toResponseDTO(atualizada);
        } catch (ResourceNotFoundException e) {
            log.warn("Erro ao vincular usuário: recurso não encontrado");
            throw e;
        } catch (Exception e) {
            log.error("Erro ao vincular usuário ID {} à empresa ID {}", usuarioId, empresaId, e);
            throw e;
        }
    }
}