package com.ag.app.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.ag.app.dto.paciente.PacienteCreateDTO;
import com.ag.app.dto.paciente.PacienteResponseDTO;
import com.ag.app.dto.paciente.PacienteUpdateDTO;
import com.ag.app.model.StatusUsuario;
import com.ag.app.repository.PacienteRepository;
import com.ag.app.service.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:4200")
public class PacienteController {

    private final PacienteService pacienteService;
    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteService pacienteService, PacienteRepository pacienteRepository) {
        this.pacienteService = pacienteService;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('EMPRESA_CONVENIO')")
    public List<PacienteResponseDTO> listarTodos() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public PacienteResponseDTO verMeuPerfil(Authentication authentication) {
        return pacienteService.verPorEmail(authentication.getName());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public PacienteResponseDTO atualizarMeuPerfil(Authentication authentication, @Valid @RequestBody PacienteUpdateDTO dto) {
        return pacienteService.atualizarPorEmail(authentication.getName(), dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('PACIENTE') or hasRole('EMPRESA_CONVENIO')")
    public PacienteResponseDTO verPorId(@PathVariable Long id) {
        return pacienteService.verPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('EMPRESA_CONVENIO')")
    public PacienteResponseDTO salvar(@Valid @RequestBody PacienteCreateDTO pacienteCreateDTO) {
        return pacienteService.salvar(pacienteCreateDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('PACIENTE') or hasRole('EMPRESA_CONVENIO')")
    public PacienteResponseDTO atualizar(@PathVariable Long id,
            @Valid @RequestBody PacienteUpdateDTO pacienteUpdateDTO) {
        return pacienteService.atualizar(id, pacienteUpdateDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public PacienteResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusUsuario status) {
        return pacienteService.atualizarStatus(id, status);
    }

    @GetMapping("/count")
    public long countPacientes() {
        return pacienteRepository.count();
    }
}