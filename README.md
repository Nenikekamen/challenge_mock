# Challenge Mock - Sistema de Transacciones

Este proyecto es una aplicación Spring Boot diseñada para procesar transacciones financieras mockeadas

## Prerrequisitos

- Java 21
- Maven (o usar `./mvnw` incluido)
- Podman (y `podman-compose`) o Docker

## Infraestructura (Mock del Proveedor)

Para simular el comportamiento del proveedor externo, utilizamos WireMock a través de un contenedor.

### Comandos útiles de infraestructura

Puedes usar los siguientes comandos para gestionar el contenedor de WireMock:

```bash
podman compose up -d        # levanta WireMock en segundo plano
podman compose ps           # ver si está corriendo
podman compose logs -f      # ver logs (Ctrl+C para salir)
podman compose down         # apagar y borrar el contenedor
podman compose restart      # reiniciar el contenedor
```

El servicio estará disponible en `http://localhost:8081`.

## Compilación y Pruebas

Para compilar el proyecto y ejecutar las pruebas unitarias e integración:

```bash
./mvnw clean test
```

