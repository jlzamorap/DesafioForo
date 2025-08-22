package com.aluraCursos.DesafioForo.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findByStatus(StatusTopico status, Pageable paginacion);

}
