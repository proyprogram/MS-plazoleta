package com.proyectointegrador.msplazoleta.repository;

import com.proyectointegrador.msplazoleta.entity.Plato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    Page<Plato> findByRestaurante_Id(Long idRestaurante, Pageable pageable);

    Page<Plato> findByRestaurante_IdAndCategoriaIgnoreCase(
            Long idRestaurante,
            String categoria,
            Pageable pageable
    );
}
