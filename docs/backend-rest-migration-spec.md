# FeedBook Backend Migration Spec

## Objetivo

Mover el origen de datos mockeado de `app/src/main/java/com/example/feedbook/shared/fakebackend`
hacia el submodulo `back`, exponiendo endpoints REST en Go para que la app Android consuma los mismos datos.

El backend no tendra persistencia. Todos los datos seguiran hardcodeados en memoria.

## Alcance

- Backend Go:
  - conservar login
  - exponer endpoints REST para libros, autores, home, perfil, biblioteca, estadisticas y notificaciones
  - mantener estado en memoria para:
    - perfil propio editable
    - autores seguidos
- App Android:
  - dejar de depender de `FakeFeedBookBackend`
  - consumir el backend por Retrofit
  - parametrizar la base URL por configuracion
  - mantener el mismo shape de DTOs para minimizar cambios en mappers y UI

## Decisiones

1. La app y el backend comparten contrato JSON, pero no comparten codigo fuente entre repos.
2. La base URL se parametriza desde Gradle con default local.
3. El backend sirve una API HTTP simple sobre `net/http`, sin framework externo.
4. Las pantallas que hoy observan `Flow` remoto pasan a observar un `flow { emit(fetch()) }`.
   No hay streaming real; solo lectura puntual contra REST.

## Base URL

- Default Android:
  - `http://127.0.0.1:8080/`
- La ruta de API quedara bajo prefijo `api/`.
- Login quedara en `POST /login` para mantener compatibilidad simple.

## Endpoints

### Auth

- `POST /login`

### Books / Explore

- `GET /api/books`
- `GET /api/books/{id}`
- `GET /api/books/{id}/progress`
- `GET /api/books/{id}/reviews`
- `GET /api/explore/users`

### Authors

- `GET /api/authors`
- `GET /api/authors/{id}`
- `POST /api/authors/{id}/follow`

### Home / Library / Profile

- `GET /api/home`
- `GET /api/library/me`
- `GET /api/profile/me`
- `GET /api/profile/me/public-preview`
- `PUT /api/profile/me`
- `GET /api/profile/public`

### Stats / Notifications

- `GET /api/stats`
- `GET /api/notifications`

## Estructura backend esperada

- `cmd` o `main.go` para bootstrap
- `internal/app` para server y wiring
- `internal/feedbook` para:
  - `models.go`
  - `store.go`
  - `handlers.go`

## Estructura Android esperada

- `NetworkModule` con base URL configurable
- `ApiService` ampliado con todos los recursos REST
- `RemoteDataSource` desacoplados del fake backend
- `AppContainer` inicializando solo servicios HTTP

## Verificacion

- Backend:
  - `go test ./...`
- Android:
  - `./gradlew :app:compileDebugKotlin`

## Fuera de alcance

- persistencia
- autenticacion real mas alla del login mock actual
- sincronizacion en tiempo real
- paginacion, cache y retry sofisticado
