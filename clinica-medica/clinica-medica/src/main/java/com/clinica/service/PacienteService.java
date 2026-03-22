package com.clinica.service;

import com.clinica.entity.FichaMedica;
import com.clinica.entity.Paciente;
import com.clinica.enums.StatusPaciente;
import com.clinica.enums.TipoSanguineo;
import com.clinica.exception.RecursoNaoEncontradoException;
import com.clinica.exception.RegraNegocioException;
import com.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente criar(Paciente paciente) {
        if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
            throw new RegraNegocioException("CPF já cadastrado.");
        }

        if (pacienteRepository.findByEmail(paciente.getEmail()).isPresent()) {
            throw new RegraNegocioException("E-mail já cadastrado.");
        }

        paciente.setStatusPaciente(StatusPaciente.ATIVO);
        paciente.setFichaMedica(null);

        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
    }

    public Paciente buscarPorIdCompleto(Long id) {
        return pacienteRepository.findByIdComFicha(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
    }

    public Paciente buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
    }

    public Paciente atualizar(Long id, Paciente dados) {
        Paciente paciente = buscarPorId(id);

        paciente.setNome(dados.getNome());
        paciente.setEmail(dados.getEmail());
        paciente.setTelefone(dados.getTelefone());
        paciente.setDataNascimento(dados.getDataNascimento());
        paciente.setSexo(dados.getSexo());

        return pacienteRepository.save(paciente);
    }

    public void inativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setStatusPaciente(StatusPaciente.INATIVO);
        pacienteRepository.save(paciente);
    }

    public Paciente ativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setStatusPaciente(StatusPaciente.ATIVO);
        return pacienteRepository.save(paciente);
    }

    public Paciente suspender(Long id) {
        Paciente paciente = buscarPorId(id);

        if (paciente.getStatusPaciente() == StatusPaciente.INATIVO) {
            throw new RegraNegocioException("Não é possível suspender um paciente inativo.");
        }

        paciente.setStatusPaciente(StatusPaciente.SUSPENSO);
        return pacienteRepository.save(paciente);
    }

    public FichaMedica adicionarFicha(Long pacienteId, FichaMedica ficha) {
        Paciente paciente = buscarPorId(pacienteId);

        if (paciente.getStatusPaciente() != StatusPaciente.ATIVO) {
            throw new RegraNegocioException("Ficha médica só pode ser criada para paciente ATIVO.");
        }

        if (paciente.getFichaMedica() != null) {
            throw new RegraNegocioException("Paciente já possui ficha médica.");
        }

        paciente.setFichaMedica(ficha);
        pacienteRepository.save(paciente);

        return paciente.getFichaMedica();
    }

    public FichaMedica buscarFicha(Long pacienteId) {
        Paciente paciente = buscarPorIdCompleto(pacienteId);

        if (paciente.getFichaMedica() == null) {
            throw new RecursoNaoEncontradoException("Ficha médica não encontrada.");
        }

        return paciente.getFichaMedica();
    }

    public FichaMedica atualizarFicha(Long pacienteId, FichaMedica dados) {
        Paciente paciente = buscarPorIdCompleto(pacienteId);

        if (paciente.getStatusPaciente() != StatusPaciente.ATIVO) {
            throw new RegraNegocioException("Paciente inativo ou suspenso não pode atualizar ficha.");
        }

        if (paciente.getFichaMedica() == null) {
            throw new RecursoNaoEncontradoException("Ficha médica não encontrada.");
        }

        FichaMedica ficha = paciente.getFichaMedica();
        ficha.setTipoSanguineo(dados.getTipoSanguineo());
        ficha.setAlergias(dados.getAlergias());
        ficha.setMedicamentosUso(dados.getMedicamentosUso());
        ficha.setHistoricoDoencas(dados.getHistoricoDoencas());
        ficha.setObservacoesClinicas(dados.getObservacoesClinicas());

        pacienteRepository.save(paciente);
        return ficha;
    }

    public void removerFicha(Long pacienteId) {
        Paciente paciente = buscarPorIdCompleto(pacienteId);

        if (paciente.getFichaMedica() == null) {
            throw new RecursoNaoEncontradoException("Ficha médica não encontrada.");
        }

        paciente.setFichaMedica(null);
        pacienteRepository.save(paciente);
    }

    public List<Paciente> buscarPorTermo(String termo) {
        return pacienteRepository.buscarPorTermo(termo);
    }

    public List<Paciente> listarComFicha() {
        return pacienteRepository.findByFichaMedicaIsNotNull();
    }

    public List<Paciente> listarSemFicha() {
        return pacienteRepository.findByFichaMedicaIsNull();
    }

    public List<Paciente> buscarPorTipoSanguineo(TipoSanguineo tipo) {
        return pacienteRepository.findByTipoSanguineo(tipo);
    }
}