<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List,model.Producto"%>
<%
    String ctx = request.getContextPath();
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
%>

<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
    <div>
        <h1 class="h3 mb-0">Productos</h1>
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item">
                <a href="<%=ctx%>/views/base.jsp">Inicio</a>
            </li>
            <li class="breadcrumb-item active">Listado de Productos</li>
        </ol>
    </div>
</div>

<div class="row">
    <!-- Columna izquierda: futuros filtros -->
    <div class="col-lg-3 mb-4">
        <div class="card">
            <div class="card-header">
                <strong>Categorías</strong>
            </div>
            <div class="card-body">
                <!-- Por ahora estático; luego lo hacemos dinámico -->
                <ul class="list-unstyled mb-0">
                    <li><a href="#">Todos</a></li>
                    <li><a href="#">Categoría 1</a></li>
                    <li><a href="#">Categoría 2</a></li>
                </ul>
            </div>
        </div>

        <div class="card mt-3">
            <div class="card-header">
                <strong>Filtros</strong>
            </div>
            <div class="card-body">
                <!-- acá después van filtros de precio, marca, etc. -->
                <small class="text-muted">Próximamente filtros 😎</small>
            </div>
        </div>
    </div>

    <!-- Columna derecha: cards de productos -->
    <div class="col-lg-9">
        <% if (productos == null || productos.isEmpty()) { %>
            <p class="text-muted">No hay productos para mostrar.</p>
        <% } else { %>

            <% for (Producto p : productos) {
                   String idProd   = p.getIdProducto();
                   String nombre   = p.getNombre();
                   String desc     = p.getDescripcion();
                   Double precio   = p.getPrecio();
                   float descPct   = p.getDescuento();
                   String imagen   = p.getUrlImagen(); // base64 o URL
                   int stock       = p.getStock();     // 👉 NUEVO
            %>

            <div class="card mb-3 shadow-sm">
                <div class="row g-0 align-items-center">
                    <!-- Imagen -->
                    <div class="col-md-3 text-center p-2">
                        <% if (imagen != null && !imagen.isBlank()) { %>
                            <img src="data:image/jpeg;base64,<%=imagen%>"
                                 class="img-fluid rounded" alt="<%=nombre%>">
                        <% } else { %>
                            <div class="bg-light d-flex align-items-center justify-content-center"
                                 style="height:120px;">
                                <span class="text-muted">Sin imagen</span>
                            </div>
                        <% } %>
                    </div>

                    <!-- Descripción -->
                    <div class="col-md-6">
                        <div class="card-body">
                            <h5 class="card-title mb-1"><%=nombre%></h5>
                            <p class="card-text small text-muted mb-2">
                                <%= (desc != null ? desc : "") %>
                            </p>
                            <% if (descPct > 0) { %>
                                <span class="badge bg-success me-2">
                                    -<%=descPct%> % OFF
                                </span>
                            <% } %>
                            <!-- 👉 Stock visible en la descripción -->
                            <span class="badge <%= stock > 0 ? "bg-secondary" : "bg-danger" %>">
                                Stock: <%=stock%>
                            </span>
                        </div>
                    </div>

                    <!-- Precio + botón -->
                    <div class="col-md-3 text-end pe-4">
                        <div class="card-body">
                            <h4 class="mb-1">
                                $<%= (precio != null ? String.format("%.2f", precio) : "0.00") %>
                            </h4>
                            <small class="text-muted d-block mb-2">
                                <%= stock > 0 ? "Disponible" : "Sin stock" %>
                            </small>

                            <form method="post" action="<%=ctx%>/PedidoCrearServlet">
                                <input type="hidden" name="idProducto" value="<%=idProd%>">
                                <button type="submit" class="btn btn-sm btn-primary"
                                        <%= stock <= 0 ? "disabled" : "" %>>
                                    <i class="fas fa-shopping-cart me-1"></i> Agregar al pedido
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <% } // fin for %>
        <% } // fin else %>
    </div>
</div>
