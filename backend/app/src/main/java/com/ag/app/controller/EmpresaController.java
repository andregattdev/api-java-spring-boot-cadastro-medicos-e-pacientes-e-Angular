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

import com.ag.app.dto.empresa.EmpresaCreateDTO;
import com.ag.app.dto.empresa.EmpresaResponseDTO;
import com.ag.app.dto.empresa.EmpresaUpdateDTO;
import com.ag.app.dto.funcionario.FuncionarioResponseDTO;
import com.ag.app.model.StatusUsuario;
import com.ag.app.service.EmpresaService;
import com.ag.app.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "http://localhost:4200")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final FuncionarioService funcionarioService;

    public EmpresaController(EmpresaService empresaService, FuncionarioService funcionarioService) {
        this.empresaService = empresaService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public List<EmpresaResponseDTO> listarTodas() {
        return empresaService.listarTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public EmpresaResponseDTO verPorId(@PathVariable Long id) {
        return empresaService.verPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EmpresaResponseDTO salvar(@Valid @RequestBody EmpresaCreateDTO empresaCreateDTO) {
        return empresaService.salvar(empresaCreateDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public EmpresaResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaUpdateDTO empresaUpdateDTO) {
        return empresaService.atualizar(id, empresaUpdateDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EmpresaResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusUsuario status) {
        return empresaService.atualizarStatus(id, status);
    }

    @GetMapping("/{id}/funcionarios")
    public List<FuncionarioResponseDTO> listarFuncionariosPorEmpresa(@PathVariable Long id) {
        return funcionarioService.listarPorEmpresa(id);
    }

    @PutMapping("/{empresaId}/usuario/{usuarioId}")
    public EmpresaResponseDTO vincularUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId) {
        return empresaService.vincularUsuario(empresaId, usuarioId);
    }
}
