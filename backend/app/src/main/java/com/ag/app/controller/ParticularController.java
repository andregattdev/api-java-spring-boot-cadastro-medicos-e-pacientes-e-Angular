package com.ag.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ag.app.dto.particular.ParticularCreateDTO;
import com.ag.app.dto.particular.ParticularResponseDTO;
import com.ag.app.dto.particular.ParticularUpdateDTO;
import com.ag.app.model.StatusUsuario;
import com.ag.app.service.ParticularService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/particulares")
@CrossOrigin(origins = "http://localhost:4200")
public class ParticularController {

    private final ParticularService particularService;

    public ParticularController(ParticularService particularService) {
        this.particularService = particularService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public ResponseEntity<List<ParticularResponseDTO>> listarTodos() {
        return ResponseEntity.ok(particularService.listarTodos());
    }

    @GetMapping("/nome")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public ResponseEntity<List<ParticularResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(particularService.buscarPorNome(nome));
    }

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('PARTICULAR')")
    public ResponseEntity<ParticularResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return particularService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('PARTICULAR')")
    public ResponseEntity<ParticularResponseDTO> buscarPorEmail(@PathVariable String email) {
        return particularService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public ResponseEntity<?> salvar(@Valid @RequestBody ParticularCreateDTO dto) {
        try {
            ParticularResponseDTO novoParticular = particularService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoParticular);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('PARTICULAR')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ParticularUpdateDTO dto) {
        try {
            ParticularResponseDTO atualizado = particularService.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            particularService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public ResponseEntity<?> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusUsuario novoStatus) {
        try {
            ParticularResponseDTO atualizado = particularService.alterarStatus(id, novoStatus);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
