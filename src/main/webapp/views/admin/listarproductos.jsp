<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.List, model.Producto, model.Usuario, model.Categoria"%>
<%
String ctx = request.getContextPath();

// Lista de productos
List<Producto> productos = (List<Producto>) request.getAttribute("productos");
// Lista de categorías para filtros
List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");

// Usuario en sesión para saber si es admin
model.Usuario user = (model.Usuario) session.getAttribute("user");
boolean isAdmin = false;
try {
	if (user != null && user.getTipo() != null) {
		isAdmin = (user.getTipo().getIdTipoUsuario() == 1);
	}
} catch (Exception ignore) {
}

// Filtros actuales (para mantener seleccionados en el form)
String order = request.getParameter("order"); // "precio_asc", "precio_desc"
String stockParam = request.getParameter("stock"); // "1" => solo con stock
String catParam = request.getParameter("categoria"); // id_categoria

String flashSuccess = (String) session.getAttribute("flashSuccess"); // Mensaje exito
String flashError = (String) session.getAttribute("flashError"); // Mensaje error
%>

<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
	<div>
		<h1 class="h3 mb-0">Productos</h1>
		<ol class="breadcrumb mb-0">
			<li class="breadcrumb-item"><a href="<%=ctx%>/views/base.jsp">Inicio</a>
			</li>
			<li class="breadcrumb-item active">Listado de Productos</li>
		</ol>
	</div>
</div>

<%
if (flashSuccess != null) {
%>
<div class="alert alert-success alert-dismissible fade show"
	role="alert">
	<%=flashSuccess%>
	<button type="button" class="btn-close" data-bs-dismiss="alert"
		aria-label="Close"></button>
</div>
<%
session.removeAttribute("flashSuccess");
}
%>

<%
if (flashError != null) {
%>
<div class="alert alert-danger alert-dismissible fade show" role="alert">
	<%=flashError%>
	<button type="button" class="btn-close" data-bs-dismiss="alert"
		aria-label="Close"></button>
</div>
<%
session.removeAttribute("flashError");
}
%>

<div class="row">
	<!-- Columna izquierda: FILTROS (visible para admin y usuario normal) -->
	<div class="col-lg-3 mb-4">
		<div class="card mb-3">
			<div class="card-header">
				<strong>Filtros</strong>
			</div>
			<div class="card-body">
				<form method="get" action="<%=ctx%>/ProductoListarServlet">
					<!-- Categoría -->
					<div class="mb-3">
						<label for="categoria" class="form-label">Categoría</label> <select
							class="form-select" id="categoria" name="categoria">
							<option value="0">Todas</option>
							<%
							if (categorias != null) {
								for (Categoria c : categorias) {
									if (c == null)
								continue;
									String sel = "";
									if (catParam != null && !catParam.isBlank()) {
								try {
									int idSel = Integer.parseInt(catParam);
									if (idSel == c.getIdCategoria()) {
										sel = "selected";
									}
								} catch (NumberFormatException ignore) {
								}
									}
							%>
							<option value="<%=c.getIdCategoria()%>" <%=sel%>>
								<%=c.getNombre()%>
							</option>
							<%
							}
							}
							%>
						</select>
					</div>

					<!-- Orden por precio -->
					<div class="mb-3">
						<label for="order" class="form-label">Ordenar por precio</label> <select
							class="form-select" id="order" name="order">
							<option value=""
								<%=(order == null || order.isBlank() ? "selected" : "")%>>
								Sin orden</option>
							<option value="precio_asc"
								<%="precio_asc".equals(order) ? "selected" : ""%>>
								Menor a mayor</option>
							<option value="precio_desc"
								<%="precio_desc".equals(order) ? "selected" : ""%>>
								Mayor a menor</option>
						</select>
					</div>

					<!-- Solo con stock -->
					<div class="form-check mb-3">
						<input class="form-check-input" type="checkbox" id="stock"
							name="stock" value="1"
							<%="1".equals(stockParam) ? "checked" : ""%>> <label
							class="form-check-label" for="stock"> Solo productos con
							stock </label>
					</div>

					<div class="d-grid gap-2">
						<button type="submit" class="btn btn-primary btn-sm">
							Aplicar filtros</button>
						<a href="<%=ctx%>/ProductoListarServlet"
							class="btn btn-outline-secondary btn-sm"> Limpiar filtros </a>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Columna derecha: cards de productos -->
	<div class="col-lg-9">
		<%
		if (productos == null || productos.isEmpty()) {
		%>
		<p class="text-muted">No hay productos para mostrar con los
			filtros actuales.</p>
		<%
		} else {
		%>

		<%
		for (Producto p : productos) {
			String idProd = p.getIdProducto();
			String nombre = p.getNombre();
			String desc = p.getDescripcion();
			Double precio = p.getPrecio();
			float descPct = p.getDescuento();
			String imagen = p.getUrlImagen(); // base64 o URL
			int stock = p.getStock();
			int idCatProd = p.getIdCategoria();

			// Buscar nombre de categoría según idCategoria (si lo necesitás mostrar)
			String nombreCategoria = "";
			if (categorias != null) {
				for (Categoria c : categorias) {
			if (c != null && c.getIdCategoria() == idCatProd) {
				nombreCategoria = c.getNombre();
				break;
			}
				}
			}

			// Precio final si hay descuento
			double precioBase = (precio != null ? precio : 0.0);
			double precioFinal = precioBase;
			if (descPct > 0) {
				precioFinal = precioBase * (1 - (descPct / 100.0));
			}
		%>

		<div class="card mb-3 shadow-sm">
			<div class="row g-0 align-items-center">
				<!-- Imagen -->
				<div class="col-md-3 text-center p-2">
					<%
					if (imagen != null && !imagen.isBlank()) {
					%>
					<img src="data:image/jpeg;base64,<%=imagen%>"
						class="img-fluid rounded" alt="<%=nombre%>">
					<%
					} else {
					%>
					<div
						class="bg-light d-flex align-items-center justify-content-center"
						style="height: 120px;">
						<span class="text-muted">Sin imagen</span>
					</div>
					<%
					}
					%>
				</div>

				<!-- Descripción -->
				<div class="col-md-6">
					<div class="card-body">
						<h5 class="card-title mb-1"><%=nombre%></h5>

						<!-- Nombre de la categoría -->
						<p class="card-text small mb-1">
							<strong>Categoría:</strong>
							<%=(nombreCategoria != null && !nombreCategoria.isBlank() ? nombreCategoria : "Sin categoría")%>
						</p>

						<p class="card-text small text-muted mb-2">
							<%=(desc != null ? desc : "")%>
						</p>

						<%
						if (descPct > 0) {
						%>
						<span class="badge bg-success me-2"> -<%=descPct%>% OFF
						</span>
						<%
						}
						%>

						<span class="badge <%=stock > 0 ? "bg-secondary" : "bg-danger"%>">
							Stock: <%=stock%>
						</span>
					</div>
				</div>

				<!-- Precio + botón + (botones admin) -->
				<div class="col-md-3 text-end pe-4">
					<div class="card-body">
						<%
						if (descPct > 0) {
						%>
						<div class="mb-1">
							<small class="text-muted text-decoration-line-through"> $<%=String.format("%.2f", precioBase)%>
							</small>
						</div>
						<h4 class="mb-1">
							$<%=String.format("%.2f", precioFinal)%>
						</h4>
						<%
						} else {
						%>
						<h4 class="mb-1">
							$<%=String.format("%.2f", precioBase)%>
						</h4>
						<%
						}
						%>

						<small class="text-muted d-block mb-2"> <%=stock > 0 ? "Disponible" : "Sin stock"%>
						</small>

						<!-- Botón para carrito / pedido (solo usuario normal) -->
						<%
						if (!isAdmin) {
						%>
						<form method="post" action="<%=ctx%>/PedidoEnCursoServlet"
							class="mb-2">
							<input type="hidden" name="accion" value="agregar"> <input
								type="hidden" name="idProducto" value="<%=idProd%>">
							<button type="submit" class="btn btn-sm btn-primary"
								<%=stock <= 0 ? "disabled" : ""%>>
								<i class="fas fa-shopping-cart me-1"></i> Agregar al pedido
							</button>
						</form>
						<%
						}
						%>

						<!-- 👉 Botones EDITAR/ELIMINAR solo si es admin -->
						<%
						if (isAdmin) {
						%>
						<div class="btn-group btn-group-sm" role="group">
							<!-- EDITAR -->
							<a href="<%=ctx%>/ProductoEditarServlet?id=<%=idProd%>"
								class="btn btn-outline-secondary" title="Editar"> <i
								class="fas fa-pen"></i>
							</a>

							<!-- ELIMINAR -->
							<form method="post" action="<%=ctx%>/ProductoEliminarServlet"
								onsubmit="return confirm('¿Eliminar este producto?');"
								style="display: inline-block">
								<input type="hidden" name="idProducto" value="<%=idProd%>" />
								<button type="submit" class="btn btn-outline-danger"
									title="Eliminar">
									<i class="fas fa-trash"></i>
								</button>
							</form>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>

		<%
		} // fin for productos
		} // fin else hay productos
		%>
	</div>
</div>
