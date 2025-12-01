package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Producto;
import model.Categoria;
import service.interfaces.IProductoService;
import service.interfaces.ICategoriaService;
import service.implementation.ProductoServiceImpl;
import service.implementation.CategoriaServiceImpl;

@WebServlet("/ProductoEditarServlet")
public class ProductoEditarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IProductoService  productoService  = new ProductoServiceImpl();
    private final ICategoriaService categoriaService = new CategoriaServiceImpl();

    public ProductoEditarServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        // 1) Tomar id del producto desde el link: ?id=...
        String id = request.getParameter("id");

        if (id == null || id.isBlank()) {
            request.getSession().setAttribute("flashError", "ID de producto inválido.");
            response.sendRedirect(ctx + "/ProductoListarServlet");
            return;
        }

        // 2) Buscar el producto
        Producto p = productoService.findById(id);

        if (p == null) {
            request.getSession().setAttribute("flashError", "No se encontró el producto.");
            response.sendRedirect(ctx + "/ProductoListarServlet");
            return;
        }

        // 3) Setear valores para el formulario
        request.setAttribute("idProducto",   p.getIdProducto());
        request.setAttribute("nombre",       p.getNombre());
        request.setAttribute("descripcion",  p.getDescripcion());
        request.setAttribute("precio",       p.getPrecio() != null ? String.valueOf(p.getPrecio()) : "");
        request.setAttribute("descuento",    String.valueOf(p.getDescuento()));
        request.setAttribute("urlImagen",    p.getUrlImagen());
        request.setAttribute("stock",        String.valueOf(p.getStock()));
        request.setAttribute("idCategoria",  String.valueOf(p.getIdCategoria()));

        // 4) Cargar categorías
        List<Categoria> categorias = categoriaService.findAll();
        request.setAttribute("categorias", categorias);

        // 5) Modo edición
        request.setAttribute("modo", "editar");

        request.setAttribute("contentPage", "/views/admin/crearproductos.jsp");
        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        // 1) Leer parámetros del formulario
        String idProductoParam  = request.getParameter("idProducto");
        String nombre           = request.getParameter("nombre");
        String descripcion      = request.getParameter("descripcion");
        String precioParam      = request.getParameter("precio");
        String descuentoParam   = request.getParameter("descuento");
        String urlImagen        = request.getParameter("urlImagen");
        String stockParam       = request.getParameter("stock");
        String idCategoriaParam = request.getParameter("idCategoria");

        // 2) Delegar toda la validación + actualización al service
        List<String> errores = new ArrayList<>();
        Producto actualizado = productoService.actualizarProductoDesdeFormulario(
                idProductoParam,
                nombre,
                descripcion,
                precioParam,
                descuentoParam,
                urlImagen,
                stockParam,
                idCategoriaParam,
                errores
        );

        // 3) Si hay errores → volver al formulario con datos repoblados
        if (actualizado == null) {
            if (!errores.isEmpty()) {
                StringBuilder sb = new StringBuilder("Se encontraron errores en el formulario:");
                for (String err : errores) {
                    sb.append("<br>• ").append(err);
                }
                request.setAttribute("globalError", sb.toString());
            } else {
                // fallback por si algo raro pasó
                request.setAttribute("globalError", "No se pudo actualizar el producto.");
            }

            // Repoblar valores ingresados
            request.setAttribute("idProducto",   idProductoParam);
            request.setAttribute("nombre",       nombre);
            request.setAttribute("descripcion",  descripcion);
            request.setAttribute("precio",       precioParam);
            request.setAttribute("descuento",    descuentoParam);
            request.setAttribute("urlImagen",    urlImagen);
            request.setAttribute("stock",        stockParam);
            request.setAttribute("idCategoria",  idCategoriaParam);

            // Categorías para el combo
            List<Categoria> categorias = categoriaService.findAll();
            request.setAttribute("categorias", categorias);

            // Modo edición
            request.setAttribute("modo", "editar");

            request.setAttribute("contentPage", "/views/admin/crearproductos.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
            return;
        }

        // 4) OK → mensaje flash + redirect al listado
        request.getSession().setAttribute("flashSuccess", "Producto actualizado correctamente.");
        response.sendRedirect(ctx + "/ProductoListarServlet");
    }
}
