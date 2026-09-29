# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Configuración (sin subir secretos a Git)

Todas las claves de `application.properties` aceptan variables de entorno (`DB_HOST`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `JWT_SECRET`, `PORT`, ...).
Para desarrollo local copie `secrets.properties.example` como `secrets.properties` (ignorado por Git) y ponga ahí su clave de MySQL/PostgreSQL y la contraseña de aplicación de Gmail.

| Propiedad | Variable | Valores |
|---|---|---|
| `db.engine` | `DB_ENGINE` | `mysql` (por defecto) o `postgresql` — elige el adaptador `UserRepositoryMySQL` o `UserRepositoryPostgreSQL` |
| `db.ssl-mode` | `DB_SSL_MODE` | Solo PostgreSQL: `disable`, `prefer` (defecto), `require` (Supabase) |

## Base de datos

* **MySQL:** ejecute `src/main/resources/schema.sql`.
* **PostgreSQL local:** `CREATE DATABASE crud_usuarios;` y ejecute `src/main/resources/schema-postgresql.sql`.
* **Supabase:** pegue `schema-postgresql.sql` en el *SQL Editor*. Use los datos de **Connect → Session pooler** (host `aws-0-<region>.pooler.supabase.com`, puerto `5432`, usuario `postgres.<project-ref>`, base `postgres`, `DB_SSL_MODE=require`). La conexión directa de Supabase es solo IPv6 y Render no la alcanza.

Usuario inicial: `admin@example.com` / `Admin1234!`.

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Docker

```bash
docker build -t users-management .
docker compose up --build                                   # API + MySQL
docker compose --profile postgres up --build app-pg postgres  # API + PostgreSQL
```

La imagen compila el proyecto dentro del contenedor (multi-stage), así que no hace falta Maven en la máquina.

## Despliegue en Render

1. Cree la base en Supabase y ejecute `schema-postgresql.sql`.
2. En Render: **New → Blueprint** y seleccione este repositorio (usa `render.yaml`), o **New → Web Service → Docker**.
3. Complete las variables: `DB_HOST`, `DB_USERNAME`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD` (el resto ya viene en `render.yaml`).
4. Abra `https://<servicio>.onrender.com/swagger-ui/index.html`.

> Nota: el plan gratuito de Render bloquea los puertos SMTP (25/465/587), por lo que el envío de correo solo funciona en local/Docker o en un plan de pago. El SMTP tiene un timeout de 10 s para no colgar las peticiones.
