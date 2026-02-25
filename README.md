
# Final Project — Advanced Programming (Java Web)
Web system developed for the **Advanced Programming** course. It allows managing **users, products, and orders** with two roles: **Admin** and **User**.

Repository: https://github.com/RachidEsper/TpFinalProgAvanzada/tree/main

---

## 🧩 Tech Stack
- **Backend:** Java Web (Servlets, JSP)
- **Persistence:** JDBC
- **Database:** MySQL
- **Server:** Apache Tomcat
- **Architecture / Patterns:** DAO, Singleton, layered separation of concerns
- **Frontend:** Bootstrap + HTML + CSS + JavaScript

---

## ✅ Features (Use Cases)

### 👤 Admin
- Create user
- Edit user
- List users
- Delete user *(only if the user has no active orders)*
- Add product
- View products
- Edit product
- Change order status *(between “In Process” and “Finished”)*

### 🛒 User
- Authenticate (login)
- View products
- Add products to the current order *(draft / in-progress cart)*
- Start order *(moves to “In Process” and can no longer be edited until completion)*
- View order history

---

## 📌 Notes
- The **Admin** manages and supervises users/products/orders.
- The **User** only manages their own order and views history.
- The order flow involves both roles: the **User starts** an order and the **Admin updates** its status.

---

## 🗂️ Architecture / Structure Approach
- Layered separation of responsibilities (e.g., **DAO / Service / Web**)
- Data access through **DAO** (JDBC)
- Shared configuration/resources using **Singleton**
- JSP views using a customized **Bootstrap** template

---

## ⚙️ Requirements
- **JDK 17+**
- **Apache Tomcat 10/11**
- **MySQL 8+**
- Recommended IDE: **IntelliJ IDEA / Eclipse**

---

# ▶️ How to Run (English)

## 1) Database (MySQL)
1. Create a database, for example:
   - `tp_final_prog_avanzada`
2. Import the SQL schema/data **if the repository includes an `.sql` file**.
   - Look for something like: `*.sql` (e.g., `schema.sql`, `database.sql`, `dump.sql`)
3. Create a MySQL user (or use your local one) and grant permissions.

> If there is no SQL script in the repo, you must create the tables manually or export/import your local schema.

## 2) Configure JDBC Connection
Find where the project stores database settings (common places):
- A `.properties` file (recommended), or
- Hardcoded in a Java class (e.g., `ConnectionFactory`, `DBConnection`, `SingletonConnection`, etc.)

Update:
- host / port
- database name
- username
- password

Typical JDBC URL example:
- `jdbc:mysql://localhost:3306/tp_final_prog_avanzada?useSSL=false&serverTimezone=UTC`

Also make sure the MySQL driver is available:
- `mysql-connector-j` (JDBC driver)

## 3) Deploy to Tomcat
### Option A — Run from IDE (recommended)
1. Open/import the project in IntelliJ/Eclipse.
2. Add a **Tomcat** run configuration.
3. Deploy the application (WAR or exploded artifact).
4. Start the server.

### Option B — Build a WAR and deploy manually
1. Build/export the `.war` (via your build tool or IDE).
2. Copy it to:
   - `TOMCAT_HOME/webapps/`
3. Start Tomcat:
   - Windows: `bin/startup.bat`
   - Linux/Mac: `bin/startup.sh`

## 4) Open in the Browser
- `http://localhost:8080/<context-path>/`

`<context-path>` is usually the WAR/project name.

## 5) Default Users (Optional)
If your database includes seeded users, document them here:

- Admin: `...` / `...`
- User: `...` / `...`

---


# TP Final – Programación Avanzada (Java Web)
Sistema web desarrollado para la materia **Programación Avanzada**. Permite gestionar **usuarios, productos y pedidos** con roles **Administrador** y **Usuario**.

Repositorio: https://github.com/RachidEsper/TpFinalProgAvanzada/tree/main

---

## 🧩 Tecnologías utilizadas
- **Backend:** Java Web (Servlets, JSP)
- **Persistencia:** JDBC
- **Base de datos:** MySQL
- **Servidor:** Apache Tomcat
- **Patrones / Arquitectura:** DAO, Singleton, división de responsabilidades por capas
- **Frontend:** Bootstrap + HTML + CSS + JavaScript

---

## ✅ Funcionalidades (Casos de Uso)

### 👤 Administrador
1. **Crear usuario**
2. **Editar usuario**
3. **Listar usuarios**
4. **Borrar usuario** *(solo si no tiene pedidos activos)*
5. **Cargar producto**
6. **Ver productos**
7. **Editar producto**
8. **Cambiar estado de pedido** *(entre “En Proceso” y “Finalizado”)*

### 🛒 Usuario
9. **Autenticarse en el sistema**
10. **Ver productos**
11. **Agregar producto al pedido** *(pedido en curso)*
12. **Iniciar pedido** *(pasa a “En Proceso” y ya no se puede editar hasta finalización)*
13. **Listar pedidos históricos**

📌 **Notas:**
- El Administrador gestiona y supervisa usuarios/productos/pedidos.
- El Usuario solo gestiona su pedido y consulta su historial.
- El flujo de pedidos involucra ambos roles: el **Usuario inicia** y el **Administrador actualiza estados**.

---

## 🗂️ Estructura / Enfoque de arquitectura
- Separación de responsabilidades en capas (ej.: **DAO / Service / Web**)
- Acceso a datos mediante **DAO** (JDBC)
- Configuración/recursos compartidos con patrón **Singleton**
- Vistas con **JSP** + plantilla Bootstrap

---

## ⚙️ Requisitos
- **JDK 17+** 
- **Apache Tomcat 10/11** 
- **MySQL 8+**
- IDE recomendado: IntelliJ IDEA / Eclipse


# ▶️ Cómo correrlo (Español)

## 1) Base de datos (MySQL)
1. Crear una base de datos, por ejemplo:
   - `tp_final_prog_avanzada`
2. Importar el schema/datos **si el repositorio trae un archivo `.sql`**.
   - Buscar archivos como: `*.sql` (ej.: `schema.sql`, `database.sql`, `dump.sql`)
3. Crear un usuario en MySQL (o usar el local) y dar permisos a la base.

> Si el repo no incluye un script SQL, vas a tener que crear las tablas a mano o exportar/importar tu esquema local.

## 2) Configurar la conexión JDBC
Buscar dónde están las credenciales/configuración (lugares típicos):
- Un archivo `.properties`, o
- En una clase Java (ej.: `ConnectionFactory`, `DBConnection`, `SingletonConnection`, etc.)

Actualizar:
- host / puerto
- nombre de base
- usuario
- contraseña

Ejemplo típico de URL JDBC:
- `jdbc:mysql://localhost:3306/tp_final_prog_avanzada?useSSL=false&serverTimezone=UTC`

Además, asegurarse de tener el driver de MySQL:
- `mysql-connector-j`

## 3) Desplegar en Tomcat
### Opción A — Correr desde el IDE (recomendado)
1. Abrir/importar el proyecto en IntelliJ/Eclipse.
2. Configurar un **Tomcat** como Run Configuration.
3. Deployar la app (WAR o exploded).
4. Levantar el servidor.

### Opción B — Generar un WAR y desplegar manualmente
1. Generar/exportar el archivo `.war` (con el build tool o desde el IDE).
2. Copiarlo a:
   - `TOMCAT_HOME/webapps/`
3. Iniciar Tomcat:
   - Windows: `bin/startup.bat`
   - Linux/Mac: `bin/startup.sh`

## 4) Abrir en el navegador
- `http://localhost:8080/<context-path>/`

`<context-path>` suele ser el nombre del proyecto/WAR.

## 5) Usuarios por defecto (Opcional)
Si tu base tiene usuarios precargados, documentalos acá:

- Admin: `...` / `...`
- Usuario: `...` / `...`

---

## 🔧 Troubleshooting / Problemas comunes
- **Tomcat 10/11** uses **Jakarta** namespaces (`jakarta.servlet.*`). Ensure your dependencies/project are aligned with your Tomcat version.
- If you get connection errors, verify:
  - MySQL is running
  - DB name/user/password are correct
  - JDBC URL parameters
  - MySQL driver is included in the project/build

---

## 📄 License
Educational project (coursework).

