# Laboratorio – Parte 2: BluePrints API con Seguridad JWT (OAuth 2.0)

Este laboratorio extiende la **Parte 1** ([Lab_P1_BluePrints_Java21_API](https://github.com/SantiagoSu15/Lab-4-ARSW.git)) agregando **seguridad a la API** usando **Spring Boot 3, Java 21 y JWT (OAuth 2.0)**.  
El API se convierte en un **Resource Server** protegido por tokens Bearer firmados con **RS256**.  
Incluye un endpoint didáctico `/auth/login` que emite el token para facilitar las pruebas.

## Comenzando


### Requisitos

- JDK 21
- Maven 3.9+
- Git



### Instalacion

1. Clonar o descomprimir el proyecto:
   ```bash
   git clone https://github.com/DECSIS-ECI/Lab_P2_BluePrints_Java21_API_Security_JWT.git
   cd Lab_P2_BluePrints_Java21_API_Security_JWT
   ```

2. Ejecutar con Maven:
   ```bash
   mvn -q -DskipTests spring-boot:run
   ```

3. Verificar que la aplicación levante en `http://localhost:8080`.

---
## Estructura del proyecto
```
src/main/java/co/edu/eci/blueprints/
  ├── api/BlueprintController.java       # Endpoints protegidos
  ├── auth/AuthController.java           # Login didáctico para emitir tokens
  ├── config/OpenApiConfig.java          # Configuración Swagger + JWT
  └── security/
       ├── SecurityConfig.java
       ├── MethodSecurityConfig.java
       ├── JwtKeyProvider.java
       ├── InMemoryUserService.java
       └── RsaKeyProperties.java
src/main/resources/
  └── application.yml
```
---


## Actividades

## 1. Revisar el código de configuración de seguridad

- `SecurityFilterChain`:Configuracion principal donde se definen reglas de autorizacion, y con la llamada a `authorizeHttpRequests` se define el acceso a los endpoints.

**Endpoints Publicos:**

Las rutas para acceder a swagger `v3/api-docs/`, `/swagger-ui/`, `/swagger-ui.html` son todos publicos sin necesidar de estar logueado
De igual manera las rutas `/actuator/health` y `/auth/login`  son publicas.

**Endpoints Protegidos:**
Acceder a cualquier que comience con `/api` requiere tener algun scope (`SCOPE_blueprints.read` o `SCOPE_blueprints.write`) para acceder.

Cualquier otra ruta requiere estar logueado.


- `passwordEncoder`: Manera que se encriptan las contraseñas
- `jwtDecoder`: Valida tokens
- `jwtEncoder`: Genera tokens

## 2. Explorar el flujo de login y analizar las claims del JWT emitido.

- Usuario hace post a `/auth/login` con credenciales correctas.
- A la clase `RsaKeyProperties` se pide el atributo tokenTtlSeconds, en caso de ser nulo se usa de lo contrario se pone por defecto 3600 segundos.
Esta clase es un record que lee las propiedades de blueprints.security en el archivo application.yml.
Spring mapea automaticamente las propiedades de la clase a los atributos del record.
- Se construye un objeto `JwtClaimsSet` que representa el contenido del token donde:
  - `iss`: es el emisor del token
  - `issuedAt`: fehca del token
  - `expiresAt`: fecha que expira el token
  - `subject`: Usuario que genera el token
  - `scope`: scopes que tiene el token (permisos)
- Se construye un objeto `JwtHeader` que representa el encabezado del token con un algoritmo de firma: `RS256`
- Se firma y genera  el token con la cabecera y el claims y devolviendolo en formato String
- Finalmente se devuelve una respuesta HTTP 200 con el token en el body.


## 3. Extender los scopes 
Se ha vuelto a agregar los archivos pasados del laboratorio 4 para poder tener la configuracion y endpoints de los blueprints.

Por ende se ha vuelto a tener una base en postgre y se realizaron cambios en el archivo application.yml para poder tener la configuracion de la base de datos.


**blueprints.read**
Los endpoints que se pueden acceder con el scope `blueprints.read` son:
- `/blueprints`
- `/blueprints/{author}`
- `/blueprints/{author}/{bpname}`

Evidencias

Autenticacion en swagger tras loggin con el token emitido.
![Imagen1](/docs/1.png)

Validado
![Imagen2](/docs/2.png)

Prueba de endpoint con get (Endpoint del lab5)
![Imagen3](/docs/3.png)

Prueba de endpoint con get (Endpoint del lab4) (vacio por que no hay datos en la BD de Postgre)
![Imagen4](/docs/4.png)

## Authors

* **Juan Rangel** & **Santiago Suarez**

