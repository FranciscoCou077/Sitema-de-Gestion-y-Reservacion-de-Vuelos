<div align="center">

# ✈️ Aeroviajes

### Sistema de gestión y reservación de vuelos

*Aplicación de escritorio en Java que simula un sistema comercial de reserva de vuelos, desarrollada como demostración integral del paradigma de Programación Orientada a Objetos, patrones de diseño y programación concurrente.*

<br>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![NetBeans](https://img.shields.io/badge/IDE-NetBeans%2021-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white)

![Patrones](https://img.shields.io/badge/Patrones%20GoF-6-success?style=flat-square)
![Concurrencia](https://img.shields.io/badge/Concurrencia-Productor--Consumidor-blueviolet?style=flat-square)
![Estado](https://img.shields.io/badge/Estado-En%20desarrollo-yellow?style=flat-square)
![Licencia](https://img.shields.io/badge/Licencia-MIT-green?style=flat-square)

</div>

---

## 📑 Tabla de contenidos

- [Descripción](#-descripción)
- [Características principales](#-características-principales)
- [Capturas de pantalla](#-capturas-de-pantalla)
- [Arquitectura del sistema](#-arquitectura-del-sistema)
- [Patrones de diseño implementados](#-patrones-de-diseño-implementados)
- [Conceptos de POO aplicados](#-conceptos-de-poo-aplicados)
- [Programación concurrente](#-programación-concurrente)
- [Tecnologías](#-tecnologías)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Guía de uso](#-guía-de-uso)
- [Documentación](#-documentación)
- [Equipo](#-equipo)
- [Contexto académico](#-contexto-académico)
- [Licencia](#-licencia)

---

## Descripción

**Aeroviajes** es un sistema de escritorio que permite a los usuarios registrarse, iniciar sesión, consultar vuelos disponibles, comprar boletos y descargar el comprobante de su reserva. Incorpora un esquema de control de acceso con dos roles diferenciados —**administrador** y **cliente**— y persiste toda la información mediante manejo de archivos.

El proyecto fue concebido no solo para cumplir los requisitos de un curso de Programación Orientada a Objetos, sino para servir como **pieza de portafolio profesional** que evidencia el dominio de:

- Diseño de software por capas y separación de responsabilidades.
- Aplicación correcta de patrones de diseño GoF en un dominio realista.
- Programación concurrente con hilos y estructuras *thread-safe*.
- Construcción de interfaces gráficas de usuario con Java Swing.

---

## Características principales

### Para clientes
-  **Registro de usuarios** con generación automática de contraseña segura.
-  **Inicio de sesión** mediante credenciales validadas contra archivo persistente.
-  **Consulta de vuelos** con detalle de origen, destino, fecha, precio y disponibilidad de asientos.
-  **Compra de boletos** con resumen previo a la confirmación y validación de método de pago.
-  **Generación y descarga de tickets** con todos los datos de la reserva.

### Para administradores
-  **Alta y baja de vuelos** en el sistema.
-  **Consulta de usuarios registrados** en la plataforma.
-  **Eliminación de usuarios** específicos.
-  **Operaciones protegidas** mediante un *Protection Proxy* que valida permisos en tiempo de ejecución.

### Transversales
-  **Persistencia automática** de usuarios, vuelos y reservas mediante serialización.
-  **Generación asíncrona de tickets** que no congela la interfaz gráfica.
-  **Auto-guardado periódico** en segundo plano.

---

## Capturas de pantalla

> _Las capturas se agregarán conforme avance el desarrollo de la interfaz gráfica._

| Inicio de sesión | Panel de cliente | Compra de boleto |
|:---:|:---:|:---:|
| _(pendiente)_ | _(pendiente)_ | _(pendiente)_ |

| Panel de administrador | Gestión de vuelos | Ticket generado |
|:---:|:---:|:---:|
| _(pendiente)_ | _(pendiente)_ | _(pendiente)_ |

---

## Arquitectura del sistema

El sistema sigue una **arquitectura por capas** que separa claramente las responsabilidades:

```
┌──────────────────────────────────────────────┐
│   Capa de Presentación  ·  aeroviajes.ui       │  ← Swing (JFrame, JPanel)
├──────────────────────────────────────────────┤
│   Capa de Servicio  ·  aeroviajes.service      │  ← Lógica de negocio
├──────────────────────────────────────────────┤
│   Capa de Persistencia  ·  aeroviajes.persistence │  ← Archivos / serialización
├──────────────────────────────────────────────┤
│   Capa de Dominio  ·  aeroviajes.model         │  ← Entidades del negocio
└──────────────────────────────────────────────┘
        ▲ patterns · threads · exception · util  (transversales)
```

La comunicación entre capas es unidireccional descendente: la presentación depende del servicio, el servicio del dominio y la persistencia, y nunca al revés. Esto facilita el mantenimiento, las pruebas y la extensibilidad del sistema.

---

## Patrones de diseño implementados

El proyecto implementa **seis patrones de diseño GoF** que abarcan las tres categorías clásicas (creacionales, estructurales y conductuales), además de un **patrón de concurrencia**.

| Patrón | Categoría | Ubicación | Propósito en Aeroviajes |
|--------|-----------|-----------|--------------------------|
| **Singleton** | Creacional | `SistemaAeroviajes` | Garantiza una única instancia coordinadora del sistema y acceso global controlado. |
| **Factory Method** | Creacional | `UsuarioFactory` | Centraliza la creación de objetos `Administrador` o `Cliente` según el tipo solicitado. |
| **Flyweight** | Estructural | `AeropuertoFlyweightFactory`, `AerolineaFlyweightFactory` | Comparte instancias inmutables de aeropuertos y aerolíneas entre múltiples vuelos, optimizando memoria. |
| **Proxy** | Estructural | `GestorVuelosProxy` | *Protection Proxy* que valida permisos de administrador antes de delegar operaciones críticas. |
| **Strategy** | Conductual | `IValidadorPago` + implementaciones | Intercambia algoritmos de validación de pago según el tipo de tarjeta. |
| **Observer** | Conductual | `NotificadorReservas`, `IObservadorReserva` | Notifica a múltiples componentes cuando se concreta una reserva. |

### Separación de estado en el Flyweight

| Estado intrínseco (compartido) | Estado extrínseco (único por vuelo) |
|---|---|
| Código IATA, nombre, ciudad, país del aeropuerto | Fecha y hora de salida |
| Nombre y código de la aerolínea | Precio, asientos disponibles |

---

## Conceptos de POO aplicados

| Concepto | Implementación |
|----------|----------------|
| **Encapsulamiento** | Atributos `private`/`private final`, exposición controlada vía getters. |
| **Herencia** | `Usuario` (abstracta) → `Administrador`, `Cliente`. |
| **Polimorfismo** | Listas polimórficas de `IObservadorReserva`; método `crear()` sobreescrito en validadores. |
| **Abstracción** | Clase abstracta `Usuario` + 5 interfaces (`IRepositorio`, `IGestorVuelos`, `IValidadorPago`, `IObservadorReserva`, `Runnable`). |
| **Colecciones** | `ArrayList`, `HashMap`, `HashSet`, `LinkedList`, `BlockingQueue`. |
| **Manejo de excepciones** | 6 excepciones personalizadas + manejo robusto en la capa de presentación. |
| **Genéricos** | Interfaz `IRepositorio<T>` parametrizada por tipo. |
| **Manejo de archivos** | Serialización de objetos (`.dat`) y escritura de tickets (`.txt`). |

---

## Programación concurrente

Aeroviajes integra hilos mediante el patrón **Productor-Consumidor**, acoplado al patrón Observer:

1. Al concretarse una compra, `NotificadorReservas` dispara la notificación (Observer).
2. El observador `ProductorTickets` deposita la reserva en una `BlockingQueue<Reserva>` *thread-safe*.
3. Un hilo dedicado (`HiloConsumidorTickets`) consume la cola y genera el archivo del ticket en segundo plano.
4. La interfaz gráfica retorna el control al usuario de inmediato, sin bloquearse.

| Hilo | Mecanismo | Función |
|------|-----------|---------|
| `HiloConsumidorTickets` | `extends Thread` | Consume la cola y genera tickets de forma asíncrona. |
| `HiloAutoGuardado` | `implements Runnable` | Persiste los datos del sistema periódicamente. |
| `HiloCargaVuelos` | `extends SwingWorker` | Carga el catálogo de vuelos mostrando una barra de progreso. |

---

## Tecnologías

- **Lenguaje:** Java 21
- **Interfaz gráfica:** Java Swing
- **IDE:** Apache NetBeans 21
- **Concurrencia:** `java.util.concurrent` (`BlockingQueue`, `SwingWorker`)
- **Persistencia:** Serialización Java (`ObjectInputStream` / `ObjectOutputStream`)
- **Control de versiones:** Git + GitHub
- **Documentación:** LaTeX (reporte), PlantUML (diagramas UML)

---

## Estructura del proyecto

```
Aeroviajes/
├── src/aeroviajes/
│   ├── Main.java
│   ├── model/          # Entidades del dominio
│   ├── service/        # Lógica de negocio
│   ├── persistence/    # Repositorios y manejo de archivos
│   ├── exception/      # Excepciones personalizadas
│   ├── patterns/       # Singleton, Factory, Flyweight, Proxy, Strategy, Observer
│   ├── threads/        # Hilos y Productor-Consumidor
│   ├── ui/             # Interfaz gráfica Swing
│   └── util/           # Utilidades (generadores, validadores)
├── data/               # Archivos persistentes y tickets generados
├── docs/               # Reporte LaTeX, diagramas UML, manual de usuario
├── .gitignore
└── README.md
```

---

## Instalación y ejecución

### Requisitos previos
- [JDK 21](https://www.oracle.com/java/technologies/downloads/) o superior
- [Apache NetBeans 21](https://netbeans.apache.org/) (opcional, recomendado)

### Opción A — Desde NetBeans
```bash
git clone https://github.com/<usuario>/Aeroviajes.git
```
1. Abrir el proyecto en NetBeans (`File → Open Project`).
2. Ejecutar con `F6` o el botón **Run Project**.

### Opción B — Desde la línea de comandos
```bash
git clone https://github.com/<usuario>/Aeroviajes.git
cd Aeroviajes
javac -d build/classes $(find src -name "*.java")
java -cp build/classes aeroviajes.Main
```

---

## Guía de uso

1. **Inicio:** al ejecutar, se muestra el menú principal con opciones de registro, inicio de sesión y acceso como administrador.
2. **Registro:** un nuevo cliente ingresa nombre, apellido y correo; el sistema genera automáticamente una contraseña segura.
3. **Cliente:** tras iniciar sesión, puede consultar vuelos, comprar boletos y descargar tickets.
4. **Administrador:** accede a un menú para gestionar vuelos y usuarios del sistema.

> **Credenciales de administrador por defecto:** se documentan en el manual de usuario (`docs/manual-usuario.pdf`).

---

## Documentación

| Documento | Ubicación |
|-----------|-----------|
| Reporte técnico (LaTeX) | `docs/reporte/reporte.pdf` |
| Diagramas UML | `docs/diagrams/` |
| Manual de usuario | `docs/manual-usuario.pdf` |
| Javadoc | _(generado opcionalmente)_ |

---

## Equipo

| Integrante | GitHub |
|------------|--------|
| **Francisco José Coutiño Morales** | [Mi GitHub](https://github.com/FranciscoCou077) |
| **Ernesto Flamenco Villaseñor** |  [Su GitHub](https://github.com/ernestogoretzka) |

---

## Contexto académico

Proyecto final de la asignatura **Programación Orientada a Objetos**.

- **Periodo:** Semestre 2026-2
- **Modalidad:** Proyecto en equipo (2 integrantes)
- **Objetivo:** Integrar la totalidad de conceptos del curso en una aplicación real, completa y profesional.

---

## Licencia

Este proyecto se distribuye bajo la licencia **MIT**. Consulta el archivo [`LICENSE`](LICENSE) para más detalles.

<div align="center">

<br>

*Desarrollado con JAVA y dedicación como proyecto académico y pieza de portafolio.*

</div>
