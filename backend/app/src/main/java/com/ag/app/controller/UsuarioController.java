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

import com.ag.app.dto.usuario.UsuarioCreateDTO;
import com.ag.app.dto.usuario.UsuarioResponseDTO;
import com.ag.app.dto.usuario.UsuarioUpdateDTO;
import com.ag.app.model.StatusUsuario;
import com.ag.app.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public UsuarioResponseDTO verPorId(@PathVariable Long id) {
        return usuarioService.verPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UsuarioResponseDTO salvar(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        return usuarioService.salvar(usuarioCreateDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public UsuarioResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        return usuarioService.atualizar(id, usuarioUpdateDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UsuarioResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusUsuario status) {
        return usuarioService.atualizarStatus(id, status);
    }
}

