package com.ag.app.service;

import com.ag.app.dto.usuario.UsuarioCreateDTO;
import com.ag.app.dto.usuario.UsuarioResponseDTO;
import com.ag.app.dto.usuario.UsuarioUpdateDTO;
import com.ag.app.exception.ResourceNotFoundException;
import com.ag.app.model.Doutor;
import com.ag.app.model.Paciente;
import com.ag.app.model.Usuario;
import com.ag.app.model.StatusUsuario;
import com.ag.app.model.Funcionario;
import com.ag.app.model.TipoUsuario;
import com.ag.app.model.Empresa;
import com.ag.app.repository.DoutorRepository;
import com.ag.app.repository.EmpresaRepository;
import com.ag.app.repository.FuncionarioRepository;
import com.ag.app.repository.PacienteRepository;
import com.ag.app.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DoutorRepository doutorRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          DoutorRepository doutorRepository,
                          PacienteRepository pacienteRepository,
                          FuncionarioRepository funcionarioRepository,
                          EmpresaRepository empresaRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.doutorRepository = doutorRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTipo(usuario.getTipo().name());
        dto.setStatus(usuario.getStatus().name());
        if (usuario.getEmpresa() != null) {
            dto.setEmpresaId(usuario.getEmpresa().getId());
            dto.setEmpresaNome(usuario.getEmpresa().getNome());
        }
        return dto;
    }

    public List<UsuarioResponseDTO> listarTodos() {
        log.debug("Iniciando listagem de todos os usuários");
        List<UsuarioResponseDTO> usuarios = usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Listagem concluída. Total: {} usuários", usuarios.size());
        return usuarios;
    }

    public long count() {
        log.debug("Contando total de usuários");
        return usuarioRepository.count();
    }

    public UsuarioResponseDTO verPorId(Long id) {
        log.debug("Buscando usuário com ID: {}", id);
        UsuarioResponseDTO usuario = usuarioRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado", id);
                    return new ResourceNotFoundException("Usuário", "id", id);
                });
        log.info("Usuário encontrado: ID: {}", id);
        return usuario;
    }

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioCreateDTO usuarioCreateDTO) {
        log.info("Criando novo usuário: {} (Tipo: {})", usuarioCreateDTO.getNome(), usuarioCreateDTO.getTipo());
        if (usuarioRepository.findByEmail(usuarioCreateDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email já cadastrado: " + usuarioCreateDTO.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCreateDTO.getNome());
        usuario.setEmail(usuarioCreateDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioCreateDTO.getSenha()));
        usuario.setTipo(TipoUsuario.valueOf(usuarioCreateDTO.getTipo()));
        usuario.setStatus(StatusUsuario.ATIVO);

        if (usuarioCreateDTO.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(usuarioCreateDTO.getEmpresaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", usuarioCreateDTO.getEmpresaId()));
            usuario.setEmpresa(empresa);
        }

        Usuario salvo = usuarioRepository.save(usuario);
        log.debug("Usuário salvo no banco com ID: {}", salvo.getId());

        // Criar entidades relacionadas conforme o tipo
        switch (usuario.getTipo()) {
            case MEDICO -> {
                log.debug("Criando registro de Doutor para usuário ID: {}", salvo.getId());
                Doutor doutor = new Doutor();
                doutor.setNome(usuario.getNome());
                doutor.setEmail(usuario.getEmail());
                doutor.setStatus(usuario.getStatus());
                doutor.setUsuario(salvo);
                doutorRepository.save(doutor);
                log.debug("Doutor criado");
            }
            case PACIENTE -> {
                log.debug("Criando registro de Paciente para usuário ID: {}", salvo.getId());
                Paciente paciente = new Paciente();
                paciente.setNome(usuario.getNome());
                paciente.setEmail(usuario.getEmail());
                paciente.setStatus(usuario.getStatus());
                paciente.setUsuario(salvo);
                pacienteRepository.save(paciente);
                log.debug("Paciente criado");
            }
            case FUNCIONARIO -> {
                log.debug("Criando registro de Funcionário para usuário ID: {}", salvo.getId());
                Funcionario funcionario = new Funcionario();
                funcionario.setNome(usuario.getNome());
                funcionario.setEmail(usuario.getEmail());
                funcionario.setStatus(usuario.getStatus());
                funcionario.setUsuario(salvo);
                if (usuarioCreateDTO.getEmpresaId() != null) {
                    Empresa empresa = empresaRepository.findById(usuarioCreateDTO.getEmpresaId())
                            .orElseThrow(() -> {
                                log.warn("Empresa com ID {} não encontrada", usuarioCreateDTO.getEmpresaId());
                                return new ResourceNotFoundException("Empresa", "id", usuarioCreateDTO.getEmpresaId());
                            });
                    funcionario.setEmpresa(empresa);
                    log.debug("Funcionário vinculado à empresa ID: {}", empresa.getId());
                }
                funcionarioRepository.save(funcionario);
                log.debug("Funcionário criado");
            }
            case ADMINISTRADOR, EMPRESA_CONVENIO, PARTICULAR -> {
                // Estes tipos não requerem entidades relacionadas adicionais
                log.debug("Tipo {} não requer entidades relacionadas", usuario.getTipo());
            }
        }

        log.info("Usuário criado com sucesso: {} (ID: {})", usuario.getNome(), salvo.getId());
        return toResponseDTO(salvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {
        log.info("Atualizando usuário com ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado", id);
                    return new ResourceNotFoundException("Usuário", "id", id);
                });
        
        log.debug("Usuário encontrado: {}. Atualizando dados", usuario.getNome());
        usuario.setNome(usuarioUpdateDTO.getNome());
        usuario.setEmail(usuarioUpdateDTO.getEmail());
        
        Usuario atualizado = usuarioRepository.save(usuario);
        log.info("Usuário ID {} atualizado com sucesso", id);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public UsuarioResponseDTO atualizarStatus(Long id, StatusUsuario status) {
        log.info("Atualizando status do usuário ID: {} para {}", id, status);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado para atualização", id);
                    return new ResourceNotFoundException("Usuário", "id", id);
                });
        usuario.setStatus(status);
        Usuario atualizado = usuarioRepository.save(usuario);
        log.info("Status atualizado com sucesso para usuário ID: {}", id);
        return toResponseDTO(atualizado);
    }
}