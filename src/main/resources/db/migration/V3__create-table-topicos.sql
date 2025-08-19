create table topicos (
    id BIGINT not null AUTO_INCREMENT PRIMARY KEY,
    titulo varchar(255) not null,
    mensaje TEXT not null,
    fecha_creacion DATETIME not null,
    status varchar(50) not null,
    autor_id BIGINT not null,
    curso_id BIGINT not null,
    constraint fk_autor foreign key (autor_id) references usuarios(id),
    constraint fk_curso foreign key (curso_id) references cursos(id)
);