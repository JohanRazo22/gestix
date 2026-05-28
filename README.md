# Gestix

> Gestor de tareas profesional construido con **Spring Boot 3 + PostgreSQL + JWT + OAuth2 (Google + Microsoft)** y un front estático con **Tailwind + Chart.js**.

Permite organizar tareas con prioridad, fecha límite y categoría, autenticarse con email/contraseña, con cuenta de Google o con cuenta de Microsoft (Entra ID), y visualizar el progreso con gráficos en el dashboard.

---

## Stack

| Capa | Tecnología |
|------|------------|
| Backend | Spring Boot 3.2.5, Java 17 |
| Persistencia | Spring Data JPA + PostgreSQL |
| Seguridad | Spring Security, JWT (jjwt 0.12), OAuth2 Google + Microsoft (OIDC) |
| Validación | Jakarta Bean Validation |
| Build | Maven |
| Front | HTML estático + Tailwind (CDN) + Chart.js |
| Tests | JUnit 5, Mockito, Spring Security Test |

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/gestix/
│   │   ├── GestixApplication.java
│   │   ├── config/         # SecurityConfig, PasswordEncoderConfig
│   │   ├── controller/     # AuthController, TaskController
│   │   ├── service/        # AuthService, TaskService
│   │   ├── repository/     # UserRepository, TaskRepository
│   │   ├── entity/         # User, Task
│   │   ├── enums/          # TaskStatus, Priority
│   │   ├── dto/            # Auth/Login/Register/Task Request+Response
│   │   ├── security/       # JwtUtil, JwtAuthFilter, OAuth2 handlers
│   │   └── exception/      # GlobalExceptionHandler
│   └── resources/
│       ├── application.yml
│       └── static/         # index.html, login.html, dashboard.html, js/, css/
└── test/
    ├── java/com/gestix/...
    └── resources/application.yml   # config dummy para tests
```

---

## Requisitos

- **Java 17+**
- **Maven 3.8+** (o usar el wrapper si se añade)
- **PostgreSQL 13+** corriendo localmente o accesible
- Cuenta de Google Cloud con credenciales OAuth2 (opcional, solo si se quiere login con Google)
- App registrada en Microsoft Entra ID (opcional, solo si se quiere login con Microsoft)

---

## Configuración

Todos los secretos se leen desde variables de entorno. **Nada de credenciales en el repo.**

1. Crea la base de datos:

   ```sql
   CREATE DATABASE gestix_db;
   ```

2. Copia el ejemplo de variables:

   ```bash
   cp .env.example .env
   ```

3. Edita `.env` y completa al menos:

   | Variable | Obligatoria | Descripción |
   |----------|:----------:|-------------|
   | `DB_URL` | no (default: `jdbc:postgresql://localhost:5432/gestix_db`) | URL JDBC de la base |
   | `DB_USERNAME` | no (default: `postgres`) | Usuario de la base |
   | `DB_PASSWORD` | **sí** | Contraseña de la base |
   | `GOOGLE_CLIENT_ID` | **sí** | Client ID de Google OAuth2 |
   | `GOOGLE_CLIENT_SECRET` | **sí** | Client Secret de Google OAuth2 |
   | `MICROSOFT_CLIENT_ID` | **sí** | Application (client) ID de la app en Microsoft Entra |
   | `MICROSOFT_CLIENT_SECRET` | **sí** | Client Secret (columna *Value*) de la app en Microsoft Entra |
   | `JWT_SECRET` | **sí** | Clave HMAC-SHA256 (≥ 32 caracteres). Genera con `openssl rand -hex 32` |
   | `JWT_EXPIRATION` | no (default: `86400000`) | Vida del JWT en milisegundos |
   | `SERVER_PORT` | no (default: `8080`) | Puerto HTTP |
   | `JPA_DDL_AUTO` | no (default: `update`) | Estrategia de schema de Hibernate |
   | `JPA_SHOW_SQL` | no (default: `true`) | Imprime SQL en consola |

### Cómo obtener credenciales de Google OAuth2

1. Ve a <https://console.cloud.google.com/apis/credentials>.
2. Crea un proyecto si no tienes uno.
3. Crea credenciales de tipo **OAuth client ID** → **Web application**.
4. En *Authorized redirect URIs* añade:
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
5. Copia el `client-id` y `client-secret` a tu `.env`.

### Cómo obtener credenciales de Microsoft (Entra ID)

1. Entra a <https://entra.microsoft.com> con una cuenta que tenga directorio (empresarial,
   sandbox de M365 dev program, o un tenant gratuito creado en *Administrar inquilinos*).
2. **Entra ID → Registros de aplicaciones → + Nuevo registro**.
3. Rellena:
   - **Nombre**: `Gestix`
   - **Tipos de cuenta admitidos**: *Cuentas en cualquier directorio organizativo (multiinquilino) y cuentas personales de Microsoft*.
   - **URI de redirección**: tipo *Web* · URL `http://localhost:8080/login/oauth2/code/microsoft`.
4. Copia el **Application (client) ID** (overview de la app) a `MICROSOFT_CLIENT_ID`.
5. **Certificados y secretos → + Nuevo secreto de cliente** (expira en 24 meses).
   Copia la columna **Value** *inmediatamente* — solo se muestra una vez.
   Va a `MICROSOFT_CLIENT_SECRET`.

> El backend usa el endpoint `common` (acepta cuentas personales y de cualquier organización).
> Si quieres restringirlo a un solo tenant, cambia `common` por el `tenantId` en
> `application.yml` (campos `authorization-uri`, `token-uri` y `jwk-set-uri`).

---

## Cómo correrlo

### Opción A — exportar variables y arrancar Maven

**Linux / macOS:**
```bash
set -a; source .env; set +a
mvn spring-boot:run
```

**Windows PowerShell:**
```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1].Trim(), $matches[2].Trim())
    }
}
mvn spring-boot:run
```

### Opción B — pasarlas inline a Maven

```bash
DB_PASSWORD=xxx GOOGLE_CLIENT_ID=xxx GOOGLE_CLIENT_SECRET=xxx JWT_SECRET=xxx mvn spring-boot:run
```

Luego abre <http://localhost:8080>.

---

## Tests

```bash
mvn test
```

Los tests usan `src/test/resources/application.yml` con valores dummy, por lo que **no necesitan ninguna variable de entorno** para ejecutarse.

Cobertura actual: **50 tests**, todos en verde.

| Suite | Tests |
|-------|------:|
| `AuthServiceTest` | 7 |
| `TaskServiceTest` | 13 |
| `JwtUtilTest` | 8 |
| `AuthControllerTest` | 10 |
| `TaskControllerTest` | 12 |

---

## API

Todos los endpoints `/api/tasks/**` requieren el header:

```
Authorization: Bearer <token>
```

### Auth

| Método | Endpoint | Body | Respuesta |
|--------|----------|------|-----------|
| POST | `/api/auth/register` | `{ username, email, password }` | `{ token, username, email }` |
| POST | `/api/auth/login` | `{ email, password }` | `{ token, username, email }` |
| POST | `/api/auth/forgot-password` | `{ email }` | `{ message }` — siempre la misma respuesta (no revela si el email existe) |
| POST | `/api/auth/reset-password` | `{ token, password }` | `{ message }` — token del enlace enviado por correo |
| GET  | `/oauth2/authorization/google` | — | Redirige a Google y luego a `/dashboard.html?token=...` |
| GET  | `/oauth2/authorization/microsoft` | — | Redirige a Microsoft (Entra ID) y luego a `/dashboard.html?token=...` |

### Tareas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST   | `/api/tasks` | Crea tarea |
| GET    | `/api/tasks` | Lista tareas del usuario autenticado |
| GET    | `/api/tasks/{id}` | Obtiene una tarea propia |
| PUT    | `/api/tasks/{id}` | Actualiza tarea |
| DELETE | `/api/tasks/{id}` | Elimina tarea |

Body de `TaskRequest`:

```json
{
  "title": "string",
  "description": "string?",
  "status": "PENDING|IN_PROGRESS|COMPLETED",
  "priority": "LOW|MEDIUM|HIGH",
  "category": "string?",
  "dueDate": "2026-12-31",
  "assignedToId": null
}
```

---

## Seguridad — checklist antes de hacer público el repo

- [x] `.gitignore` excluye `target/`, logs, IDE y `.env`.
- [x] Ningún secreto hard-codeado en `application.yml`.
- [x] `application.yml` de tests con valores dummy (no reales).
- [ ] **Rota** cualquier secreto que haya estado en claro previamente:
  - Genera un nuevo `JWT_SECRET` (`openssl rand -hex 32`).
  - Regenera el **client secret** de Google OAuth2 en Google Cloud Console.
  - Regenera el **client secret** de Microsoft en *Entra → Certificados y secretos*
    (borra el viejo y crea uno nuevo).
  - Cambia la contraseña de PostgreSQL si era la de producción.

---

## Roadmap / ideas

- Persistir proyectos como entidad propia (hoy viven en `localStorage`).
- Refresh tokens y revocación.
- OAuth2 → token vía cookie `HttpOnly` en vez de query string.
- Filtros y paginación de tareas en el backend.
- Dockerfile + `docker-compose.yml` con Postgres.
- Migración del front a un SPA (React / Vue).

---

## Licencia

MIT — siéntete libre de usarlo, modificarlo y compartirlo.
