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

import com.ag.app.dto.funcionario.FuncionarioCreateDTO;
import com.ag.app.dto.funcionario.FuncionarioResponseDTO;
import com.ag.app.dto.funcionario.FuncionarioUpdateDTO;
import com.ag.app.model.StatusUsuario;
import com.ag.app.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "http://localhost:4200")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public FuncionarioResponseDTO verPorId(@PathVariable Long id) {
        return funcionarioService.verPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public FuncionarioResponseDTO salvar(@Valid @RequestBody FuncionarioCreateDTO funcionarioCreateDTO) {
        return funcionarioService.salvar(funcionarioCreateDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public FuncionarioResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody FuncionarioUpdateDTO funcionarioUpdateDTO) {
        return funcionarioService.atualizar(id, funcionarioUpdateDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public FuncionarioResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusUsuario status) {
        return funcionarioService.atualizarStatus(id, status);
    }
}
