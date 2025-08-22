package com.aluraCursos.DesafioForo.topico;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroTopico(

        @NotBlank
        String titulo,

        @NotBlank
        String mensaje,

        @NotBlank
        String nombreAutor,

        @NotBlank
        String nombreCurso
) {
}
