package web.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Usuario;

import java.io.IOException;
import java.util.List;

import dao.implementation.UsuarioDAOImpl;
import dao.interfaces.IUsuarioDAO;

/**
 * Servlet implementation class UsuarioListarServlet
 */
@WebServlet("/UsuarioListarServlet")
public class UsuarioListarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuarioListarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		IUsuarioDAO usuarioDAO = new UsuarioDAOImpl();
		List<Usuario> usuarios = usuarioDAO.findAll(); // Traé nombre,email, telefono, idTipoUsuario
	    request.setAttribute("usuarios", usuarios);
		request.setAttribute("contentPage", "/views/admin/listadousuarios.jsp");
		RequestDispatcher dispatcher = request.getRequestDispatcher("views/base.jsp");
		dispatcher.forward(request, response); 
	   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
