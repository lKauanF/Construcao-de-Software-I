package com.clinica.repository;

import com.clinica.entity.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FichaMedicaRepository extends JpaRepository<FichaMedica, Long> {
}