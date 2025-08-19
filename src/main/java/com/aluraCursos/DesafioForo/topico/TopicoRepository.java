package com.aluraCursos.DesafioForo.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Metodo para buscar un tópico por título y mensaje para verificar duplicados.
    Optional<Topico> findByTituloAndMensaje(String titulo, String mensaje);

    // Metodo para buscar tópicos con paginación, ordenación y filtrado
    Page<Topico> findByCursoNombreAndFechaCreacionBetween(String nombreCurso, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable paginacion);
}
