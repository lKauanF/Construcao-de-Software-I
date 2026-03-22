package Reservas.Reservas_Hotel.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Detalhes-Estadia")
public class DetalhesEstadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotBlank
    private String numeroQuarto;

    @Column
    @NotNull
    @Min(1)
    private Integer andar;

    @Column
    private boolean possuiFrigobar = false;
    @Column
    private boolean possuiVaranda = false;
    @Column
    private boolean acessibilidade = false;
    @Column
    @Size(max=300)
    private String observacoesQuarto;

}
