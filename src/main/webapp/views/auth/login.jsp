<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String ctx = request.getContextPath();

String globalError = (String) request.getAttribute("globalError");
String techError = (String) request.getAttribute("techError");
java.util.Map<String, String> errors = (java.util.Map<String, String>) request.getAttribute("errors");

String emailVal = (String) request.getAttribute("email");

String emailErr = (errors != null) ? errors.get("email") : null;
String passErr = (errors != null) ? errors.get("password") : null;

String emailClass = (emailErr != null) ? "form-control is-invalid" : "form-control";
String passClass = (passErr != null) ? "form-control is-invalid" : "form-control";
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
<title>Login</title>

<!-- CSS Bootstrap 5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">

<!-- Estilos del template -->
<link href="<%=ctx%>/assets/css/styles.css" rel="stylesheet" />

<!-- Iconos -->
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
	crossorigin="anonymous"></script>
</head>
<body class="bg-primary">

	<div id="layoutAuthentication">
		<div id="layoutAuthentication_content">
			<main>
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-lg-5">
							<div class="card shadow-lg border-0 rounded-lg mt-5">
								<div class="card-header">
									<h3 class="text-center font-weight-light my-4">Login</h3>
								</div>

								<div class="card-body">

									<%
									if (globalError != null) {
									%>
									<div class="alert alert-danger"><%=globalError%></div>
									<%
									}
									%>
									<%
									if (techError != null) {
									%>
									<div class="alert alert-warning"><%=techError%></div>
									<%
									}
									%>

									<!-- IMPORTANTE: la acción pega a /login -->
									<form method="post" action="<%=ctx%>/LoginServlet" novalidate>
										<div class="form-floating mb-3">
											<input class="<%=emailClass%>" id="inputEmail" name="email"
												type="email" placeholder="name@example.com"
												value="<%=(emailVal != null) ? emailVal : ""%>" required>
											<label for="inputEmail">Email</label>
											<%
											if (emailErr != null) {
											%>
											<div class="invalid-feedback"><%=emailErr%></div>
											<%
											}
											%>
										</div>

										<div class="form-floating mb-3">
											<input class="<%=passClass%>" id="inputPassword"
												name="password" type="password" placeholder="Password"
												required> <label for="inputPassword">Contraseña</label>
											<%
											if (passErr != null) {
											%>
											<div class="invalid-feedback"><%=passErr%></div>
											<%
											}
											%>
										</div>

									

										<div
											class="d-flex align-items-center justify-content-between mt-4 mb-0">
											<button type="submit" class="btn btn-primary">Entrar</button>
										</div>
									</form>
								</div>

								<div class="card-footer text-center py-3">
									<div class="small"></div>
								</div>

							</div>
						</div>
					</div>
				</div>
			</main>
		</div>

		<div id="layoutAuthentication_footer">
			<footer class="py-4 bg-light mt-auto">
				<div class="container-fluid px-4">
					<div
						class="d-flex align-items-center justify-content-between small">
						<div class="text-muted">&copy; VENTASMN 2025</div>
						<div>
							<a href="#">Privacidad</a> &middot; <a href="#">Términos</a>
						</div>
					</div>
				</div>
			</footer>
		</div>
	</div>

	<!-- JS Bootstrap 5 -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>

	<!-- Script del template -->
	<script src="<%=ctx%>/assets/js/scripts.js"></script>
</body>
</html>
