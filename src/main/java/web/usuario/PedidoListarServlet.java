package web.usuario;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Usuario;
import model.Pedido;
import model.DetallePedido;
import service.interfaces.IPedidoService;
import service.implementation.PedidoServiceImpl;

@WebServlet("/PedidoListarServlet")
public class PedidoListarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public PedidoListarServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IPedidoService sv = new PedidoServiceImpl();

        // Admin → ve todos
        List<Pedido> pedidos = sv.findAll();

        request.setAttribute("pedidos", pedidos);
        request.setAttribute("contentPage", "/views/admin/listarpedidos.jsp");

        request.getRequestDispatcher("/views/base.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
