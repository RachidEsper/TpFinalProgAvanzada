package web.usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Pedido;
import model.Usuario;
import service.implementation.PedidoServiceImpl;
import service.interfaces.IPedidoService;

@WebServlet("/PedidoListarServlet")
public class PedidoListarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public PedidoListarServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario user = (session != null) ? (Usuario) session.getAttribute("user") : null;

        IPedidoService sv = new PedidoServiceImpl();

        boolean isAdmin = false;
        if (user != null && user.getTipo() != null) {
            isAdmin = (user.getTipo().getIdTipoUsuario() == 1);
        }

        List<Pedido> pedidos = new ArrayList<>();

        if (isAdmin) {
            // Admin ve todos
            pedidos = sv.findAll();
        } else if (user != null) {
            // Usuario normal: solo sus pedidos
            List<Pedido> todos = sv.findByUsuario(user.getIdUsuario());

            // Opcional: filtrar solo los que YA tienen dirección (es decir, ya fueron "enviados")
            for (Pedido p : todos) {
                String dir = p.getDireccionEntrega();
                if (dir != null && !dir.isBlank()) {
                    pedidos.add(p);
                }
            }
        }

        request.setAttribute("pedidos", pedidos);
        request.setAttribute("isAdmin", isAdmin);
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
