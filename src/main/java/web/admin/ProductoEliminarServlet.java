package web.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.interfaces.IProductoService;
import service.implementation.ProductoServiceImpl;

@WebServlet("/ProductoEliminarServlet")
public class ProductoEliminarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IProductoService productoService = new ProductoServiceImpl();

    public ProductoEliminarServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        String idProducto = request.getParameter("idProducto");

        if (idProducto == null || idProducto.isBlank()) {
            request.getSession().setAttribute("flashError", "ID de producto inválido.");
            response.sendRedirect(ctx + "/ProductoListarServlet");
            return;
        }

        try {
            // 👉 El DAO devuelve true si se eliminó, false si hubo error (FK, etc.)
            boolean eliminado = productoService.deleteById(idProducto);

            if (eliminado) {
                request.getSession().setAttribute("flashSuccess",
                        "Producto eliminado correctamente.");
            } else {
                // 🔴 Caso típico: FK en detalle_pedido
                request.getSession().setAttribute("flashError",
                        "No se puede eliminar un producto que ya está en un pedido en proceso.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("flashError",
                    "Ocurrió un error inesperado al intentar eliminar el producto.");
        }

        // Siempre volvemos al listado
        response.sendRedirect(ctx + "/ProductoListarServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/ProductoListarServlet");
    }
}
