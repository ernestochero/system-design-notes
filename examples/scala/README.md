# Scala POCs & Examples

Mini proyecto basado en SBT para experimentar con conceptos de system design usando Scala 3.

## Requisitos

- Java 11+ (`java -version`)
- [sbt](https://www.scala-sbt.org/download.html) instalado y disponible en el `PATH`

## Cómo ejecutar el demo

```bash
cd examples/scala
sbt run
```

Esto ejecuta `runRateLimiterDemo`, que modela un token bucket simple para rate limiting.

## Cómo crear tus propios ejemplos

1. Agrega nuevos archivos en `src/main/scala/examples/` con la lógica que quieras probar.
2. Expone un método `@main` o un `object extends App` para poder lanzarlo con `sbt run` o `sbt "runMain package.Main"`.
3. Para pruebas unitarias añade specs en `src/test/scala` y ejecútalas con `sbt test`.

## Dependencias extra

Edita `build.sbt` y agrega las librerías que necesites en `libraryDependencies`. sbt descargará los JARs automáticamente.
