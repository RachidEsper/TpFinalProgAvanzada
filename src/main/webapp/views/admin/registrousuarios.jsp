<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String ctx = request.getContextPath();

String nombreVal = (String) request.getAttribute("nombre");
String emailVal  = (String) request.getAttribute("email");
String telVal    = (String) request.getAttribute("telefono");
String tipoVal   = (String) request.getAttribute("idTipoUsuario");

String globalError  = (String) request.getAttribute("globalError");
String flashSuccess = (String) session.getAttribute("flashSuccess");
%>

<% if (flashSuccess != null) { %>
<div class="alert alert-success alert-dismissible fade show" role="alert">
    <%=flashSuccess%>
    <button type="button" class="btn-close" data-bs-dismiss="alert"
        aria-label="Close"></button>
</div>
<%
    session.removeAttribute("flashSuccess");
}
%>

<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
    <div>
        <h1 class="h3 mb-0">Alta de Usuario</h1>
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item">
                <a href="<%=ctx%>/views/base.jsp">Inicio</a>
            </li>
            <li class="breadcrumb-item">
                <a href="<%=ctx%>/UsuarioListarServlet">Usuarios</a>
            </li>
            <li class="breadcrumb-item active">Nuevo</li>
        </ol>
    </div>
</div>

<% if (globalError != null) { %>
<div class="alert alert-danger"><%=globalError%></div>
<% } %>

<div class="card shadow-sm mb-4">
    <div class="card-header">
        <i class="fas fa-user-plus me-1"></i> Completar datos
    </div>
    <div class="card-body">
        <form method="post" action="<%=ctx%>/UsuarioCrearServlet" novalidate>
            <div class="row g-3">
                <!-- Nombre -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="text" class="form-control" id="nombre"
                            name="nombre" placeholder="Nombre"
                            value="<%=(nombreVal != null ? nombreVal : "")%>" required>
                        <label for="nombre">Nombre *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese el nombre.
                        </div>
                    </div>
                </div>

                <!-- Email -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="email" class="form-control" id="email"
                            name="email" placeholder="name@example.com"
                            value="<%=(emailVal != null ? emailVal : "")%>" required>
                        <label for="email">Email *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese un email válido.
                        </div>
                    </div>
                </div>

                <!-- Contraseña -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="password" class="form-control" id="password"
                            name="password" placeholder="Contraseña" required>
                        <label for="password">Contraseña *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese una contraseña (mínimo 4 caracteres).
                        </div>
                    </div>
                </div>

                <!-- Teléfono -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="text" class="form-control" id="telefono"
                            name="telefono" placeholder="Teléfono"
                            value="<%=(telVal != null ? telVal : "")%>">
                        <label for="telefono">Teléfono (opcional)</label>
                    </div>
                </div>

                <!-- Tipo de Usuario -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <select class="form-select" id="idTipoUsuario"
                            name="idTipoUsuario" required>
                            <option value=""
                                <%=(tipoVal == null || tipoVal.isBlank()) ? "selected" : ""%>
                                disabled>Seleccioná un tipo</option>
                            <option value="1" <%="1".equals(tipoVal) ? "selected" : ""%>>
                                1 - Administrador
                            </option>
                            <option value="2" <%="2".equals(tipoVal) ? "selected" : ""%>>
                                2 - Usuario Normal
                            </option>
                        </select>
                        <label for="idTipoUsuario">Tipo de Usuario *</label>
                        <div class="invalid-feedback">
                            Por favor, seleccione un tipo de usuario.
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mt-4">
                <button type="button" class="btn btn-outline-secondary me-2"
                    onclick="window.location.href='<%=ctx%>/views/base.jsp'">
                    Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    Guardar
                </button>
            </div>
        </form>
    </div>
</div>
