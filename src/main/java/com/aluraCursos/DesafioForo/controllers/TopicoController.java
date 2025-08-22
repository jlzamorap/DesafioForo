package com.aluraCursos.DesafioForo.controllers;

import com.aluraCursos.DesafioForo.curso.CursoRepository;
import com.aluraCursos.DesafioForo.topico.*;
import com.aluraCursos.DesafioForo.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriComponentsBuilder) {

        var autor = usuarioRepository.findByNombre(datos.nombreAutor());
        var curso = cursoRepository.findByNombre(datos.nombreCurso());

        // Verificar si las entidades existen (manejo de errores)
        if (autor == null || curso == null) {
            // Devolver un error 404 Not Found si el autor o el curso no existen
            return ResponseEntity.notFound().build();
        }

        // Crear el nuevo tópico, ahora con las entidades de autor y curso asociadas
        var topico = new Topico(datos, autor, curso);
        repository.save(topico);

        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));

    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(@PageableDefault(size=10, sort = {"titulo"}) Pageable paginacion){

        var page = repository.findByStatus(StatusTopico.ACTIVO, paginacion)
                .map(DatosListaTopico::new);

        return ResponseEntity.ok(page);

    }

    @GetMapping("/{id}")
    public ResponseEntity detallarTopico(@PathVariable Long id){
        var topico = repository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DatosDetalleTopico(topico.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PutMapping
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizarTopico datos){
        // 1. Encuentra el tópico de forma segura usando findById
        var optionalTopico = repository.findById(datos.id());
        // 2. Verifica si el tópico existe
        if (optionalTopico.isPresent()) {
            var topico = optionalTopico.get();
            // 3. Delega la lógica de actualización a la entidad
            topico.actualizarInformaciones(datos);

            return ResponseEntity.ok(new DatosDetalleTopico(topico));
        } else {
            // 4. Devuelve 404 Not Found si no existe
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){

        var optionalTopico = repository.findById(id);

        if (optionalTopico.isPresent()) {
            var topico = optionalTopico.get();

            topico.eliminar();

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

