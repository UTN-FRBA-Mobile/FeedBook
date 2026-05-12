# FeedBook

<p align="center">
  <img src="app/src/main/res/drawable/feedbook_logo.svg" alt="Logo de FeedBook" width="120" />
</p>

Proyecto de la facultad para Android sobre lectura, seguimiento de progreso y funciones sociales entre lectores.

## Idea general

FeedBook busca combinar un gestor personal de lecturas con una capa social simple. La app apunta a que cada usuario pueda:

- explorar un catalogo de libros desde un servidor propio
- registrar lecturas y calificaciones
- escribir resenas
- seguir el avance de lectura por porcentaje
- organizar libros en listas personalizadas o pendientes
- ver actividad, reseñas y recomendaciones de otros lectores

## Funcionalidades pensadas

- escaneo de ISBN con la camara para agregar libros
- descarga de archivos EPUB cuando esten disponibles
- acceso offline a libros descargados
- notificaciones push por actividad social
- widget con el libro en curso y el progreso actual

## APIs y servicios

- servidor de catalogo y biblioteca: expone libros, metadatos, ISBN y descargas EPUB
- servidor de aplicacion: maneja usuarios, amistades, resenas, listas y actividad social
- camara del dispositivo: para escanear codigos ISBN
- almacenamiento local: para guardar EPUBs descargados
- notificaciones del sistema: para avisos sociales
- widget de Android: para mostrar lectura actual y progreso

## Base tecnica

- Android nativo
- Kotlin
- Jetpack Compose
- backend companion en Go dentro del submodulo `back/`
- soporte desde Android 9 en adelante

## Backend local

El contenido principal que antes estaba mockeado en la app ahora se sirve desde
el backend REST del submodulo `back/`.

Para levantarlo localmente:

```bash
cd back
go test ./...
go run .
```

Por defecto escucha en `http://127.0.0.1:8080`. Se puede cambiar con
`FEEDBOOK_ADDR`.

La app Android usa una sola origin configurable mediante
`BuildConfig.BACKEND_ORIGIN`, que inicialmente apunta a
`http://localhost:8080/`.

Tips de entorno:

- emulador Android Studio: usar `http://10.0.2.2:8080/`
- dispositivo fisico con `adb reverse tcp:8080 tcp:8080`: mantener `http://localhost:8080/`

## Forma de trabajo

- La constitucion del proyecto vive en `.specify/memory/constitution.md` y fija
  reglas para arquitectura por feature, limites de red/offline, testing,
  accesibilidad y configuracion segura.
- Cada nuevo cambio de producto deberia reflejar esas reglas en su spec, plan y
  tareas antes de implementarse.

## Nota

Este repositorio representa una idea inicial del proyecto y su primera base de implementacion.
