\# Proyecto Final - Arquitectura de Microservicios con Spring Boot



Entrega: 5 de junio, 2026.



\## Integrantes ##



Martínez Santana Diego Maximiliano

Suárez Peñaloza Luis Alberto

&#x20;

\---



\# Descripción del Proyecto



Este proyecto implementa una arquitectura de microservicios utilizando Spring Boot, Spring Cloud, Eureka, Gateway, Config Server, Spring Security con JWT y Spring Boot Admin.



El sistema simula una plataforma de comercio electrónico compuesta por:



\* Auth Service (Autenticación y autorización)

\* Product Service (Administración de productos)

\* Invoice Service (aquí se implementó el carrito de compras y facturación)

\* Config Service (Configuración centralizada)

\* Registry Service (Eureka Discovery Server)

\* Gateway Service (API Gateway)

\* Admin Service (Monitoreo de microservicios)



\---



\# Arquitectura



Cliente

↓

Gateway Service

↓

┌─────────────┬─────────────┬─────────────┐

│ Auth Service│ Product Serv│ Invoice Serv│

└─────────────┴─────────────┴─────────────┘

↓

Eureka Registry

↓

Config Service



Todos los microservicios obtienen su configuración desde Config Server y se registran automáticamente en Eureka.





\# Tecnologías utilizadas



\* Java 21

\* Spring Boot

\* Spring Cloud Config

\* Spring Cloud Gateway

\* Spring Security

\* JWT

\* Spring Data JPA

\* MySQL

\* Eureka Discovery Server

\* Spring Boot Admin

\* Maven

\* GitHub

\* Postman



\---



\# Repositorios



\## Repositorio principal



\[https://github.com/DiegoI00/dwb-proyecto-final]



\## Repositorio Config Server



\[https://github.com/DiegoI00/config-data-dwb-final]



\---



\# Bases de Datos



Importar los siguientes scripts de SQL:



database.sql



Incluidos dentro de la carpeta:



database/



\# Orden de ejecución



Es importante ejecutar los servicios en el siguiente orden:



\## 1. Config Service



Puerto:



8888



Verificar:



http://localhost:8888



\## 2. Registry Service (Eureka)



Puerto:



8761



Verificar:



http://localhost:8761





\## 3. Admin Service



Puerto:



9090



Verificar:



http://localhost:9090 debe salir todo arriba



\---



\## 4. Auth Service



Puerto:



8082



\---



\## 5. Product Service



Puerto:



8081



\---



\## 6. Invoice Service



Puerto:



8084



\---



\## 7. Gateway Service



Puerto:



8080



\---



\# Verificación de Eureka



Ingresar a:



http://localhost:8761



Deben aparecer registrados:



\* AUTH-SERVICE

\* PRODUCT

\* INVOICE-SERVICE

\* GATEWAY-SERVICE

\* ADMIN-SERVICE



Estado esperado:



UP



\---



\# Verificación de Spring Boot Admin



Ingresar a:



http://localhost:9090



Todos los servicios deben aparecer en estado:



UP



\---



\# Flujo de pruebas en Postman



\## 1. Login



POST



http://localhost:8080/auth-service/login



en Body pones lo de tu base de datos, ejemplo:



```json

{

&#x20; "username": "admin",

&#x20; "password": "Admin123!"

}

```



Respuesta esperada:



```json

{

&#x20; "token": "..."

}

```



Copiar el token.



\---



\## 2. Consultar productos



GET



http://localhost:8080/product-service/product



Header:



Authorization: Bearer TOKEN



\---



\## 3. Consultar producto específico



GET



http://localhost:8080/product-service/product/1



Header:



Authorization: Bearer TOKEN



\---



\## 4. Agregar producto al carrito



POST



http://localhost:8080/invoice-service/cart-item



Header:



Authorization: Bearer TOKEN



Body:



```json

{

&#x20; "product\_id": 1,

&#x20; "quantity": 1

}

```



\---



\## 5. Consultar carrito



GET



http://localhost:8080/invoice-service/cart-item



Header:



Authorization: Bearer TOKEN



\---



\## 6. Generar factura



POST



http://localhost:8080/invoice-service/invoice



Header:



Authorization: Bearer TOKEN



Body:



```json

{}

```



Respuesta esperada:



```json

{

&#x20; "message": "La factura ha sido registrada"

}

```



\---



\## 7. Consultar facturas



GET



http://localhost:8080/invoice-service/invoice



Header:



Authorization: Bearer TOKEN



\---



\## 8. Verificar actualización de inventario



Consultar nuevamente:



GET



http://localhost:8080/product-service/product/1



y verificar que el stock disminuyó después de generar la factura.



\---



\# Características implementadas



Arquitectura basada en microservicios



Configuración centralizada mediante Config Server



Descubrimiento de servicios mediante Eureka



API Gateway



Seguridad con JWT



Roles y permisos



Gestión de productos



Carrito de compras



Facturación



Actualización automática de inventario



Monitoreo con Spring Boot Admin



Persistencia con MySQL



\---



Saludos.

