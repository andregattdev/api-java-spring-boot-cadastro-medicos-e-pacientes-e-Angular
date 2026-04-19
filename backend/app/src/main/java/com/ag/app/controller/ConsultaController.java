package com.ag.app.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.core.Authentication;

import com.ag.app.dto.consulta.ConsultaCreateDTO;
import com.ag.app.dto.consulta.ConsultaResponseDTO;
import com.ag.app.dto.consulta.ConsultaUpdateDTO;
import com.ag.app.model.StatusConsulta;
import com.ag.app.repository.ConsultaRepository;
import com.ag.app.service.ConsultaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final ConsultaRepository consultaRepository;

    public ConsultaController(ConsultaService consultaService, ConsultaRepository consultaRepository) {
        this.consultaService = consultaService;
        this.consultaRepository = consultaRepository;
    }

    // CRUD básico
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('PACIENTE')")
    public List<ConsultaResponseDTO> listarTodas(Authentication authentication) {
        boolean isPaciente = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
        boolean isMedico = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));
        
        if (isPaciente) {
            return consultaService.listarPorPacienteEmail(authentication.getName());
        }
        if (isMedico) {
            return consultaService.listarPorDoutorEmail(authentication.getName());
        }
        return consultaService.listarTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('PACIENTE')")
    public ConsultaResponseDTO verPorId(@PathVariable Long id) {
        return consultaService.verPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO') or hasRole('EMPRESA_CONVENIO') or hasRole('PACIENTE')")
    public ConsultaResponseDTO salvar(Authentication authentication, @Valid @RequestBody ConsultaCreateDTO consultaCreateDTO) {
        boolean isPaciente = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
        return consultaService.salvar(consultaCreateDTO, authentication.getName(), isPaciente);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO')")
    public ConsultaResponseDTO atualizar(@PathVariable Long id,
            @Valid @RequestBody ConsultaUpdateDTO consultaUpdateDTO) {
        return consultaService.atualizar(id, consultaUpdateDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('MEDICO')")
    public ConsultaResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusConsulta status) {
        return consultaService.atualizarStatus(id, status);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('FUNCIONARIO') or hasRole('PACIENTE')")
    public org.springframework.http.ResponseEntity<Void> desmarcar(@PathVariable Long id, Authentication authentication) {
        boolean isPaciente = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
        consultaService.desmarcar(id, authentication.getName(), isPaciente);
        return org.springframework.http.ResponseEntity.noContent().build();
    }

    // Contagens
    @GetMapping("/count")
    public long countConsultas() {
        return consultaRepository.count();
    }

    @GetMapping("/count/day/{date}")
    public long countByDay(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return consultaRepository.countByDataHoraBetween(start, end);
    }

    @GetMapping("/count/week")
    public long countByWeek(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return consultaRepository.countByDataHoraBetween(start.atStartOfDay(), end.atTime(23, 59, 59));
    }

    @GetMapping("/count/month/{year}/{month}")
    public long countByMonth(@PathVariable int year, @PathVariable int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return consultaRepository.countByDataHoraBetween(start.atStartOfDay(), end.atTime(23, 59, 59));
    }

    @GetMapping("/day/{date}")
    public List<ConsultaResponseDTO> consultasDoDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Authentication authentication) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        boolean isMedico = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));
        if (isMedico) {
             return consultaService.listarPorDoutorEmailEPeriodo(authentication.getName(), start, end);
        }

        return consultaService.listarPorPeriodo(start, end);
    }

    @GetMapping("/day/{date}/list")
    public List<ConsultaResponseDTO> consultasDoDiaList(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Authentication authentication) {
        return consultasDoDia(date, authentication);
    }

}
