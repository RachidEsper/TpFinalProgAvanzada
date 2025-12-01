<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
    import="java.util.List, model.Categoria" %>
<%
String ctx = request.getContextPath();

// Saber si estamos en modo edición
String modo = (String) request.getAttribute("modo");
boolean esEdicion = "editar".equals(modo);

// Valores repoblados si hubo errores o vienen del servlet de edición
String idProductoVal   = (String) request.getAttribute("idProducto");
String nombreVal       = (String) request.getAttribute("nombre");
String descripcionVal  = (String) request.getAttribute("descripcion");
String precioVal       = (String) request.getAttribute("precio");
String descuentoVal    = (String) request.getAttribute("descuento");
String urlImagenVal    = (String) request.getAttribute("urlImagen");
String stockVal        = (String) request.getAttribute("stock");
String idCategoriaVal  = (String) request.getAttribute("idCategoria");

String globalError     = (String) request.getAttribute("globalError");
String flashSuccess    = (String) session.getAttribute("flashSuccess");

// Lista de categorías (cargada por el servlet)
List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");

// A qué servlet va a enviar el form
String formAction = esEdicion
    ? (ctx + "/ProductoEditarServlet")
    : (ctx + "/ProductoCrearServlet");
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
        <h1 class="h3 mb-0">
            <%= esEdicion ? "Editar Producto" : "Alta de Producto" %>
        </h1>
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item">
                <a href="<%=ctx%>/views/base.jsp">Inicio</a>
            </li>
            <li class="breadcrumb-item">
                <a href="<%=ctx%>/ProductoListarServlet">Productos</a>
            </li>
            <li class="breadcrumb-item active">
                <%= esEdicion ? "Editar" : "Nuevo" %>
            </li>
        </ol>
    </div>
</div>

<% if (globalError != null) { %>
<div class="alert alert-danger"><%=globalError%></div>
<% } %>

<div class="card shadow-sm mb-4">
    <div class="card-header">
        <i class="fas fa-box me-1"></i>
        <%= esEdicion ? "Modificar datos del producto" : "Completar datos del producto" %>
    </div>
    <div class="card-body">
        <form method="post" action="<%=formAction%>" novalidate>
            <div class="row g-3">
                <!-- ID Producto -->
                <div class="col-md-4">
                    <div class="form-floating">
                        <input type="text"
                               class="form-control"
                               id="idProducto"
                               name="idProducto"
                               placeholder="ID Producto"
                               value="<%=(idProductoVal != null ? idProductoVal : "")%>"
                               <%= esEdicion ? "readonly" : "" %> >
                        <label for="idProducto">ID Producto</label>
                    </div>
                </div>

                <!-- Nombre -->
                <div class="col-md-8">
                    <div class="form-floating">
                        <input type="text" class="form-control" id="nombre"
                            name="nombre" placeholder="Nombre del producto"
                            value="<%=(nombreVal != null ? nombreVal : "")%>" required>
                        <label for="nombre">Nombre *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese el nombre del producto.
                        </div>
                    </div>
                </div>

                <!-- Descripción -->
                <div class="col-12">
                    <div class="form-floating">
                        <textarea class="form-control" placeholder="Descripción"
                            id="descripcion" name="descripcion" style="height: 100px" required><%=
                            (descripcionVal != null ? descripcionVal : "") %></textarea>
                        <label for="descripcion">Descripción *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese una descripción.
                        </div>
                    </div>
                </div>

                <!-- Precio (texto, sin flechitas) -->
                <div class="col-md-4">
                    <div class="form-floating">
                        <input type="text"
                               class="form-control"
                               id="precio"
                               name="precio"
                               placeholder="Precio"
                               inputmode="decimal"
                               value="<%=(precioVal != null ? precioVal : "")%>"
                               required>
                        <label for="precio">Precio *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese un precio válido (ej: 199.99).
                        </div>
                    </div>
                </div>

                <!-- Descuento -->
                <div class="col-md-4">
                    <div class="form-floating">
                        <input type="number" step="0.01" min="0"
                            class="form-control" id="descuento" name="descuento"
                            placeholder="Descuento"
                            value="<%=(descuentoVal != null ? descuentoVal : "")%>">
                        <label for="descuento">Descuento (ej: 0.15 ó 15) </label>
                        <div class="invalid-feedback">
                            Ingrese un descuento válido o deje en blanco.
                        </div>
                    </div>
                </div>

                <!-- Stock -->
                <div class="col-md-4">
                    <div class="form-floating">
                        <input type="number" min="0"
                            class="form-control" id="stock" name="stock"
                            placeholder="Stock"
                            value="<%=(stockVal != null ? stockVal : "")%>" required>
                        <label for="stock">Stock *</label>
                        <div class="invalid-feedback">
                            Por favor, ingrese el stock disponible.
                        </div>
                    </div>
                </div>

                <!-- URL Imagen (opcional) -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="text" class="form-control" id="urlImagen"
                            name="urlImagen" placeholder="URL de imagen"
                            value="<%=(urlImagenVal != null ? urlImagenVal : "")%>">
                        <label for="urlImagen">URL de imagen (opcional)</label>
                    </div>
                </div>

                <!-- Categoría -->
                <div class="col-md-6">
                    <div class="form-floating">
                        <select class="form-select" id="idCategoria"
                            name="idCategoria" required>
                            <option value="" 
                                <%= (idCategoriaVal == null || idCategoriaVal.isBlank()) ? "selected" : "" %>
                                disabled>Seleccioná una categoría</option>
                            <%
                            if (categorias != null) {
                                for (Categoria c : categorias) {
                                    String selected = (idCategoriaVal != null 
                                        && idCategoriaVal.equals(String.valueOf(c.getIdCategoria())))
                                        ? "selected" : "";
                            %>
                                <option value="<%=c.getIdCategoria()%>" <%=selected%>>
                                    <%=c.getIdCategoria()%> - <%=c.getNombre()%>
                                </option>
                            <%
                                }
                            }
                            %>
                        </select>
                        <label for="idCategoria">Categoría *</label>
                        <div class="invalid-feedback">
                            Por favor, seleccione una categoría。
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mt-4">
                <button type="button" class="btn btn-outline-secondary me-2"
                    onclick="window.location.href='<%=ctx%>/ProductoListarServlet'">
                    Cancelar
                </button>
                <button type="submit" class="btn btn-primary">
                    <%= esEdicion ? "Actualizar" : "Guardar" %>
                </button>
            </div>
        </form>
    </div>
</div>
