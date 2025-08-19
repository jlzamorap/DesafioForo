package com.aluraCursos.DesafioForo.topico;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(

        Long id,
        String titulo,
        String mensaje,
        StatusTopico status

) {
}
