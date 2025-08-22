
# Desafio Foro API REST

¡Bienvenido! Este proyecto es una API RESTful construida con Spring Boot, diseñada para gestionar los tópicos de un foro, incluyendo funcionalidades para usuarios y cursos. La aplicación sigue las mejores prácticas de desarrollo y seguridad, y utiliza JPA para la persistencia de datos y JWT para la autenticación.


## Funcionalidades y Endpoints

La API ofrece las siguientes funcionalidades para la gestión de tópicos:

Autenticación

* POST /login: Autentica a un usuario y devuelve un token JWT para acceder a los endpoints protegidos.

Tópicos

* POST /topicos: Crea un nuevo tópico.

Protegido: Requiere un token JWT válido.

Validaciones: Asegura que el título, el mensaje, el autor y el curso sean válidos.

* GET /topicos: Lista todos los tópicos.

Paginación: Los resultados están paginados (10 por página por defecto) y ordenados por título.

* GET /topicos/{id}: Muestra los detalles de un tópico específico.

* PUT /topicos/{id}: Actualiza un tópico existente.

Protegido: Requiere un token JWT válido.

* DELETE /topicos/{id}: Elimina un tópico.

Protegido: Requiere un token JWT válido.

Respuesta: Devuelve un estado 204 No Content en caso de éxito.


## Tecnologías Utilizadas

* Java 17: Lenguaje de programación.

* Spring Boot: Framework principal para el desarrollo de la API.

* Spring Security: Para gestionar la autenticación y la autorización con tokens JWT.

* Spring Data JPA: Para la persistencia de datos y la interacción con la base de datos.

* MySQL: Base de datos relacional para almacenar la información.

* Flyway: Para el control de versiones de la base de datos.

* Lombok: Para reducir el código repetitivo (getters, setters, constructores, etc.).

* Insomnia: Usado para probar y documentar los endpoints de la API.
## Configuracion y uso

1. Clona el repositorio:
Bash

git clone [url_del_repositorio]

2. Configura la base de datos:

Crea una base de datos MySQL.

Actualiza las credenciales de conexión en el archivo src/main/resources/application.properties.

Flyway se encargará de crear las tablas automáticamente al iniciar la aplicación.

3. Ejecuta la aplicación:

Desde tu IDE (IntelliJ, Eclipse), ejecuta la clase DesafioForoApplication.

Desde la línea de comandos, usa:
Bash

./mvnw spring-boot:run
## Diagrama de clases

La aplicación se basa en las siguientes entidades principales y sus relaciones:

- Topico:

id

titulo

mensaje

fecha_creacion

status

autor (relación ManyToOne con Usuario)

curso (relación ManyToOne con Curso)

- Usuario

- Curso
## Seguridad

El proyecto utiliza Spring Security y tokens JWT para proteger los endpoints. El flujo de seguridad es el siguiente:

- El usuario envía credenciales a /login.

- Si son válidas, se genera un token JWT.

- El usuario incluye este token en el encabezado Authorization de futuras solicitudes a endpoints protegidos (p. ej., POST /topicos).

- Un filtro de seguridad valida el token antes de permitir el acceso al controlador.
## contribuciones

Las contribuciones son bienvenidas. Si encuentras un error o tienes una sugerencia, no dudes en abrir un issue o enviar un pull request.

