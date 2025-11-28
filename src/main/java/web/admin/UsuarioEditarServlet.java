package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TipoUsuario;
import model.Usuario;
import service.implementation.UsuarioServiceImpl;
import service.interfaces.IUsuarioService;

import java.io.IOException;

import dao.implementation.UsuarioDAOImpl;
import dao.interfaces.IUsuarioDAO;

/**
 * Servlet implementation class UsuarioEditarServlet
 */

/*
 * Rol del servlet:

GET → Mostrar el formulario de edición, precargado con los datos del usuario.

POST → Recibir los campos editados, validar, actualizar en DB, y redirigir de nuevo al listado (o volver al formulario con errores).
 */




@WebServlet("/UsuarioEditarServlet")
public class UsuarioEditarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuarioEditarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 IUsuarioService sv = new UsuarioServiceImpl();
		 
		 String idParam=request.getParameter("id");
		 
		 if(idParam==null || idParam.isBlank()) {
			 // Si no encontro id redirige a listado de usuarios
			 response.sendRedirect(request.getContextPath() + "/UsuarioListarServlet");
			 return;
		 }
		 
		 int id = Integer.parseInt(idParam);
		 Usuario u=sv.findById(id);
		 
		 if (u == null) {
	            // flashError en sesión si no lo encuentra
	            HttpSession session = request.getSession();
	            session.setAttribute("flashError", "El usuario no existe.");
	            response.sendRedirect(request.getContextPath() + "/UsuarioListarServlet");
	            return;
	        }
		 	// Enviás el usuario a la vista
	        request.setAttribute("usuario", u);

	        // Indicás qué contenido va dentro del layout
	        request.setAttribute("contentPage", "/views/admin/usuarioeditar.jsp");
	        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("UTF-8");
	    String ctx = request.getContextPath();

	    IUsuarioService sv = new UsuarioServiceImpl();

	    // 1) Leer parámetros del form
	    String idParam   = request.getParameter("id");
	    String nombre    = request.getParameter("nombre");
	    String email     = request.getParameter("email");
	    String telefono  = request.getParameter("telefono");
	    String tipoParam = request.getParameter("idTipoUsuario");
	    String pass      = request.getParameter("password");

	    // 2) Validaciones básicas
	    java.util.Map<String, String> errors = new java.util.HashMap<>();

	    if (idParam == null || idParam.isBlank()) {
	        // Algo raro: no vino el id → vuelvo al listado
	        response.sendRedirect(ctx + "/UsuarioListarServlet");
	        return;
	    }
	    int id = Integer.parseInt(idParam);

	    if (nombre == null || nombre.isBlank()) {
	        errors.put("nombre", "El nombre es obligatorio.");
	    }

	    if (email == null || email.isBlank()) {
	        errors.put("email", "El email es obligatorio.");
	    }

	    if (tipoParam == null || tipoParam.isBlank()) {
	        errors.put("idTipoUsuario", "Debe seleccionar un tipo de usuario.");
	    }

	    // 3) Si hay errores → volver al form con lo que el usuario cargó
	    if (!errors.isEmpty()) {
	        Usuario uForm = new Usuario();
	        uForm.setIdUsuario(id);
	        uForm.setNombre(nombre);
	        uForm.setEmail(email);
	        uForm.setTelefono(telefono);
	        // No tiene sentido mostrar la contraseña, así que no la usamos en el JSP

	        if (tipoParam != null && !tipoParam.isBlank()) {
	            TipoUsuario t = new TipoUsuario();
	            t.setIdTipoUsuario(Integer.parseInt(tipoParam));
	            uForm.setTipo(t);
	        }

	        request.setAttribute("usuario", uForm);
	        request.setAttribute("errors", errors);
	        request.setAttribute("globalError", "Revisá los campos marcados.");

	        request.setAttribute("contentPage", "/views/admin/usuarioeditar.jsp");
	        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
	        return;
	    }

	    // 4) Sin errores: buscar el usuario original en BD
	    Usuario uDb = sv.findById(id);
	    if (uDb == null) {
	        HttpSession session = request.getSession();
	        session.setAttribute("flashError", "El usuario ya no existe.");
	        response.sendRedirect(ctx + "/UsuarioListarServlet");
	        return;
	    }

	    // 5) Decidir qué contraseña guardar:
	    //    - si el admin escribió algo, usamos esa
	    //    - si dejó vacío, mantenemos la actual
	    String passwordFinal;
	    if (pass != null && !pass.isBlank()) {
	        passwordFinal = pass;
	    } else {
	        passwordFinal = uDb.getPassword();  // la que ya estaba en BD
	    }

	    int tipoInt = Integer.parseInt(tipoParam);

	    // 6) Guardar cambios a través del service
	    boolean ok = sv.updateUsuario(
	            id,
	            nombre,
	            email,
	            passwordFinal,
	            telefono,
	            tipoInt
	    );

	    HttpSession session = request.getSession();
	    if (ok) {
	        session.setAttribute("flashSuccess", "Usuario actualizado correctamente.");
	    } else {
	        session.setAttribute("flashError", "No se pudo actualizar el usuario.");
	    }

	    // 7) Volver al listado
	    response.sendRedirect(ctx + "/UsuarioListarServlet");
	}



}
