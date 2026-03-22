package Reservas.Reservas_Hotel.service;

import Reservas.Reservas_Hotel.entity.Reserva;
import Reservas.Reservas_Hotel.enums.Status;
import Reservas.Reservas_Hotel.enums.tipoQuarto;
import Reservas.Reservas_Hotel.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva criar(Reserva reserva) {

        if (!reserva.datasValidas()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de saída deve ser posterior à data de entrada.");
        }

        reserva.setStatus(Status.PENDENTE);
        reserva.setDataCriacao(LocalDateTime.now());

        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada."));
    }

    public Reserva atualizar(Long id, Reserva reservaAtualizada) {

        Reserva reserva = buscarPorId(id);

        reserva.setNomeHospede(reservaAtualizada.getNomeHospede());
        reserva.setEmailHospede(reservaAtualizada.getEmailHospede());
        reserva.setTelefonHospede(reservaAtualizada.getTelefonHospede());
        reserva.setDataEntrada(reservaAtualizada.getDataEntrada());
        reserva.setDataSaida(reservaAtualizada.getDataSaida());
        reserva.setTipoQuarto(reservaAtualizada.getTipoQuarto());
        reserva.setObservacoes(reservaAtualizada.getObservacoes());

        return reservaRepository.save(reserva);
    }

    public void excluir(Long id) {

        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() == Status.EM_HOSPEDAGEM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é permitido excluir uma reserva em hospedagem.");
        }

        reservaRepository.delete(reserva);
    }

    public List<Reserva> buscarPorStatus(Status status) {
        return reservaRepository.findByStatus(status);
    }

    public List<Reserva> buscarPorTipoQuarto(tipoQuarto tipoQuarto) {
        return reservaRepository.findByTipoQuarto(tipoQuarto);
    }

    public List<Reserva> reservasHoje() {
        return reservaRepository.findByDataEntrada(LocalDate.now());
    }

    public List<Reserva> proximasReservas(int dias) {

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);

        return reservaRepository.findByDataEntradaBetween(hoje, limite);
    }

    public List<Reserva> buscarPorTermo(String termo) {
        return reservaRepository
                .findByNomeHospedeContainingIgnoreCaseOrEmailHospedeContainingIgnoreCase(termo, termo);
    }

    public List<Reserva> emHospedagem() {
        return reservaRepository.findByStatusOrderByDataEntradaAsc(Status.EM_HOSPEDAGEM);
    }

    public Reserva confirmar(Long id) {

        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != Status.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Somente reservas pendentes podem ser confirmadas.");
        }

        reserva.setStatus(Status.CONFIRMADA);

        return reservaRepository.save(reserva);
    }

    public Reserva checkin(Long id) {

        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != Status.CONFIRMADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Reserva precisa estar confirmada para check-in.");
        }

        reserva.setStatus(Status.EM_HOSPEDAGEM);
        reserva.setDataCheckIn(LocalDateTime.now());

        return reservaRepository.save(reserva);
    }

    public Reserva checkout(Long id) {

        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != Status.EM_HOSPEDAGEM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Reserva não está em hospedagem.");
        }

        reserva.setStatus(Status.CONCLUIDA);
        reserva.setDataCheckOut(LocalDateTime.now());

        return reservaRepository.save(reserva);
    }

    public Reserva cancelar(Long id) {

        Reserva reserva = buscarPorId(id);

        reserva.setStatus(Status.CANCELADA);

        return reservaRepository.save(reserva);
    }
}