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
	        // 2) Autenticación
	        Usuario user = auth.login(email, password);

	        if (user == null) {
	        	// Credenciales inválidas → volver al login con mensaje global
	            request.setAttribute("globalError", "Credenciales inválidas.");
	            request.setAttribute("email", email); // si querés repoblar el email
	            getServletContext()
	                .getRequestDispatcher("/views/auth/login.jsp")
	                .forward(request, response);
	            return;
	        }

	        // (éxito: por ahora no hacemos nada porque no querés /home todavía)
	        // Cuando lo hagas: guardar en sesión y redirect a /home.

	    } catch (DataAccessException dae) {
	        // 3) Error técnico de BD → volver al login sin 500
	    	 dae.printStackTrace(); // <-- para saber si fue BD
	        request.setAttribute("globalError", "Problema técnico. Intentá nuevamente.");
	        request.setAttribute("email", email);
	        getServletContext()
	            .getRequestDispatcher("/views/auth/login.jsp")
	            .forward(request, response);
	        return;

	    } catch (Exception ex) {
	        // 4) Cualquier otro error inesperado
	    	 ex.printStackTrace(); // <-- para ver si fue IllegalArgumentException/NPE, etc.
	        request.setAttribute("globalError", "Ocurrió un error inesperado.");
	        request.setAttribute("email", email);
	        getServletContext()
	            .getRequestDispatcher("/views/auth/login.jsp")
	            .forward(request, response);
	        return;
	    }
	}


}
