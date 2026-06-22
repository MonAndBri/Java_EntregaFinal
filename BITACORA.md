# 📑 Bitácora de Desarrollo: API REST E-Commerce

Este documento registra de forma cronológica y técnica todos los hitos, refactorizaciones, migraciones y optimizaciones estructurales realizadas durante el desarrollo del backend de la aplicación utilizando **Spring Boot**, **JPA/Hibernate** y **MySQL**.

---

## 🚀 Hito 1: Inicialización y CRUD en Memoria

### 📁 Arquitectura Base del Proyecto
* Se inicializó el proyecto directamente desde el entorno de desarrollo **VS Code**.
* Se estructuró el código bajo el patrón de diseño arquitectónico de capas creando los siguientes paquetes:
  * `com.techlab.ecommerce.model` (Entidades de datos).
  * `com.techlab.ecommerce.service` (Lógica de negocio).
  * `com.techlab.ecommerce.controller` (Capa de exposición HTTP).
  * `com.techlab.ecommerce.exception` (Gestión de errores).

### 🛠️ Migración de Lógica Base y Adaptaciones
* Se migraron los archivos de lógica pura de Java hacia sus respectivos paquetes en el ecosistema Spring, corrigiendo declaraciones de `package` e `imports`:
  * `Producto.java` ➡️ `model/`
  * `ProductoService.java` ➡️ `service/`
  * `ProductoNoEncontradoException.java` y `StockInsuficienteException.java` ➡️ `exception/`
* Se decoró la capa de servicios con la anotación `@Service`, implementando la inyección de dependencias por constructor.

### 🌐 Exposición de la API y Endpoints CRUD (Productos)
* Se implementó `ProductoController.java` anotado con `@RestController` y `@RequestMapping("/productos")`.
* Se desarrolló el CRUD completo operando sobre colecciones en memoria (`ArrayList`):
  * **`GET /productos`**: Lista completa de elementos.
  * **`GET /productos/{id}`**: Búsqueda específica utilizando `@PathVariable`, aprovechando la serialización automática de objetos Java a JSON gracias a los getters de la entidad.
  * **`POST /productos`**: Inserción de nuevos registros mediante `@PostMapping`, usando `@RequestBody` para interceptar y transformar el JSON del cuerpo de la petición en un objeto Java.
  * **`PUT /productos/{id}`**: Modificación combinando `@PathVariable` para identificar el recurso y `@RequestBody` para los datos a sobreescribir.
  * **`DELETE /productos/{id}`**: Baja de recursos mediante `@DeleteMapping`.

### 🔄 Control de Respuestas y Configuración Inicial
* **`ResponseEntity<T>`**: Se refactorizaron todos los métodos del controlador para retornar envoltorios de respuestas explícitos, garantizando un control absoluto sobre las cabeceras HTTP y los códigos de estado (`200 OK`, `201 Created`, `404 Not Found`).
* **Datos de Prueba**: Se adaptó `EcommerceApplication.java` implementando `CommandLineRunner` y la anotación `@Bean` para ejecutar una precarga automatizada de productos (Café, Yerba, Galletitas) al levantar el servidor de Tomcat.
* **Estandarización de Git**: Se configuró de forma global la propiedad `core.autocrlf true` para estandarizar los saltos de línea (`LF` / `CRLF`) evitando conflictos entre entornos Windows y Linux, salvando el hito con el primer commit del repositorio.

---

## 📦 Hito 2: Implementación del Modelo Categoría y Errores de Negocio

### 📐 Estructura del Módulo de Categorías
Se replicó de forma consistente la arquitectura por capas para dar soporte a las categorías de la plataforma:
* **Modelo (`model/`)**: Creación de la clase `Categoria` (atributos: `id`, `nombre`, `descripcion`) incorporando el constructor explícito por defecto sin argumentos, preparando el terreno para la posterior migración a un mapeo objeto-relacional.
* **Servicio (`service/`)**: Desarrollo de `CategoriaService` encargado de procesar las transacciones del ABML en memoria y aislar la lógica de autoincremento de identificadores primarios.
* **Controlador (`controller/`)**: Implementación de `CategoriaController` utilizando respuestas tipadas y homogéneas con `ResponseEntity<T>`.

### ⚠️ Manejo de Excepciones de Negocio Específiones
Se expandió el paquete `exception/` para validar flujos alternativos en la capa de servicios:
* **`CategoriaNombreInvalidoException`**: Disparada para frenar inserciones o modificaciones en caso de campos nulos o vacíos en el payload.
* **`CategoriaNoEncontradaException`**: Intercepta intentos de búsqueda o manipulación de IDs inexistentes, mapeando la respuesta HTTP correspondiente de recurso no hallado.

---

## 🗄️ Hito 3: Persistencia Real con Spring Data JPA y MySQL

### 🗺️ Mapeo Objeto-Relacional (ORM)
Se transformaron los modelos en memoria en entidades de base de datos relacional mediante **JPA / Hibernate**:
* Las clases se decoraron con `@Entity` y se vincularon a sus respectivas tablas mediante `@Table(name = "productos")` y `@Table(name = "categorias")`.
* Se removieron los contadores manuales de ID. Se configuraron las claves primarias delegando la estrategia de indexación al motor MySQL a través de `@Id` y `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
* **Modelado de Relaciones**: Se estableció un vínculo estructural de Muchos a Uno (**`@ManyToOne`**) en la clase `Producto`, inyectando el objeto `Categoria` bajo la anotación `@JoinColumn(name = "categoria_id")` para constituir la clave foránea (FK) en la base de datos.

| Tabla: PRODUCTOS (Muchos) | Relación | Tabla: CATEGORIAS (Uno) |
| :--- | :---: | :--- |
| `id (PK)` <br> `nombre` <br> `precio` <br> `stock` <br> **`categoria_id (FK)`** | *** ➡️ 1** | `id (PK)` <br> `nombre` <br> `descripcion` |

### 🛰️ Capa de Repositorios y Desacoplamiento de Memoria
* Se crearon las interfaces `ProductoRepository` y `CategoriaRepository` extendiendo de `JpaRepository<T, ID>`.
* Se eliminaron definitivamente las colecciones `ArrayList` de los servicios.
* Se inyectaron los componentes repositorios en los servicios, sustituyendo la lógica imperativa por llamadas directas a métodos nativos de Spring Data como `.save()`, `.findAll()`, y `.findById()`.

### 🔧 Depuración de Base de Datos y Dependencias (`pom.xml`)
* **Script SQL Especializado**: Se ejecutó el script `eliminar_tablas.sql` en la raíz del proyecto para aplicar sentencias `DROP TABLE` de manera ordenada en phpMyAdmin, permitiendo una recreación limpia de las restricciones transaccionales y relacionales por parte de Hibernate.
* **Ajustes de Maven**: Se agregaron al archivo `pom.xml` las dependencias de *Spring Data JPA* y el *Connector de MySQL*. Se actualizó la versión del parent de Spring Boot a la `4.1.0` y se ejecutó un *Maven Reload* para purgar las advertencias del entorno de desarrollo.
* **Persistencia en Cascada de Carga Inicial**: Se adaptó el `CommandLineRunner` en `EcommerceApplication.java` para sincronizar las inserciones iniciales en orden jerárquico estricto: primero se persistieron las categorías en MySQL para poblar sus IDs reales y posteriormente se asociaron a los productos cargados.

---

## 🔍 Hito 4: Consultas Avanzadas, Validación Declarativa y Manejo Global de Errores

### 🗺️ Query Methods y Consultas JPQL
Se dotó a `ProductoRepository` de capacidades analíticas personalizadas:
* **Query Method**: Se implementó `findByNombreContaining(String nombre)` para delegar la generación de consultas automatizadas con la cláusula `LIKE %texto%` a nivel de base de datos.
* **Consulta JPQL (`@Query`)**: Se diseñó el método `buscarPorCategoria` utilizando sintaxis orientada a objetos en lugar de SQL nativo, empleando parámetros nombrados (`@Param`) para filtrar los productos mediante la navegación de propiedades (`p.categoria.nombre`) sin necesidad de codificar un `JOIN` explícito de forma manual.

### 🛡️ Mapeo de Validación Declarativa (Jakarta Bean Validation)
* Se barrió toda la lógica condicional basada en estructuras distributivas `if` de la capa de servicios.
* Se implementaron restricciones de integridad directas mediante metadatos en las clases del modelo (`model/`):
  * **`@NotBlank`**: Bloquea strings vacíos, nulos o compuestos únicamente por espacios en atributos de texto.
  * **`@Positive` / `@PositiveOrZero`**: Garantizan consistencia matemática impidiendo la carga de precios o stocks negativos.
* Se habilitó la validación en cascada anteponiendo la anotación **`@Valid`** a los parámetros de entrada interceptados por `@RequestBody` en los métodos de inserción (`POST`) y edición (`PUT`) de los controladores.

### 🧠 Interceptor de Excepciones Centralizado (`@RestControllerAdvice`)
* Se construyó la clase `GlobalExceptionHandler.java` anotada con `@RestControllerAdvice` para capturar errores de manera transversal en toda la aplicación.
* **Manejo Genérico de Validaciones**: Se estructuró un método `@ExceptionHandler` enfocado en `MethodArgumentNotValidException` (excepción que se dispara al fallar la validación de `@Valid`).
* Se programó un flujo de extracción dinámico mediante un mapa de datos (`Map<String, String>`) que recorre la colección de objetos `FieldError`, aislando el campo inválido junto con su correspondiente mensaje de error personalizado (`getDefaultMessage()`). El interceptor unifica las advertencias dispersas en un único JSON prolijo y estructurado, retornando códigos de estado `400 Bad Request`.

---

## 💎 Hito 5: Optimización de Arquitectura con Lombok y Desacoplamiento de Controladores

### 🏗️ Integración de Lombok (Eliminación de Código Boilerplate)
Se incorporó la librería **Lombok** en el `pom.xml` para remover cientos de líneas de código repetitivo de los modelos:
* **`Categoria.java`**: Refactorizado de forma masiva utilizando las anotaciones `@Data`, `@NoArgsConstructor` y `@AllArgsConstructor`. Lombok genera de forma invisible los Getters, Setters, `equals()`, `hashCode()` y `toString()`.
* **`Producto.java` (Diseño Seguro para JPA)**: Para evitar un error crítico de recursión infinita por bucles de lectura entre relaciones (`StackOverflowError`), se evitó el uso de la anotación `@Data`. En su lugar, se desglosó el comportamiento de forma selectiva mediante:
  * `@Getter` y `@Setter`
  * `@NoArgsConstructor` y `@AllArgsConstructor`
* **Constructores y Métodos Manuales de Control**:
  * Se mantuvieron manualmente en ambos modelos los constructores personalizados que prescinden del atributo `id` (autoincremental en MySQL), facilitando la instanciación limpia de entidades desde las capas superiores.
  * Se conservó la sobreescritura explísica del método `@Override toString()` en `Producto.java`, parametrizando únicamente el atributo string plano `categoria.getNombre()` para blindar la lectura transaccional de la API.

### ✂️ Desacoplamiento Absoluto: Eliminación Completa de Bloques Try-Catch
* Se eliminaron todos los bloques estructurados `try-catch` de `ProductoController.java` y `CategoriaController.java`.
* **Anidación de Funciones**: Los endpoints de la capa controladora se redujeron a su mínima expresión pasándole el retorno de la capa de servicios de forma directa a los métodos constructores de `ResponseEntity` (ej. `ResponseEntity.ok(service.obtenerPorId(id))`), optimizando la memoria local al descartar la declaración de variables intermedias redundantes.
* **Mapeo de Excepciones de Negocio**: Se actualizaron los manejadores de `@GlobalExceptionHandler` registrando explícitamente métodos para interceptar las excepciones personalizadas (`ProductoNoEncontradoException`, `CategoriaNoEncontradaException`, etc.). Ahora las excepciones vuelan libres hacia arriba, siendo capturadas en el aire por el interceptor. Esto dota al backend de una **Separación de Incumbencias (Separation of Concerns)** absoluta y profesional.

### 🔒 Activación del Mecanismo CORS
* Se incorporó de manera preventiva la anotación **`@CrossOrigin(origins = "http://localhost:5500")`** en la cabecera de ambos controladores. Esta adición permite accesos cruzados explícitos, protegiendo a la API REST de bloqueos de seguridad por políticas de origen en los navegadores web cuando se acople el código del Frontend en las próximas fases del proyecto.

### 🚦 Verificación Final de No-Regresión
* Se realizaron testeos de regresión de extremo a extremo (End-to-End) sobre el 100% de los endpoints utilizando **Thunder Client**.
* Se comprobó que tanto los flujos exitosos como las respuestas estructuradas de error operan de forma idéntica, habiendo reducido las líneas de código de los controladores en más de un 40%. El proyecto queda catalogado con arquitectura limpia, robusta y escalable.