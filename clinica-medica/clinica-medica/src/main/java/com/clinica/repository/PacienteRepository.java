package com.clinica.repository;

import com.clinica.entity.Paciente;
import com.clinica.enums.TipoSanguineo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.fichaMedica WHERE p.id = :id")
    Optional<Paciente> findByIdComFicha(@Param("id") Long id);

    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByEmail(String email);

    @Query("""
        SELECT p FROM Paciente p
        WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :t, '%'))
           OR LOWER(p.email) LIKE LOWER(CONCAT('%', :t, '%'))
    """)
    List<Paciente> buscarPorTermo(@Param("t") String termo);

    @Query("SELECT p FROM Paciente p JOIN p.fichaMedica f WHERE f.tipoSanguineo = :tipo")
    List<Paciente> findByTipoSanguineo(@Param("tipo") TipoSanguineo tipo);

    List<Paciente> findByFichaMedicaIsNull();

    List<Paciente> findByFichaMedicaIsNotNull();
}