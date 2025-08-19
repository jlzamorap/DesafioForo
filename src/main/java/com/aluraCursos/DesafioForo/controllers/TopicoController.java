package com.aluraCursos.DesafioForo.controllers;

import com.aluraCursos.DesafioForo.topico.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosDetalleTopico> registrar(@RequestBody @Valid DatosRegistroTopico datosRegistro,
                                                              UriComponentsBuilder uriBuilder) {
        Optional<Topico> topicoExistente = repository.findByTituloAndMensaje(datosRegistro.titulo(), datosRegistro.mensaje());
        if (topicoExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Topico topico = new Topico(datosRegistro);
        repository.save(topico);

        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosDetalleTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable paginacion,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio) {

        Page<Topico> topicos;

        if (curso != null && anio != null) {
            // Filtrar por curso y año
            LocalDateTime fechaInicio = LocalDate.of(anio, 1, 1).atStartOfDay();
            LocalDateTime fechaFin = LocalDate.of(anio, 12, 31).atTime(23, 59, 59);
            topicos = repository.findByCursoNombreAndFechaCreacionBetween(curso, fechaInicio, fechaFin, paginacion);
        } else {
            // Listar todos los tópicos con paginación y ordenación por defecto
            topicos = repository.findAll(paginacion);
        }
        return ResponseEntity.ok(topicos.map(DatosListaTopico::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> detalleTopico(@PathVariable Long id) {
        return repository.findById(id)
                .map(topico -> ResponseEntity.ok(new DatosDetalleTopico(topico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> actualizarTopico(@PathVariable Long id,
                                                               @RequestBody @Valid DatosActualizarTopico datosActualizacion) {
        Optional<Topico> optionalTopico = repository.findById(id);
        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        Topico topico = optionalTopico.get();

        if (datosActualizacion.titulo() != null && datosActualizacion.mensaje() != null) {
            Optional<Topico> duplicado = repository.findByTituloAndMensaje(
                    datosActualizacion.titulo(),
                    datosActualizacion.mensaje()
            );
            if (duplicado.isPresent() && !duplicado.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 si ya existe otro igual
            }
        }
        // Actualizar solo los campos que no sean nulos
        if (datosActualizacion.titulo() != null) {
            topico.setTitulo(datosActualizacion.titulo());
        }
        if (datosActualizacion.mensaje() != null) {
            topico.setMensaje(datosActualizacion.mensaje());
        }
        if (datosActualizacion.status() != null) {
            topico.setStatus(datosActualizacion.status());
        }

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        // 1. Verificar si el tópico existe
        Optional<Topico> topicoOptional = repository.findById(id);

        if (topicoOptional.isPresent()) {
            // 2. Si el tópico existe, eliminarlo de la base de datos
            repository.deleteById(id);
            // 3. Devolver una respuesta sin contenido (204 No Content)
            return ResponseEntity.noContent().build();
        } else {
            // 4. Si el tópico no existe, devolver un error 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}
