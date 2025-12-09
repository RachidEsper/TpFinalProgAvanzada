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

