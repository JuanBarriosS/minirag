package com.juan_barrios.minirag.repository;

import com.juan_barrios.minirag.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository
 extends JpaRepository<Consulta, Long> {
}