<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <title>Login</title>
  <link href="https://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/login.css">
  <style>body{margin-top:20px}.modal-footer{border-top:0}</style>
</head>
<body>

<div id="loginModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog"><div class="modal-content">
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h1 class="text-center">Login</h1>
    </div>

    <div class="modal-body">
   <%
	  String globalError = (String) request.getAttribute("globalError");
	  String techError   = (String) request.getAttribute("techError");
	  java.util.Map<String,String> errors =
	      (java.util.Map<String,String>) request.getAttribute("errors");
	  String emailVal = (String) request.getAttribute("email");
	
	  String emailErr = (errors != null) ? errors.get("email") : null;
	  String passErr  = (errors != null) ? errors.get("password") : null;
	%>	

	<% if (globalError != null) { %>
	  <div class="alert alert-danger"><%= globalError %></div>
	<% } %>
		<% if (techError != null) { %>
	  <div class="alert alert-warning"><%= techError %></div>
	<% } %>
      <!-- IMPORTANTE: pegamos a la URL /login -->
      <form class="form col-md-12 center-block" method="post" action="<%= request.getContextPath() %>/login">
        <div class="form-group">
          <input type="email" name="email" class="form-control input-lg" placeholder="Email" required>
        </div>
        <div class="form-group">
          <input type="password" name="password" class="form-control input-lg" placeholder="Contraseña" required>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-lg btn-block">Entrar</button>
          <span class="pull-right"><a href="#">Registrar</a></span>
          <span><a href="#">¿Necesitás ayuda?</a></span>
        </div>
      </form>
    </div>

    <div class="modal-footer">
      <div class="col-md-12"><button class="btn" data-dismiss="modal" aria-hidden="true">Cancelar</button></div>
    </div>
  </div></div>
</div>

<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="https://netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
</body>
</html>
