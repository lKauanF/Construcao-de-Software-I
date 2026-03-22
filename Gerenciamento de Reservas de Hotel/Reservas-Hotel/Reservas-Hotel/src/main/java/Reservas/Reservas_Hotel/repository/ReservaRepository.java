package Reservas.Reservas_Hotel.repository;

import Reservas.Reservas_Hotel.entity.Reserva;
import Reservas.Reservas_Hotel.enums.Status;
import Reservas.Reservas_Hotel.enums.tipoQuarto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByStatus(Status status);

    List<Reserva> findByTipoQuarto(tipoQuarto tipoQuarto);

    List<Reserva> findByDataEntrada(LocalDate dataEntrada);

    List<Reserva> findByDataEntradaBetween(LocalDate inicio, LocalDate fim);

    List<Reserva> findByNomeHospedeContainingIgnoreCaseOrEmailHospedeContainingIgnoreCase(String nome, String email);

    List<Reserva> findByStatusOrderByDataEntradaAsc(Status status);
}