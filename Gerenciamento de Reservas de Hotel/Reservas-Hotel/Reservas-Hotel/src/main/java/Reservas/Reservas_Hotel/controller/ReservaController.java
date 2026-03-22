package Reservas.Reservas_Hotel.controller;

import Reservas.Reservas_Hotel.entity.Reserva;
import Reservas.Reservas_Hotel.enums.Status;
import Reservas.Reservas_Hotel.enums.tipoQuarto;
import Reservas.Reservas_Hotel.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> criar(@Valid @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.criar(reserva));
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody Reserva reserva) {

        return ResponseEntity.ok(reservaService.atualizar(id, reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        reservaService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reserva>> porStatus(@PathVariable Status status) {
        return ResponseEntity.ok(reservaService.buscarPorStatus(status));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reserva>> porTipo(@PathVariable tipoQuarto tipo) {
        return ResponseEntity.ok(reservaService.buscarPorTipoQuarto(tipo));
    }

    @GetMapping("/hoje")
    public ResponseEntity<List<Reserva>> reservasHoje() {
        return ResponseEntity.ok(reservaService.reservasHoje());
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<Reserva>> proximas(@RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(reservaService.proximasReservas(dias));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Reserva>> buscar(@RequestParam String termo) {
        return ResponseEntity.ok(reservaService.buscarPorTermo(termo));
    }

    @GetMapping("/em-hospedagem")
    public ResponseEntity<List<Reserva>> emHospedagem() {
        return ResponseEntity.ok(reservaService.emHospedagem());
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.confirmar(id));
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<Reserva> checkin(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.checkin(id));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<Reserva> checkout(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.checkout(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }
}