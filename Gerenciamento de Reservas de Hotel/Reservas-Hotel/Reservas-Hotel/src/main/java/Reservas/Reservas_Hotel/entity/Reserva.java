package Reservas.Reservas_Hotel.entity;

import Reservas.Reservas_Hotel.enums.Status;
import Reservas.Reservas_Hotel.enums.tipoQuarto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column
    private String nomeHospede;

    @NotBlank
    @Email
    @Column
    private String emailHospede;

    @Size(max = 20)
    @Column
    private String telefonHospede;

    @NotNull
    @FutureOrPresent
    @Column
    private LocalDate dataEntrada;

    @NotNull
    @Column
    private LocalDate dataSaida;

    private LocalDateTime dataCheckIn;

    private LocalDateTime dataCheckOut;

    private LocalDateTime dataCriacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private tipoQuarto tipoQuarto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Size(max = 500)
    @Column
    private String observacoes;

    @PrePersist
    @Column
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.status = Status.PENDENTE;
        this.dataCheckIn = null;
        this.dataCheckOut = null;
    }

    public boolean datasValidas() {
        return dataEntrada != null && dataSaida != null && dataSaida.isAfter(dataEntrada);
    }

    public Reserva() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeHospede() {
        return nomeHospede;
    }

    public void setNomeHospede(String nomeHospede) {
        this.nomeHospede = nomeHospede;
    }

    public String getEmailHospede() {
        return emailHospede;
    }

    public void setEmailHospede(String emailHospede) {
        this.emailHospede = emailHospede;
    }

    public String getTelefonHospede() {
        return telefonHospede;
    }

    public void setTelefonHospede(String telefonHospede) {
        this.telefonHospede = telefonHospede;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(LocalDateTime dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDateTime getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(LocalDateTime dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public tipoQuarto getTipoQuarto() {
        return tipoQuarto;
    }

    public void setTipoQuarto(tipoQuarto tipoQuarto) {
        this.tipoQuarto = tipoQuarto;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}