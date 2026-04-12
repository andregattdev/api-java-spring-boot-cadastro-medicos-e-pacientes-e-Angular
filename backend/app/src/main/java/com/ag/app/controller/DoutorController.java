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

import com.ag.app.dto.doutor.DoutorCreateDTO;
import com.ag.app.dto.doutor.DoutorResponseDTO;
import com.ag.app.dto.doutor.DoutorUpdateDTO;
import com.ag.app.model.StatusUsuario;
import com.ag.app.repository.DoutorRepository;
import com.ag.app.service.DoutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doutores")
@CrossOrigin(origins = "http://localhost:4200")
public class DoutorController {

    private final DoutorService doutorService;
    private final DoutorRepository doutorRepository;

    public DoutorController(DoutorService doutorService, DoutorRepository doutorRepository) {
        this.doutorService = doutorService;
        this.doutorRepository = doutorRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('PACIENTE')")
    public List<DoutorResponseDTO> listarTodos() {
        return doutorService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('PACIENTE')")
    public DoutorResponseDTO verPorId(@PathVariable Long id) {
        return doutorService.verPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public DoutorResponseDTO salvar(@Valid @RequestBody DoutorCreateDTO doutorCreateDTO) {
        return doutorService.salvar(doutorCreateDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO')")
    public DoutorResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody DoutorUpdateDTO doutorUpdateDTO) {
        return doutorService.atualizar(id, doutorUpdateDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO')")
    public DoutorResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusUsuario status) {
        return doutorService.atualizarStatus(id, status);
    }

    @GetMapping("/count")
    public long countDoutores() {
        return doutorRepository.count();
    }
}
