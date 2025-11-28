<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List, model.Pedido, model.DetallePedido"%>
<%
    String ctx = request.getContextPath();

    List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");

    String flashSuccess = (String) session.getAttribute("flashSuccess");
    String flashError   = (String) session.getAttribute("flashError");
%>

<%-- Mensajes flash --%>
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

<% if (flashError != null) { %>
<div class="alert alert-danger alert-dismissible fade show" role="alert">
    <%=flashError%>
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

<% if (pedidos == null || pedidos.isEmpty()) { %>
    <p class="text-muted">No hay pedidos para mostrar.</p>
<% } else { %>

    <%-- Recorremos todos los pedidos --%>
    <% for (Pedido p : pedidos) {
           int idPed = p.getIdPedido();
           boolean finalizado = (p.getEstado() != null && p.getEstado());
    %>

    <div class="card mb-4 shadow-sm">
        <div class="card-header d-flex justify-content-between align-items-center">
            <div>
                <strong>Pedido #<%=idPed%></strong>
                &nbsp;·&nbsp;
                Fecha:
                <%= (p.getFecha() != null ? p.getFecha().toString() : "—") %>
                &nbsp;·&nbsp;
                Usuario ID:
                <%= (p.getUsuario() != null ? p.getUsuario().getIdUsuario() : 0) %>
            </div>

            <%-- Combo + botón para cambiar estado (ADMIN) --%>
            <div>
                <form method="post" action="<%=ctx%>/PedidoEditarServlet"
                      class="d-flex align-items-center gap-2">

                    <input type="hidden" name="idPedido" value="<%=idPed%>"/>

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
                        <th style="width: 80px;">Cant.</th>
                        <th style="width: 120px;">P. Unitario</th>
                        <th style="width: 120px;">Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<DetallePedido> detalles = p.getDetalles();   // 👉 ahora viene desde el Pedido

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
                            String idProd = (d.getProducto() != null
                                             ? d.getProducto().getIdProducto()
                                             : "—");
                            int cantidad = d.getCantidad();
                            Double pu = d.getPrecioUnitario();
                            double subtotal = (pu != null ? pu : 0.0) * cantidad;
                %>
                    <tr>
                        <td><%=idProd%></td>
                        <td><%=cantidad%></td>
                        <td>$<%= (pu != null ? String.format("%.2f", pu) : "0.00") %></td>
                        <td>$<%= String.format("%.2f", subtotal) %></td>
                    </tr>
                <%
                        } // fin for detalle
                    } // fin else detalles
                %>
                </tbody>
            </table>
        </div>
    </div>

    <% } // fin for pedidos %>

<% } // fin else pedidos vacíos %>
