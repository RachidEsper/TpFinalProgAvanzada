package web.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.implementation.ProductoServiceImpl;
import service.interfaces.IProductoService;
import model.Producto;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class ProductoListarServlet
 */
@WebServlet("/ProductoListarServlet")
public class ProductoListarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductoListarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IProductoService sv = new ProductoServiceImpl();

        // por ahora: traemos todos los productos sin filtro
        List<Producto> productos = sv.findAll();
        request.setAttribute("productos", productos);

        // indicamos qué JSP va dentro de base.jsp
        request.setAttribute("contentPage", "/views/admin/listarproductos.jsp");

        request.getRequestDispatcher("/views/base.jsp")
               .forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
