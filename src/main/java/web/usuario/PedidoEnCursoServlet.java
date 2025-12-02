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

import model.DetallePedido;
import model.Pedido;
import model.Usuario;
import service.implementation.PedidoServiceImpl;
import service.interfaces.IPedidoService;

@WebServlet("/PedidoEnCursoServlet")
public class PedidoEnCursoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Service
    private final IPedidoService pedidoService = new PedidoServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    /**
     * GET:
     *  - Muestra el pedido abierto del usuario (carrito) en crearpedido.jsp
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario user = (session != null) ? (Usuario) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Traigo el pedido ABIERTO del usuario (estado = false, sin dirección)
        Pedido pedido = pedidoService.findPedidoAbiertoByUsuario(user.getIdUsuario());

        if (pedido != null) {
            request.setAttribute("pedido", pedido);
            List<DetallePedido> detalles = pedido.getDetalles();
            request.setAttribute("detalles", detalles);

            // Calcular total
            Double total = 0.0;
            if (detalles != null) {
                for (DetallePedido d : detalles) {
                    double pUnit = (d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0.0);
                    total += pUnit * d.getCantidad();
                }
            }
            request.setAttribute("total", total);
        }

        request.setAttribute("contentPage", "/views/usuario/crearpedido.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
    }

    /**
     * POST:
     *  - accion=agregar   → agrega un producto al pedido abierto (o lo crea)
     *  - accion=finalizar → finaliza el pedido (lo deja en estado "Procesando")
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        HttpSession session = request.getSession(false);
        Usuario user = (session != null) ? (Usuario) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(ctx + "/LoginServlet");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            // por defecto muestro el carrito
            doGet(request, response);
            return;
        }

        // =======================
        // 1) AGREGAR PRODUCTO
        // =======================
        if ("agregar".equals(accion)) {

            String idProducto = request.getParameter("idProducto");
            String cantParam  = request.getParameter("cantidad");

            Map<String, String> errors = new HashMap<>();

            boolean ok = pedidoService.agregarProductoAlCarrito(
                    user.getIdUsuario(),
                    idProducto,
                    cantParam,
                    errors
            );

            if (!ok) {
                String msg = buildErrorHtml(errors, "No se pudo agregar el producto al pedido.");
                session.setAttribute("flashError", msg);
                // Lo mando de nuevo al listado de productos
                response.sendRedirect(ctx + "/ListadoProductosServlet");
            } else {
                session.setAttribute("flashSuccess", "Producto agregado al pedido.");
                // Mostrar el carrito
                response.sendRedirect(ctx + "/PedidoEnCursoServlet");
            }
            return;
        }

        // =======================
        // 2) FINALIZAR PEDIDO
        // =======================
        if ("finalizar".equals(accion)) {

            String idPedidoParam = request.getParameter("idPedido");
            String direccion     = request.getParameter("direccionEntrega");

            Map<String, String> errors = new HashMap<>();

            boolean ok = pedidoService.finalizarPedidoDesdeFormulario(
                    user.getIdUsuario(),
                    idPedidoParam,
                    direccion,
                    errors
            );

            if (!ok) {
                if (!errors.isEmpty()) {
                    // Mostrar errores en la misma vista del carrito
                    String msg = buildErrorHtml(errors, "Se encontraron errores:");
                    request.setAttribute("globalError", msg);
                    doGet(request, response); // recarga datos del carrito y hace forward
                    return;
                } else {
                    // Error genérico
                    session.setAttribute("flashError", "No se pudo finalizar el pedido.");
                    response.sendRedirect(ctx + "/PedidoEnCursoServlet");
                    return;
                }
            }

            // OK
            session.setAttribute("flashSuccess", "Pedido Iniciado correctamente.");
            response.sendRedirect(ctx + "/PedidoListarServlet");
            return;
        }

        // Cualquier otra acción → mostrar pedido en curso
        doGet(request, response);
    }

    /**
     * Helper para armar un mensaje HTML con los errores.
     */
    private String buildErrorHtml(Map<String, String> errors, String defaultMsg) {
        if (errors == null || errors.isEmpty()) {
            return defaultMsg;
        }
        StringBuilder sb = new StringBuilder(defaultMsg);
        for (String msg : errors.values()) {
            if (msg == null || msg.isBlank()) continue;
            sb.append("<br>• ").append(msg);
        }
        return sb.toString();
    }
}
