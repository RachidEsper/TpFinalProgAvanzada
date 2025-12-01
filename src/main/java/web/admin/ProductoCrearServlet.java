package web.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dao.implementation.CategoriaDAOImpl;
import dao.interfaces.ICategoriaDAO;
import model.Categoria;
import model.Producto;
import service.implementation.CategoriaServiceImpl;
import service.implementation.ProductoServiceImpl;
import service.interfaces.ICategoriaService;
import service.interfaces.IProductoService;
import dao.interfaces.IProductoDAO;
/**
 * Servlet implementation class ProductoCrearServlet
 */
@WebServlet("/ProductoCrearServlet")
public class ProductoCrearServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductoCrearServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Cargás las categorías desde el DAO
		ICategoriaDAO categoriaDAO = new CategoriaDAOImpl();
		
		List<Categoria> categorias = categoriaDAO.findall();
		request.setAttribute("categorias", categorias);

		// Indicás qué JSP de contenido querés meter dentro de base.jsp
		request.setAttribute("contentPage", "/views/admin/crearproductos.jsp");

		// Forward al layout base
		request.getRequestDispatcher("/views/base.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("UTF-8");
	    String ctx = request.getContextPath();

	    ICategoriaService categoriaService = new CategoriaServiceImpl();
	    IProductoService  productoService  = new ProductoServiceImpl();

	    // 1) Leer parámetros del formulario (sin validar acá)
	    String idProductoParam  = request.getParameter("idProducto");
	    String nombre           = request.getParameter("nombre");
	    String descripcion      = request.getParameter("descripcion");
	    String precioParam      = request.getParameter("precio");
	    String descuentoParam   = request.getParameter("descuento");   // opcional
	    String urlImagen        = request.getParameter("urlImagen");   // opcional
	    String stockParam       = request.getParameter("stock");
	    String idCategoriaParam = request.getParameter("idCategoria");

	    // 2) Llamar al service para que valide + cree el producto
	    List<String> errores = new ArrayList<>();
	    Producto nuevoProducto = productoService.crearProductoDesdeFormulario(
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

	    // 3) Si hubo errores → volver al formulario con mensajes y valores repoblados
	    if (nuevoProducto == null) {
	        StringBuilder sb = new StringBuilder("Se encontraron errores en el formulario:");
	        for (String err : errores) {
	            sb.append("<br>• ").append(err);
	        }
	        request.setAttribute("globalError", sb.toString());

	        // Repoblar valores originales del form
	        request.setAttribute("idProducto",   idProductoParam);
	        request.setAttribute("nombre",       nombre);
	        request.setAttribute("descripcion",  descripcion);
	        request.setAttribute("precio",       precioParam);
	        request.setAttribute("descuento",    descuentoParam);
	        request.setAttribute("urlImagen",    urlImagen);
	        request.setAttribute("stock",        stockParam);
	        request.setAttribute("idCategoria",  idCategoriaParam);

	        // Recargar categorías para el combo
	        List<Categoria> categorias = categoriaService.findAll();
	        request.setAttribute("categorias", categorias);

	        // Volver al formulario dentro de base.jsp
	        request.setAttribute("contentPage", "/views/admin/crearproductos.jsp");
	        request.getRequestDispatcher("/views/base.jsp").forward(request, response);
	        return;
	    }

	    // 4) Si todo OK → mensaje flash + redirect al listado
	    request.getSession().setAttribute("flashSuccess", "Producto creado correctamente.");
	    response.sendRedirect(ctx + "/ProductoListarServlet");
	}



}
