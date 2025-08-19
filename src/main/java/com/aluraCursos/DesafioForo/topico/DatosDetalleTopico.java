package com.aluraCursos.DesafioForo.topico;

import java.time.LocalDateTime;

public record DatosDetalleTopico(

        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String nombreAutor,
        String nombreCurso
) {
    public DatosDetalleTopico(Topico topico) {
    this(topico.getId(),
        topico.getTitulo(),
        topico.getMensaje(),
        topico.getFechaCreacion(),
        topico.getStatus(),
        topico.getAutor().getNombre(),
        topico.getCurso().getNombre()
        );

    }

}

