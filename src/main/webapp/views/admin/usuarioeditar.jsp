<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.Usuario"%>
<%
String ctx = request.getContextPath();
Usuario u = (Usuario) request.getAttribute("usuario");

String globalError = (String) request.getAttribute("globalError");
%>

<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
    <div>
        <h1 class="h3 mb-0">Editar Usuario</h1>
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="<%=ctx%>/views/base.jsp">Inicio</a></li>
            <li class="breadcrumb-item"><a href="<%=ctx%>/UsuarioListarServlet">Usuarios</a></li>
            <li class="breadcrumb-item active">Editar</li>
        </ol>
    </div>
</div>

<% if (globalError != null) { %>
<div class="alert alert-danger"><%=globalError%></div>
<% } %>

<div class="card shadow-sm mb-4">
    <div class="card-header">
        <i class="fas fa-user-edit me-1"></i> Datos del usuario
    </div>
    <div class="card-body">
        <form method="post" action="<%=ctx%>/UsuarioEditarServlet" novalidate>
            <input type="hidden" name="id" value="<%=u.getIdUsuario()%>"/>

            <div class="row g-3">
                <!-- Nombre -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="text" class="form-control" id="nombre"
                               name="nombre" placeholder="Nombre"
                               value="<%=u.getNombre()%>" required>
                        <label for="nombre">Nombre *</label>
                    </div>
                </div>

                <!-- Email -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="email" class="form-control" id="email"
                               name="email" placeholder="Email"
                               value="<%=u.getEmail()%>" required>
                        <label for="email">Email *</label>
                    </div>
                </div>

                <!-- Contraseña -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="password" class="form-control" id="password"
                               name="password" placeholder="Contraseña">
                        <label for="password">Contraseña (dejar vacío para no cambiar)</label>
                    </div>
                </div>

                <!-- Teléfono -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="text" class="form-control" id="telefono"
                               name="telefono" placeholder="Teléfono"
                               value="<%=u.getTelefono()%>">
                        <label for="telefono">Teléfono</label>
                    </div>
                </div>

                <!-- Tipo de usuario -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <select class="form-select" id="idTipoUsuario"
                                name="idTipoUsuario" required>
                            <option value="1" <%= (u.getTipo().getIdTipoUsuario() == 1 ? "selected" : "") %> >
                                1 - Administrador
                            </option>
                            <option value="2" <%= (u.getTipo().getIdTipoUsuario() == 2 ? "selected" : "") %> >
                                2 - Usuario Normal
                            </option>
                        </select>
                        <label for="idTipoUsuario">Tipo de Usuario *</label>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mt-4">
                <button type="button" class="btn btn-outline-secondary me-2"
                        onclick="window.location.href='<%=ctx%>/UsuarioListarServlet'">
                    Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    Guardar cambios
                </button>
            </div>
        </form>
    </div>
</div>
