package web.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class PedidoCrearServlet
 */
@WebServlet("/PedidoCrearServlet")
public class PedidoCrearServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PedidoCrearServlet() {
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

        // 1) Leer qué producto se agregó
        String idProducto = request.getParameter("idProducto");

        // 2) Aquí iría la lógica de "carrito"
        //    Por ejemplo: agregar el idProducto a una lista en sesión o a la tabla pedido_detalle
        //    Ejemplo súper simple (para más adelante lo mejorás con un PedidoService):
        /*
        HttpSession session = request.getSession();
        List<String> carrito = (List<String>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        carrito.add(idProducto);
        session.setAttribute("carrito", carrito);
        */

        // 3) (Opcional) Mensaje flash de éxito
        HttpSession session = request.getSession();
        session.setAttribute("flashSuccess", "Producto agregado al pedido.");

        // 4) Redirigir de vuelta al listado de productos
        response.sendRedirect(ctx + "/ProductoListarServlet");
    }

}
