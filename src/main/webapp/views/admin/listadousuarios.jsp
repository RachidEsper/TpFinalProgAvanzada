<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List,model.Usuario"%>
<%
    String ctx = request.getContextPath();
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
%>

<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
    <div>
        <h1 class="h3 mb-0">Usuarios</h1>
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="<%=ctx%>/base.jsp">Inicio</a></li>
            <li class="breadcrumb-item active">Usuarios</li>
        </ol>
    </div>
    <a href="<%=ctx%>/UsuarioCrearServlet" class="btn btn-primary">
        <i class="fas fa-user-plus me-1"></i> Alta de Usuario
    </a>
</div>

<div class="card mb-4">
    <div class="card-header">
        <i class="fas fa-table me-1"></i>Listado
    </div>
    <div class="card-body">
        <table id="datatablesSimple" class="table table-striped align-middle">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Contraseña</th>
                    <th>Teléfono</th>
                    <th>idTipoUsuario</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <%
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (Usuario u : usuarios) {
                        String nombreU = u.getNombre() != null ? u.getNombre() : "";
                        String contrasenia = u.getPassword() != null ? u.getPassword() : "";
                        String email = u.getEmail() != null ? u.getEmail() : "";
                        String telefono = u.getTelefono() != null ? u.getTelefono() : "";
                        String idTipo = "";
                        try {
                            idTipo = (u.getTipo() != null) ? String.valueOf(u.getTipo().getIdTipoUsuario()) : "";
                        } catch (Exception ignore) {}
                %>
                <tr>
                    <td><%=nombreU%></td>
                    <td><%=email%></td>
                    <td><%=contrasenia%></td>
                    <td><%=telefono%></td>
                    <td><%=idTipo%></td>
                    <td>
                        <div class="btn-group btn-group-sm" role="group">
                            <a href="<%=ctx%>/UsuarioEditarServlet id=<%=u.getIdUsuario()%>"
                               class="btn btn-outline-secondary" title="Editar">
                                <i class="fas fa-pen"></i>
                            </a>
                            <form method="post" action="<%=ctx%>/UsuarioEliminarServlet"
                                  onsubmit="return confirm('¿Eliminar este usuario?');"
                                  style="display: inline-block">
                                <input type="hidden" name="id" value="<%=u.getIdUsuario()%>" />
                                <button type="submit" class="btn btn-outline-danger" title="Eliminar">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7" class="text-center text-muted">No hay usuarios para mostrar.</td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
</div>

<script>
    (function() {
        var el = document.getElementById('datatablesSimple');
        if (el && window.simpleDatatables)
            new simpleDatatables.DataTable(el);
    })();
</script>
