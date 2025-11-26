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

/**
 * Servlet implementation class UsuarioCrudServlet
 */
@WebServlet("/UsuarioCrudServlet")
public class UsuarioCrudServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuarioCrudServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtener todos los usuarios de la base de datos
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		java.util.List<Usuario> usuarios = usuarioDAO.findAll();
		
		// Guardar la lista en el request
		request.setAttribute("usuarios", usuarios);
		
		// Especificar qué página de contenido incluir dentro del layout
		request.setAttribute("contentPage", "/views/admin/listadousuarios.jsp");
		
		// Redirigir al layout base que incluirá el contenido
		request.getRequestDispatcher("/base.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nombre=request.getParameter("nombre");
	    String email=request.getParameter("email");
	    String telefono=request.getParameter("telefono");
	    String password=request.getParameter("password");
	    int idTipoUsuario=0;
	    if(request.getParameter("tipoUsuario")=="1") {
	    	 idTipoUsuario=1;
	    }
	    else {
	    	 idTipoUsuario=2;
	    }
	    
	  System.out.println("Nombre: " + nombre);
	  System.out.println("Email: " + email);
	  System.out.println("Teléfono: " + telefono);
	  System.out.println("Password: " + password);
	  System.out.println("ID Tipo Usuario: " + idTipoUsuario);
	  
	    
	    
	    
	    // Lógica para crear, actualizar o eliminar usuarios según los parámetros recibidos
	    IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
	    // Ejemplo de creación de usuario
	    Usuario nuevoUsuario = new Usuario();
	    nuevoUsuario.setNombre(nombre);
	    nuevoUsuario.setEmail(email);
	    nuevoUsuario.setTelefono(telefono);
//	    nuevoUsuario.setContrasenia(password);
	    // Asignar el tipo de usuario según el parámetro recibido
	    TipoUsuario tipoUsuario = new TipoUsuario();
	    tipoUsuario.setIdTipoUsuario(idTipoUsuario);
	    nuevoUsuario.setTipo(tipoUsuario);
	    usuarioDAO.create(nuevoUsuario);
	    
	    
	    // Redirigir o reenviar a la página adecuada después de la operación
	    request.getSession().setAttribute("flashSuccess", "Usuario registrado correctamente.");
	    
	}

}
