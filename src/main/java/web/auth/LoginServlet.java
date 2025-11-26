package web.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Usuario;
import service.implementation.IAuthServiceImpl;
import service.interfaces.IAuthService;
import dao.interfaces.IUsuarioDAO;
import java.io.IOException;

import dao.implementation.UsuarioDAOImpl;
import exception.DataAccessException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name="LoginServlet", urlPatterns={"/LoginServlet"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {

	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    IAuthService auth = new IAuthServiceImpl();
	    try {
	        Usuario user = auth.login(email, password);
	        if (user == null) {
	            request.setAttribute("globalError", "Credenciales inválidas.");
	            // no repobles el email si querés dejarlo vacío
	            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
	            return;
	        }

	        // A) si login devuelve Usuario con nombre:
	        String nombre = user.getNombre();

	        // guardá lo necesario en sesión
	        HttpSession session = request.getSession(true);
	        session.setAttribute("user", user);               // si usás el objeto
	        session.setAttribute("displayName", nombre);      // para la bienvenida

	         
	        response.sendRedirect(request.getContextPath() + "/views/base.jsp");
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("globalError", "Problema técnico. Intentá nuevamente.");
	        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
	    }
		  }


}
