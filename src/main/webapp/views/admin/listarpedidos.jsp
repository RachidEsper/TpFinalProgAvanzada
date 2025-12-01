<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.List, model.Pedido, model.DetallePedido, model.Usuario"%>
<%
    String ctx = request.getContextPath();

    List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");

    String flashSuccess = (String) session.getAttribute("flashSuccess");
    String flashError   = (String) session.getAttribute("flashError");

    // Usuario en sesión para saber si es admin
    Usuario userSesion = (Usuario) session.getAttribute("user");
    boolean isAdmin = false;
    if (userSesion != null && userSesion.getTipo() != null) {
        isAdmin = (userSesion.getTipo().getIdTipoUsuario() == 1);
    }
%>

<%-- Mensajes flash --%>
<% if (flashSuccess != null) { %>
<div class="alert alert-success alert-dismissible fade show" role="alert">
    <%= flashSuccess %>
    <button type="button" class="btn-close" data-bs-dismiss="alert"
            aria-label="Close"></button>
</div>
<%
    session.removeAttribute("flashSuccess");
}
%>

<% if (flashError != null) { %>
<div class="alert alert-danger alert-dismissible fade show" role="alert">
    <%= flashError %>
    <button type="button" class="btn-close" data-bs-dismiss="alert"
            aria-label="Close"></button>
</div>
<%
    session.removeAttribute("flashError");
}
%>

<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
    <div>
        <h1 class="h3 mb-0">Pedidos</h1>
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item">
                <a href="<%=ctx%>/views/base.jsp">Inicio</a>
            </li>
            <li class="breadcrumb-item active">Listado de pedidos</li>
        </ol>
    </div>
</div>

<%
    // Si no hay pedidos:
    if (pedidos == null || pedidos.isEmpty()) {
        if (isAdmin) {
%>
<p class="text-muted">No hay pedidos para mostrar.</p>
<%
        } else {
            // Usuario normal sin pedidos: no mostramos nada extra
        }
    } else {

        // Hay pedidos para mostrar
        for (Pedido p : pedidos) {
            int idPed = p.getIdPedido();
            boolean finalizado = (p.getEstado() != null && p.getEstado());

            // ===== Nombre del usuario del pedido =====
            String nombreUsuarioPedido = "—";
            if (p.getUsuario() != null) {
                Usuario u = p.getUsuario();
                if (u.getNombre() != null && !u.getNombre().isBlank()) {
                    nombreUsuarioPedido = u.getNombre();
                } else {
                    nombreUsuarioPedido = "Usuario: " + u.getNombre();
                }
            }
%>

<div class="card mb-4 shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <div>
            <strong>Pedido #<%= idPed %></strong>
            &nbsp;·&nbsp;
            Fecha:
            <%= (p.getFecha() != null ? p.getFecha().toString() : "—") %>
            &nbsp;·&nbsp;
            Usuario:
            <%= nombreUsuarioPedido %>
        </div>

        <%
            if (isAdmin) {
        %>
        <%-- SOLO ADMIN: combo + botón para cambiar estado --%>
        <div>
            <form method="post" action="<%=ctx%>/PedidoEditarServlet"
                  class="d-flex align-items-center gap-2">

                <input type="hidden" name="idPedido" value="<%=idPed%>" />

                <select name="estado" class="form-select form-select-sm">
                    <option value="0" <%= !finalizado ? "selected" : "" %>>
                        Procesando
                    </option>
                    <option value="1" <%= finalizado ? "selected" : "" %>>
                        Finalizado
                    </option>
                </select>

                <button type="submit" class="btn btn-sm btn-outline-primary">
                    Actualizar
                </button>
            </form>
        </div>
        <%
            } else {
        %>
        <%-- Usuario normal: solo ve el estado como badge --%>
        <div>
            <% if (finalizado) { %>
                <span class="badge bg-success">Finalizado</span>
            <% } else { %>
                <span class="badge bg-warning text-dark">Procesando</span>
            <% } %>
        </div>
        <%
            }
        %>
    </div>

    <div class="card-body">
        <p class="mb-1">
            <strong>Dirección entrega:</strong>
            <%= (p.getDireccionEntrega() != null ? p.getDireccionEntrega() : "—") %>
        </p>
        <p class="mb-2">
            <strong>Estado actual:</strong>
            <% if (finalizado) { %>
                <span class="badge bg-success">Finalizado</span>
            <% } else { %>
                <span class="badge bg-warning text-dark">Procesando</span>
            <% } %>
        </p>
        <p class="mb-3">
            <strong>Total:</strong>
            $<%= (p.getTotal() != null ? String.format("%.2f", p.getTotal()) : "0.00") %>
        </p>

        <%-- Detalle del pedido --%>
        <h6 class="mt-3">Detalle</h6>

        <table class="table table-sm table-striped align-middle mb-0">
            <thead>
                <tr>
                    <th>Producto</th>
                    <th style="width: 80px;" class="text-center">Cant.</th>
                    <th style="width: 120px;" class="text-end">P. Unitario</th>
                    <th style="width: 120px;" class="text-end">Subtotal</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<DetallePedido> detalles = p.getDetalles();

                if (detalles == null || detalles.isEmpty()) {
            %>
                <tr>
                    <td colspan="4" class="text-muted">
                        Sin renglones de detalle.
                    </td>
                </tr>
            <%
                } else {
                    for (DetallePedido d : detalles) {
                        String idProd = (d.getProducto() != null ? d.getProducto().getIdProducto() : "—");

                        String nombreProd = null;
                        if (d.getProducto() != null &&
                            d.getProducto().getNombre() != null &&
                            !d.getProducto().getNombre().isBlank()) {
                            nombreProd = d.getProducto().getNombre();
                        }

                        int cantidad = d.getCantidad();
                        Double pu = d.getPrecioUnitario();
                        double subtotal = (pu != null ? pu : 0.0) * cantidad;
            %>
                <tr>
                    <td>
                        <%= (nombreProd != null ? nombreProd : idProd) %>
                        <% if (nombreProd != null) { %>
                            <br>
                            <small class="text-muted"><%= idProd %></small>
                        <% } %>
                    </td>
                    <td class="text-center"><%= cantidad %></td>
                    <td class="text-end">
                        $<%= (pu != null ? String.format("%.2f", pu) : "0.00") %>
                    </td>
                    <td class="text-end">
                        $<%= String.format("%.2f", subtotal) %>
                    </td>
                </tr>
            <%
                    } // fin for detalle
                } // fin else detalles
            %>
            </tbody>
        </table>
    </div>
</div>

<%
        } // fin for pedidos
    } // fin else (hay pedidos)
%>
