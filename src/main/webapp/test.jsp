<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Bootstrap funcionando</title>

  <!-- Bootstrap desde CDN -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous">

  <!-- FontAwesome (íconos) -->
  <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>

  <!-- Tu CSS del template -->
  <link rel="stylesheet" href="<%=ctx%>/assets/css/styles.css">
</head>
<body class="p-5 bg-light">
  <div class="container">
    <h1 class="text-primary">
      <i class="fas fa-check-circle text-success"></i>
      Bootstrap funcionando correctamente ✅
    </h1>

    <p class="mt-4">Este texto usa Bootstrap desde CDN y tu propio CSS desde <code>assets/css/styles.css</code>.</p>

    <button class="btn btn-success">Botón de prueba</button>
  </div>

  <!-- Bootstrap JS Bundle -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
          integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
          crossorigin="anonymous"></script>

  <!-- Tu script -->
  <script src="<%=ctx%>/assets/js/scripts.js"></script>
</body>
</html>
