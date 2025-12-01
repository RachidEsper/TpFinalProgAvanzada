package web.usuario; // o el que uses

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import model.Producto;
import model.Categoria;
import service.implementation.ProductoServiceImpl;
import service.implementation.CategoriaServiceImpl;
import service.interfaces.IProductoService;
import service.interfaces.ICategoriaService;

@WebServlet("/ProductoListarServlet")
public class ProductoListarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IProductoService productoService = new ProductoServiceImpl();
    private final ICategoriaService categoriaService = new CategoriaServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1) Traigo TODOS los productos
        List<Producto> productos = productoService.findAll();

        // 2) Traigo categorías para el combo de filtros
        List<Categoria> categorias = categoriaService.findAll();
        request.setAttribute("categorias", categorias);

        // 3) Leo parámetros de filtro
        String order       = request.getParameter("order");      // "precio_asc", "precio_desc"
        String stockParam  = request.getParameter("stock");      // "1" => solo con stock
        String catParam    = request.getParameter("categoria");  // id categoria

        // 4) Filtro por categoría
        if (catParam != null && !catParam.isBlank() && !"0".equals(catParam)) {
            try {
                int idCat = Integer.parseInt(catParam);
                productos = productos.stream()
                        .filter(p -> p.getIdCategoria() == idCat)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignore) {
            }
        }

        // 5) Filtro por stock (> 0)
        if ("1".equals(stockParam)) {
            productos = productos.stream()
                    .filter(p -> p.getStock() > 0)
                    .collect(Collectors.toList());
        }

        // 6) Ordenar por precio
        if ("precio_asc".equals(order)) {
            productos.sort(
                Comparator.comparing(
                    Producto::getPrecio,
                    Comparator.nullsLast(Double::compareTo)
                )
            );
        } else if ("precio_desc".equals(order)) {
            productos.sort(
                Comparator.comparing(
                    Producto::getPrecio,
                    Comparator.nullsLast(Double::compareTo)
                ).reversed()
            );
        }

        // 7) Enviar datos a la vista
        request.setAttribute("productos", productos);
        request.setAttribute("contentPage", "/views/admin/listarproductos.jsp");

        request.getRequestDispatcher("/views/base.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
