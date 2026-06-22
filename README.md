# 🛒 E-Commerce API REST — TechLab Backend

API REST robusta y escalable desarrollada con **Spring Boot 3**, diseñada para gestionar de forma integral un inventario de productos agrupados por categorías, con persistencia real en una base de datos relacional **MySQL**.

---

## 📑 Tabla de Contenidos

- [Tecnologías utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del proyecto](#-arquitectura-del-proyecto)
- [Requisitos previos](#-requisitos-previos)
- [Configuración y despliegue](#-configuración-y-despliegue)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Validaciones y manejo de errores](#-validaciones-y-manejo-de-errores)
- [Características destacadas](#-características-destacadas)
- [Ejemplos de uso](#-ejemplos-de-uso)
- [Contribuir](#-contribuir)

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Rol |
|---|---|---|
| Java | 17+ | Lenguaje principal |
| Spring Boot | 3.4.1 | Framework base |
| Spring Data JPA / Hibernate | — | ORM y persistencia |
| MySQL Driver | — | Conector de base de datos |
| Jakarta Bean Validation | — | Validación declarativa |
| Project Lombok | — | Reducción de código boilerplate |
| Maven | — | Gestión de dependencias y build |

---

## 📁 Arquitectura del proyecto

El proyecto sigue una arquitectura en capas con responsabilidades bien delimitadas:

```
ecommerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/techlab/ecommerce/
│   │   │       ├── controller/          # Exposición HTTP (endpoints REST)
│   │   │       │   ├── CategoriaController.java
│   │   │       │   └── ProductoController.java
│   │   │       ├── exception/           # Excepciones de negocio y handler global
│   │   │       │   ├── CategoriaNoEncontradaException.java
│   │   │       │   ├── CategoriaNombreInvalidoException.java
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── PrecioInvalidoException.java
│   │   │       │   ├── ProductoNoEncontradoException.java
│   │   │       │   └── StockInsuficienteException.java
│   │   │       ├── model/               # Entidades JPA
│   │   │       │   ├── Categoria.java
│   │   │       │   └── Producto.java
│   │   │       ├── repository/          # Acceso a datos (Spring Data JPA)
│   │   │       │   ├── CategoriaRepository.java
│   │   │       │   └── ProductoRepository.java
│   │   │       ├── service/             # Lógica de negocio
│   │   │       │   ├── CategoriaService.java
│   │   │       │   └── ProductoService.java
│   │   │       └── EcommerceApplication.java
│   │   └── resources/
│   │       └── application.properties   # Configuración de servidor, JPA y MySQL
├── eliminar_tablas.sql                  # Script auxiliar de limpieza en DB
└── pom.xml                              # Dependencias Maven
```

---

## ✅ Requisitos previos

Antes de levantar el proyecto, asegurate de tener instalado:

- **JDK 17** o superior → [Descargar](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** (o usar el wrapper `./mvnw` incluido)
- **MySQL** corriendo localmente (ej: via [XAMPP](https://www.apachefriends.org/) o [Laragon](https://laragon.org/))
- Un cliente HTTP para probar endpoints: [Postman](https://www.postman.com/) o Thunder Client (extensión de VS Code)

---

## 🚀 Configuración y despliegue

### 1. Clonar el repositorio

```bash
git clone https://github.com/MonAndBri/Java_EntregaFinal.git
cd ecommerce-techlab
```

### 2. Configurar la base de datos

Abrí **phpMyAdmin** (o tu cliente MySQL preferido) y creá una base de datos vacía:

```sql
CREATE DATABASE ecommerce;
```

> **Nota:** Si ya existían tablas de versiones anteriores que generan conflictos con las claves foráneas, podés ejecutar el script de limpieza antes de iniciar:
> ```bash
> mysql -u root -p ecommerce < eliminar_tablas.sql
> ```
> Hibernate regenerará las tablas automáticamente al iniciar.

### 3. Ajustar credenciales

Editá el archivo `src/main/resources/application.properties` con tus datos de acceso:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Ejecutar la aplicación

**Desde VS Code:** Abrí `EcommerceApplication.java` y presioná **Run** (o `F5`).

**Desde terminal:**

```bash
mvn spring-boot:run
```

El servidor Tomcat embebido se levantará en `http://localhost:8080`. Al iniciar, un `CommandLineRunner` cargará automáticamente datos de prueba en las tablas.

---

## 📋 Endpoints de la API

URL base: `http://localhost:8080`

### 📦 Productos `/productos`

| Método | Endpoint | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/productos` | Lista todos los productos con sus categorías | `200 OK` |
| `GET` | `/productos/{id}` | Obtiene un producto por ID | `200 OK` · `404 Not Found` |
| `POST` | `/productos` | Crea un nuevo producto (requiere body válido) | `201 Created` · `400 Bad Request` |
| `PUT` | `/productos/{id}` | Actualiza un producto existente | `200 OK` · `400 Bad Request` · `404 Not Found` |
| `DELETE` | `/productos/{id}` | Elimina un producto del sistema | `204 No Content` · `404 Not Found` |
| `GET` | `/productos/nombre/{n}` | Filtra productos cuyo nombre contenga el texto | `200 OK` |
| `GET` | `/productos/categoria/{c}` | Filtra productos por nombre de categoría | `200 OK` |

### 🗂️ Categorías `/categorias`

| Método | Endpoint | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/categorias` | Lista todas las categorías | `200 OK` |
| `GET` | `/categorias/{id}` | Obtiene una categoría por ID | `200 OK` · `404 Not Found` |
| `POST` | `/categorias` | Crea una nueva categoría | `201 Created` · `400 Bad Request` |
| `PUT` | `/categorias/{id}` | Actualiza una categoría existente | `200 OK` · `400 Bad Request` · `404 Not Found` |
| `DELETE` | `/categorias/{id}` | Elimina una categoría | `204 No Content` · `404 Not Found` |

---

## 🛡️ Validaciones y manejo de errores

### Validaciones de modelo

Las entidades aplican restricciones declarativas con Jakarta Bean Validation:

| Anotación | Campo aplicado | Regla |
|---|---|---|
| `@NotBlank` | `nombre` (Producto y Categoría) | No puede estar vacío ni contener solo espacios |
| `@Positive` | `precio` | Debe ser un número mayor a cero |
| `@PositiveOrZero` | `stock` | Debe ser cero o un número positivo |

### Respuesta ante validaciones fallidas

Si se envía un payload con valores inválidos, la API responde `400 Bad Request` con un mapa de errores estructurado:

**Request — `POST /productos`:**
```json
{
  "nombre": "",
  "precio": -150.0,
  "stock": 10
}
```

**Response — `400 Bad Request`:**
```json
{
  "nombre": "El nombre del producto no puede estar vacío",
  "precio": "El precio debe ser un número positivo mayor a cero"
}
```

### Manejo global de excepciones

Todas las excepciones de negocio son interceptadas por `GlobalExceptionHandler` (`@RestControllerAdvice`), eliminando bloques `try-catch` en los controladores y garantizando respuestas JSON consistentes en toda la API.

---

## ✨ Características destacadas

**Persistencia real transaccional** — Migrado de arreglos en memoria a tablas relacionales en MySQL mediante `JpaRepository`.

**Validación declarativa** — Restricciones de integridad en los modelos que blindan la base de datos contra payloads corruptos o matemáticamente incoherentes.

**Handler global de excepciones** — `@RestControllerAdvice` centraliza el manejo de errores; los controladores no tienen `try-catch`, lo que resulta en código más limpio y mantenible.

**CORS habilitado** — Configurado con `@CrossOrigin` para integrarse con frontends corriendo en `http://localhost:5500` (Live Server / VS Code).

**Precarga automática** — Un `CommandLineRunner` inyecta datos iniciales de forma jerárquica al arrancar el servidor, facilitando pruebas inmediatas sin configuración manual.

---

## 💡 Ejemplos de uso

### Crear una categoría

```http
POST /categorias
Content-Type: application/json

{
  "nombre": "Bebidas"
}
```

### Crear un producto

```http
POST /productos
Content-Type: application/json

{
  "nombre": "Agua Mineral",
  "precio": 1700.0,
  "stock": 25,
  "categoria": { "id": 1 }
}
```

### Buscar productos por categoría

```http
GET /productos/categoria/Bebidas
```

### Actualizar stock de un producto

```http
PUT /productos/3
Content-Type: application/json

{
  "nombre": "Agua Mineral",
  "precio": 1700.0,
  "stock": 10,
  "categoria": { "id": 1 }
}
```

---

> Desarrollado con ☕ y Spring Boot por **Mónica Andrea Brito**.
