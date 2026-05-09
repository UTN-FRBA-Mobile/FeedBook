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
- soporte desde Android 9 en adelante

## Nota

Este repositorio representa una idea inicial del proyecto y su primera base de implementacion.
