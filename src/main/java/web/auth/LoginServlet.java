package web.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Usuario;
import service.implementation.IAuthServiceImpl;
import service.interfaces.IAuthService;

import java.io.IOException;

import exception.DataAccessException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name="LoginServlet", urlPatterns={"/login"})
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
		        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
		        return;
		      }

		      // Éxito: guardo en sesión y redirijo a /base.jsp en la RAÍZ DE WEBAPP
		      request.getSession().setAttribute("user", user);
		      response.sendRedirect(request.getContextPath() + "/base.jsp");
		      return;

		    } catch (DataAccessException dae) {
		      dae.printStackTrace();
		      request.setAttribute("globalError", "Problema técnico. Intentá nuevamente.");
		      request.setAttribute("email", email);
		      request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
		    } catch (Exception ex) {
		      ex.printStackTrace();
		      request.setAttribute("globalError", "Ocurrió un error inesperado.");
		      request.setAttribute("email", email);
		      request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
		    }
		  }


}
