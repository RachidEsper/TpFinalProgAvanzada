package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TipoUsuario;
import model.Usuario;

import java.io.IOException;

import dao.implementation.UsuarioDAOImpl;
import dao.interfaces.IUsuarioDAO;
import service.implementation.UsuarioServiceImpl;
import service.interfaces.IUsuarioService;
/**
 * Servlet implementation class UsuarioCrearServlet
 */
@WebServlet("/UsuarioCrearServlet")
public class UsuarioCrearServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuarioCrearServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 request.setAttribute("contentPage", "/views/admin/registrousuarios.jsp");
	    request.getRequestDispatcher("/views/base.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// SOLO RECIBIR PARÁMETROS (responsabilidad del controlador)
		String nombre = request.getParameter("nombre");
		String email = request.getParameter("email");
		String telefono = request.getParameter("telefono");
		String password = request.getParameter("password");
		String idTipoUsuarioStr = request.getParameter("idTipoUsuario");
		
		try {
			// Validación básica de parseo (solo para evitar que el servlet explote)
			int idTipoUsuario = 0;
			if (idTipoUsuarioStr != null && !idTipoUsuarioStr.trim().isEmpty()) {
				idTipoUsuario = Integer.parseInt(idTipoUsuarioStr);
			}
			
			// DELEGAR AL SERVICIO (que contiene toda la lógica de negocio y validaciones)
			IUsuarioService usuarioService = new UsuarioServiceImpl();
			Usuario usuario = usuarioService.createUsuario(nombre, email, password, telefono, idTipoUsuario);
			
			System.out.println("Usuario creado: " + usuario.getNombre());
			
			// REDIRIGIR CON MENSAJE DE ÉXITO
			request.getSession().setAttribute("flashSuccess", "Usuario registrado correctamente.");
			response.sendRedirect(request.getContextPath() + "/admin/listadousuarios.jsp");
			
		} catch (NumberFormatException e) {
			// Error de formato en el tipo de usuario
			request.setAttribute("globalError", "El tipo de usuario no es válido.");
			request.setAttribute("nombre", nombre);
			request.setAttribute("email", email);
			request.setAttribute("telefono", telefono);
			request.setAttribute("idTipoUsuario", idTipoUsuarioStr);
			request.getRequestDispatcher("/views/admin/registrousuarios.jsp").forward(request, response);
			
		} catch (exception.BusinessException e) {
			// Error de validación de negocio (lanzado por el servicio)
			request.setAttribute("globalError", e.getMessage());
			request.setAttribute("nombre", nombre);
			request.setAttribute("email", email);
			request.setAttribute("telefono", telefono);
			request.setAttribute("idTipoUsuario", idTipoUsuarioStr);
			request.getRequestDispatcher("/views/admin/registrousuarios.jsp").forward(request, response);
			
		} catch (Exception e) {
			// Cualquier otro error inesperado
			e.printStackTrace();
			request.setAttribute("globalError", "Error interno del servidor. Por favor, intente nuevamente.");
			request.setAttribute("nombre", nombre);
			request.setAttribute("email", email);
			request.setAttribute("telefono", telefono);
			request.setAttribute("idTipoUsuario", idTipoUsuarioStr);
			request.getRequestDispatcher("/views/admin/registrousuarios.jsp").forward(request, response);
		}
	}

}
