<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String ctx = request.getContextPath();
System.out.println("Context Path: " + ctx);

// Usuario en sesión y nombre a mostrar
model.Usuario user = (model.Usuario) session.getAttribute("user");
String nombre = null;
if (user != null) {
	try {
		nombre = (user.getNombre() != null && !user.getNombre().isBlank()) ? user.getNombre() : user.getEmail(); // fallback al email
	} catch (Exception ignore) {
	}
}

// 👉 calcular si es admin (id_tipo_usuario = 1)
boolean isAdmin = false;
try {
	if (user != null && user.getTipo() != null) {
		isAdmin = (user.getTipo().getIdTipoUsuario() == 1);
	}
} catch (Exception ignore) {
}

// página de contenido que va “adentro del layout”
String contentPage = (String) request.getAttribute("contentPage");
%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>INICIO</title>

<!-- Simple Datatables (CSS) -->
<link
	href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css"
	rel="stylesheet" />

<!-- Bootstrap (CSS) -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">

<!-- Estilos del template -->
<link href="<%=ctx%>/assets/css/styles.css" rel="stylesheet" />

<!-- Font Awesome (icons) -->
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
	crossorigin="anonymous"></script>
</head>
<body class="sb-nav-fixed">

	<!-- NAVBAR SUPERIOR -->
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
		<!-- Marca -->
		<a class="navbar-brand ps-3" href="<%=ctx%>/views/base.jsp">VENTASMN</a>

		<!-- Toggle sidebar -->
		<button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0"
			id="sidebarToggle">
			<i class="fas fa-bars"></i>
		</button>

		<!-- Buscador -->
		<form
			class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
			<div class="input-group">
				<input class="form-control" type="text"
					placeholder="Buscar Productos.." aria-label="Buscar" />
				<button class="btn btn-primary" type="button">
					<i class="fas fa-search"></i>
				</button>
			</div>
		</form>

		<!-- Usuario -->
		<ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" id="navbarDropdown" href="#"
				role="button" data-bs-toggle="dropdown" aria-expanded="false"><i
					class="fas fa-user fa-fw"></i></a>
				<ul class="dropdown-menu dropdown-menu-end"
					aria-labelledby="navbarDropdown">
					<li><a class="dropdown-item" href="#">Perfil</a></li>
					<li><a class="dropdown-item" href="#">Actividad</a></li>
					<li><hr class="dropdown-divider" /></li>
					<li>
						<form id="logoutForm" method="post"
							action="<%=ctx%>/LogoutServlet" style="display: none"></form> <a
						class="dropdown-item" href="#"
						onclick="document.getElementById('logoutForm').submit(); return false;">
							Salir </a>
					</li>
				</ul></li>
		</ul>
	</nav>

	<div id="layoutSidenav">
		<!-- SIDEBAR -->
		<div id="layoutSidenav_nav">
			<nav class="sb-sidenav accordion sb-sidenav-dark"
				id="sidenavAccordion">
				<div class="sb-sidenav-menu">
					<div class="nav">
						<div class="sb-sidenav-menu-heading">Core</div>
						<a class="nav-link" href="<%=ctx%>/views/base.jsp">
							<div class="sb-nav-link-icon">
								<i class="fas fa-tachometer-alt"></i>
							</div> Dashboard
						</a>

						<!-- ================== SECCIÓN ADMIN ================== -->
						<%
						if (isAdmin) {
						%>
						<div class="sb-sidenav-menu-heading">Operaciones</div>

						<!-- Usuarios (solo admin) -->
						<a class="nav-link collapsed" href="#" data-bs-toggle="collapse"
							data-bs-target="#menuUsuarios" aria-expanded="false"
							aria-controls="menuUsuarios">
							<div class="sb-nav-link-icon">
								<i class="fas fa-user"></i>
							</div> Usuarios
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-down"></i>
							</div>
						</a>
						<div class="collapse" id="menuUsuarios"
							data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav">
								<a class="nav-link" href="<%=ctx%>/UsuarioListarServlet">Ver
									Usuarios</a> <a class="nav-link"
									href="<%=ctx%>/UsuarioCrearServlet">Alta Usuarios</a>
							</nav>
						</div>

						<!-- Productos (CRUD admin) -->
						<a class="nav-link collapsed" href="#" data-bs-toggle="collapse"
							data-bs-target="#menuProductos" aria-expanded="false"
							aria-controls="menuProductos">
							<div class="sb-nav-link-icon">
								<i class="fas fa-box"></i>
							</div> Productos
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-down"></i>
							</div>
						</a>
						<div class="collapse" id="menuProductos"
							data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav">
								<a class="nav-link" href="<%=ctx%>/ProductoListarServlet">Ver
									Productos</a> <a class="nav-link"
									href="<%=ctx%>/ProductoCrearServlet">Alta Productos</a>
							</nav>
						</div>

						<!-- Pedidos (admin) -->
						<a class="nav-link collapsed" href="#" data-bs-toggle="collapse"
							data-bs-target="#menuPedidos" aria-expanded="false"
							aria-controls="menuPedidos">
							<div class="sb-nav-link-icon">
								<i class="fas fa-receipt"></i>
							</div> Pedidos
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-down"></i>
							</div>
						</a>
						<div class="collapse" id="menuPedidos"
							data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav">
								<a class="nav-link" href="<%=ctx%>/PedidoListarServlet">Ver
									Pedidos</a>
							</nav>
						</div>
						<%
						}
						%>

						<!-- ================== SECCIÓN TIENDA (cualquier logueado) ================== -->
						<div class="sb-sidenav-menu-heading">Tienda</div>

						<a class="nav-link" href="<%=ctx%>/ListadoProductosServlet">
							<div class="sb-nav-link-icon">
								<i class="fas fa-box-open"></i>
							</div> Productos
						</a> <a class="nav-link" href="<%=ctx%>/CarritoServlet">
							<div class="sb-nav-link-icon">
								<i class="fas fa-shopping-cart"></i>
							</div> Mi Carrito
						</a> <a class="nav-link" href="<%=ctx%>/PedidoListarServlet">
							<div class="sb-nav-link-icon">
								<i class="fas fa-receipt"></i>
							</div> Mis Pedidos
						</a>

					</div>
				</div>
				<div class="sb-sidenav-footer">
					<div class="small">Logueado como:</div>
					<%=(nombre != null ? nombre : "Invitado")%>
					<%
					if (isAdmin) {
					%>
					(Admin)<%
					}
					%>
				</div>
			</nav>
		</div>

		<!-- CONTENIDO -->
		<div id="layoutSidenav_content">
			<main>
				<div class="container-fluid px-4">
					<%
					System.out.println("DBG contentPage = " + contentPage);
					System.out.println("DBG usuario attr = " + request.getAttribute("usuario"));

					// Si no se especificó contentPage, mostramos el dashboard por defecto.
					if (contentPage == null) {
					%>
					<div class="py-5">
						<h1 class="display-5 fw-semibold mb-3">
							¡Bienvenido,
							<%=(nombre != null ? nombre : "")%>!
						</h1>
						<p class="fs-5 text-muted">
							Esta es tu tienda <strong>VentasMN</strong>...
						</p>
					</div>
					<%
					} else {
					%>
					<jsp:include page="<%=contentPage%>" />
					<%
					}
					%>
				</div>
			</main>

			<footer class="py-4 bg-light mt-auto"> ... </footer>
		</div>
	</div>

	<!-- JS: Bootstrap Bundle -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>

	<!-- Script del template (sidebar, etc.) -->
	<script src="<%=ctx%>/assets/js/scripts.js"></script>

	<!-- Charts y demos -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
		crossorigin="anonymous"></script>
	<script src="<%=ctx%>/assets/demo/chart-area-demo.js"></script>
	<script src="<%=ctx%>/assets/demo/chart-bar-demo.js"></script>

	<!-- Simple Datatables (JS) + demo -->
	<script
		src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
		crossorigin="anonymous"></script>
	<script src="<%=ctx%>/assets/js/datatables-simple-demo.js"></script>
</body>
</html>
