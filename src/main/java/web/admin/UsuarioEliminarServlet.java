package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.implementation.UsuarioServiceImpl;
import service.interfaces.IUsuarioService;

import java.io.IOException;

/**
 * Servlet implementation class UsuarioEliminarServlet
 */
@WebServlet("/UsuarioEliminarServlet")
public class UsuarioEliminarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuarioEliminarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	 @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String ctx = request.getContextPath();
	        HttpSession session = request.getSession();

	        String idParam = request.getParameter("id");

	        if (idParam == null || idParam.isBlank()) {
	            session.setAttribute("flashError", "ID de usuario inválido.");
	            response.sendRedirect(ctx + "/UsuarioListarServlet");
	            return;
	        }

	        int id = Integer.parseInt(idParam);

	        IUsuarioService sv = new UsuarioServiceImpl();
	        boolean eliminar = sv.deleteUsuario(id); 

	        if (eliminar) {
	            session.setAttribute("flashSuccess", "Usuario eliminado correctamente.");
	        } else {
	            session.setAttribute("flashError", "El usuario no pudo ser eliminado, tiene pedidos asociados.");
	        }

	        response.sendRedirect(ctx + "/UsuarioListarServlet");
	    }

}
