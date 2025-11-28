package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.implementation.PedidoServiceImpl;
import service.interfaces.*;
import java.io.IOException;

/**
 * Servlet implementation class PedidoEditarServlet
 */
@WebServlet("/PedidoEditarServlet")
public class PedidoEditarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PedidoEditarServlet() {
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

        String idParam     = request.getParameter("idPedido");
        String estadoParam = request.getParameter("estado");

        if (idParam == null || estadoParam == null) {
            session.setAttribute("flashError", "Datos incompletos para cambiar estado.");
            response.sendRedirect(ctx + "/PedidoListarServlet");
            return;
        }

        try {
            int idPedido = Integer.parseInt(idParam);
            // 1 = Finalizado, 0 = Procesando
            boolean nuevoEstado = "1".equals(estadoParam);

            IPedidoService sv = new PedidoServiceImpl();
            boolean ok = sv.cambiarEstadoPedido(idPedido, nuevoEstado);

            if (ok) {
                session.setAttribute("flashSuccess", "Estado del pedido actualizado.");
            } else {
                session.setAttribute("flashError", "No se pudo actualizar el estado del pedido.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flashError", "Error interno al cambiar el estado del pedido.");
        }

        // Volver al listado
        response.sendRedirect(ctx + "/PedidoListarServlet");
    }

}
