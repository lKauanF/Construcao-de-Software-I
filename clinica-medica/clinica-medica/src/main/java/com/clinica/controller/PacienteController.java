package com.clinica.controller;

import com.clinica.entity.FichaMedica;
import com.clinica.entity.Paciente;
import com.clinica.enums.TipoSanguineo;
import com.clinica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Paciente criar(@RequestBody @Valid Paciente paciente) {
        return pacienteService.criar(paciente);
    }

    @GetMapping
    public List<Paciente> listarTodos() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    @GetMapping("/{id}/completo")
    public Paciente buscarPorIdCompleto(@PathVariable Long id) {
        return pacienteService.buscarPorIdCompleto(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Paciente buscarPorCpf(@PathVariable String cpf) {
        return pacienteService.buscarPorCpf(cpf);
    }

    @PutMapping("/{id}")
    public Paciente atualizar(@PathVariable Long id, @RequestBody @Valid Paciente paciente) {
        return pacienteService.atualizar(id, paciente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        pacienteService.inativar(id);
    }

    @PostMapping("/{id}/ficha")
    @ResponseStatus(HttpStatus.CREATED)
    public FichaMedica adicionarFicha(@PathVariable Long id, @RequestBody @Valid FichaMedica ficha) {
        return pacienteService.adicionarFicha(id, ficha);
    }

    @GetMapping("/{id}/ficha")
    public FichaMedica buscarFicha(@PathVariable Long id) {
        return pacienteService.buscarFicha(id);
    }

    @PutMapping("/{id}/ficha")
    public FichaMedica atualizarFicha(@PathVariable Long id, @RequestBody @Valid FichaMedica ficha) {
        return pacienteService.atualizarFicha(id, ficha);
    }

    @DeleteMapping("/{id}/ficha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerFicha(@PathVariable Long id) {
        pacienteService.removerFicha(id);
    }

    @GetMapping("/buscar")
    public List<Paciente> buscarPorTermo(@RequestParam String termo) {
        return pacienteService.buscarPorTermo(termo);
    }

    @GetMapping("/com-ficha")
    public List<Paciente> listarComFicha() {
        return pacienteService.listarComFicha();
    }

    @GetMapping("/sem-ficha")
    public List<Paciente> listarSemFicha() {
        return pacienteService.listarSemFicha();
    }

    @GetMapping("/tipo-sanguineo/{tipo}")
    public List<Paciente> buscarPorTipoSanguineo(@PathVariable TipoSanguineo tipo) {
        return pacienteService.buscarPorTipoSanguineo(tipo);
    }

    @PatchMapping("/{id}/ativar")
    public Paciente ativar(@PathVariable Long id) {
        return pacienteService.ativar(id);
    }

    @PatchMapping("/{id}/inativar")
    public Paciente inativarViaPatch(@PathVariable Long id) {
        pacienteService.inativar(id);
        return pacienteService.buscarPorId(id);
    }

    @PatchMapping("/{id}/suspender")
    public Paciente suspender(@PathVariable Long id) {
        return pacienteService.suspender(id);
    }
}