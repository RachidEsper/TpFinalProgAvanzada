<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.List, model.Pedido, model.DetallePedido, model.Usuario"%>
<%
String ctx = request.getContextPath();

Pedido pedido = (Pedido) request.getAttribute("pedido");

// Primero intento leer la lista desde el atributo "detalles"
List<DetallePedido> detalles = (List<DetallePedido>) request.getAttribute("detalles");

// Si no vino como atributo pero el pedido no es null, intento usar pedido.getDetalles()
if (detalles == null && pedido != null) {
	detalles = pedido.getDetalles();
}

Double total = (Double) request.getAttribute("total");
if (total == null)
	total = 0.0;

String direccionVal = (String) request.getAttribute("direccionEntrega");
if (direccionVal == null && pedido != null && pedido.getDireccionEntrega() != null) {
	direccionVal = pedido.getDireccionEntrega();
}

String globalError = (String) request.getAttribute("globalError");
String flashSuccess = (String) session.getAttribute("flashSuccess");
String flashError = (String) session.getAttribute("flashError");

// Usuario logueado (para mostrar en cabecera)
Usuario user = (Usuario) session.getAttribute("user");
%>


<div class="d-flex align-items-center justify-content-between mt-4 mb-3">
	<div>
		<h1 class="h3 mb-0">Crear Pedido</h1>
		<ol class="breadcrumb mb-0">
			<li class="breadcrumb-item"><a href="<%=ctx%>/views/base.jsp">Inicio</a>
			</li>
			<li class="breadcrumb-item active">Pedido en curso</li>
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

<%
if (globalError != null) {
%>
<div class="alert alert-danger alert-dismissible fade show" role="alert">
	<%=globalError%>
	<button type="button" class="btn-close" data-bs-dismiss="alert"
		aria-label="Close"></button>
</div>
<%
}
%>

<%
if (pedido == null || detalles == null || detalles.isEmpty()) {
%>
<p class="text-muted">Todavía no tenés productos en tu pedido.</p>
<%
} else {
%>

<div class="card mb-4">
	<div
		class="card-header d-flex justify-content-between align-items-center">
		<div>
			<strong>Pedido #<%=pedido.getIdPedido()%></strong> &middot; <span>Fecha:
				<%=pedido.getFecha()%></span>
			<%
			if (user != null) {
			%>
			&middot; <span>Usuario ID: <%=user.getIdUsuario()%></span>
			<%
			}
			%>
		</div>
		<span class="badge bg-secondary"> Estado: <%=(pedido.getEstado() != null ? pedido.getEstado() : "INICIADO")%>
		</span>
	</div>

	<!-- FORM: dirección + finalizar -->
	<form method="post" action="<%=ctx%>/PedidoEnCursoServlet">
		<input type="hidden" name="accion" value="finalizar"> <input
			type="hidden" name="idPedido" value="<%=pedido.getIdPedido()%>">

		<div class="card-body">

			<!-- Dirección de entrega -->
			<div class="mb-4">
				<label for="direccionEntrega" class="form-label fw-semibold">
					Dirección de entrega </label> <input type="text" class="form-control"
					id="direccionEntrega" name="direccionEntrega"
					placeholder="Ej: Av. Santa Fe 1234, CABA"
					value="<%=(direccionVal != null ? direccionVal : "")%>" required>
				<div class="form-text">Ingresá la dirección donde querés
					recibir tu pedido.</div>
			</div>

			<!-- Detalle del pedido -->
			<h6 class="fw-semibold mb-2">Detalle</h6>

			<div class="table-responsive">
				<table class="table table-sm align-middle">
					<thead class="table-light">
						<tr>
							<th style="width: 40%;">Producto</th>
							<th style="width: 15%;" class="text-center">Cant.</th>
							<th style="width: 20%;" class="text-end">P. Unitario</th>
							<th style="width: 25%;" class="text-end">Subtotal</th>
						</tr>
					</thead>
					<tbody>
						<%
						for (DetallePedido d : detalles) {
							String idProd = (d.getProducto() != null ? d.getProducto().getIdProducto() : "—");
							String nombreProd = (d.getProducto() != null ? d.getProducto().getNombre() : null);

							int cant = d.getCantidad();
							double pUnit = (d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0.0);
							double sub = pUnit * cant;
						%>
						<tr>
							<td><%=(nombreProd != null && !nombreProd.isBlank() ? nombreProd : idProd)%>
								<br> <small class="text-muted">[<%=idProd%>]
							</small></td>
							<td class="text-center"><%=cant%></td>
							<td class="text-end">$<%=String.format("%.2f", pUnit)%></td>
							<td class="text-end">$<%=String.format("%.2f", sub)%></td>
						</tr>
						<%
}
%>
					</tbody>
				</table>
			</div>

			<div class="d-flex justify-content-end mt-3">
				<h5 class="mb-0">
					Total: $<%=String.format("%.2f", total)%>
				</h5>
			</div>
		</div>

		<div class="card-footer d-flex justify-content-end">
			<a href="<%=ctx%>/ProductoListarServlet"
				class="btn btn-outline-secondary me-2"> Seguir comprando </a>
			<button type="submit" class="btn btn-primary">Finalizar
				pedido</button>
		</div>
	</form>
</div>

<%
}
%>
