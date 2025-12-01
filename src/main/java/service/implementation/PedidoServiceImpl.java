package service.implementation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.implementation.PedidoDAOImpl;
import dao.interfaces.IPedidoDAO;
import model.DetallePedido;
import model.Pedido;
import model.Producto;
import model.Usuario;
import service.interfaces.IPedidoService;

import service.interfaces.IProductoService;

public class PedidoServiceImpl implements IPedidoService {

	private final IPedidoDAO pedidoDAO;

	public PedidoServiceImpl() {
		this.pedidoDAO = new PedidoDAOImpl();
	}

	/**
	 * Regla de negocio: - Si el usuario ya tiene un pedido ABIERTO (estado =
	 * false), lo devuelve (ese funciona como "carrito"). - Si no tiene, crea uno
	 * nuevo con total=0 y estado=false.
	 */
	@Override
	public Pedido crearPedido(int idUsuario, String direccionEntrega) {

		// 1) Ver si ya tiene un "carrito" abierto
		// carrito = pedido con:
		// - estado = false (o null)
		// - direccion_entrega = null
		List<Pedido> pedidosUsuario = pedidoDAO.findByUsuarioId(idUsuario);
		Pedido pedidoAbierto = null;

		for (Pedido p : pedidosUsuario) {
			Boolean estado = p.getEstado();
			boolean esAbierto = (estado == null || !estado) && p.getDireccionEntrega() == null; // 👈 clave

			if (esAbierto) {
				// si hay varios, nos quedamos con el de mayor id (el último creado)
				if (pedidoAbierto == null || p.getIdPedido() > pedidoAbierto.getIdPedido()) {
					pedidoAbierto = p;
				}
			}
		}

		// Si ya tenía un carrito, cargo detalles y lo devuelvo
		if (pedidoAbierto != null) {
			List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(pedidoAbierto.getIdPedido());
			pedidoAbierto.setDetalles(dets);
			return pedidoAbierto;
		}

		// 2) Si no hay carrito abierto, creo uno nuevo
		Pedido nuevo = new Pedido();

		nuevo.setFecha(new Date(System.currentTimeMillis()));

		Usuario u = new Usuario();
		u.setIdUsuario(idUsuario);
		nuevo.setUsuario(u);

		nuevo.setEstado(false); // false = abierto/carrito
		nuevo.setDireccionEntrega(direccionEntrega); // normalmente null para carrito
		nuevo.setTotal(0.0); // sin renglones todavía

		Pedido creado = pedidoDAO.create(nuevo);
		if (creado != null) {
			creado.setDetalles(new ArrayList<>()); // lista vacía
		}
		return creado;
	}

	@Override
	public Pedido findById(int idPedido) {
		Pedido p = pedidoDAO.findById(idPedido);
		if (p == null)
			return null;

		List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(idPedido);
		p.setDetalles(dets);
		return p;
	}

	@Override
	public List<Pedido> findAll() {
		List<Pedido> pedidos = pedidoDAO.findAll();
		for (Pedido p : pedidos) {
			List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(p.getIdPedido());
			p.setDetalles(dets);
		}
		return pedidos;
	}

	@Override
	public List<Pedido> findByUsuario(int idUsuario) {
		List<Pedido> pedidos = pedidoDAO.findByUsuarioId(idUsuario);
		for (Pedido p : pedidos) {
			List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(p.getIdPedido());
			p.setDetalles(dets);
		}
		return pedidos;
	}

	/**
	 * Devuelve el pedido ABIERTO (estado=false) de un usuario, o null si no tiene
	 * ninguno.
	 */
	@Override
	public Pedido findPedidoAbiertoByUsuario(int idUsuario) {
		List<Pedido> pedidos = pedidoDAO.findByUsuarioId(idUsuario);

		// Buscamos el pedido ABIERTO (carrito):
		// - estado = false (o null -> lo tratamos como false)
		// - direccion_entrega = null (todavía no fue "iniciado" por el usuario)
		Pedido pedidoAbierto = null;
		for (Pedido p : pedidos) {
			Boolean estado = p.getEstado(); // false = abierto / en proceso
			boolean esAbierto = (estado == null || !estado) && p.getDireccionEntrega() == null; // 👉 clave: sin
																								// dirección => carrito

			if (esAbierto) {
				if (pedidoAbierto == null || p.getIdPedido() > pedidoAbierto.getIdPedido()) {
					pedidoAbierto = p;
				}
			}
		}

		// Si encontramos uno, cargamos sus detalles
		if (pedidoAbierto != null) {
			List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(pedidoAbierto.getIdPedido());
			pedidoAbierto.setDetalles(dets);
		}

		return pedidoAbierto;
	}

	/**
	 * Finalizar pedido: - recalcula el total a partir de los detalles - marca
	 * estado=true (cerrado/finalizado)
	 */

	/**
	 * Cancelar pedido: Para simplificar el TP, lo manejamos como un delete. Podrías
	 * diferenciarlo en el futuro con otra columna de estado.
	 */
	@Override
	public boolean cancelarPedido(int idPedido) {
		return pedidoDAO.deleteById(idPedido);
	}

	/**
	 * Eliminar pedido (acción más “administrativa”). Por ahora hace lo mismo que
	 * cancelar: borrar pedido y sus detalles.
	 */
	@Override
	public boolean eliminarPedido(int idPedido) {
		return pedidoDAO.deleteById(idPedido);
	}

	@Override
	public boolean cambiarEstadoPedido(int idPedido, boolean nuevoEstado) {
		// 1) Traer el pedido para verificar que exista
		Pedido pedido = pedidoDAO.findById(idPedido);
		if (pedido == null) {
			return false; // no existe
		}

		// 2) Si ya tiene ese estado, no hace falta tocar la BD
		Boolean estadoActual = pedido.getEstado();
		if (estadoActual != null && estadoActual == nuevoEstado) {
			return true;
		}

		// 3) Actualizar solo el campo estado (sin tocar stock ni total)
		// Esto es para uso del ADMIN desde el listado.
		boolean actualizado = pedidoDAO.updateEstadoPedido(idPedido, nuevoEstado);
		if (actualizado) {
			pedido.setEstado(nuevoEstado);
		}

		return actualizado;
	}

	@Override
	public boolean finalizarPedido(int idPedido, String direccionEntrega) {
		// 1) Traer el pedido
		Pedido p = pedidoDAO.findById(idPedido);
		IProductoService productoService = new ProductoServiceImpl();

		if (p == null)
			return false;

		// Si ya está FINALIZADO (estado=true) no hacemos nada
		if (Boolean.TRUE.equals(p.getEstado())) {
			return false;
		}

		// 2) Traer detalles del pedido
		List<DetallePedido> detalles = pedidoDAO.findDetallesByPedidoId(idPedido);
		if (detalles == null || detalles.isEmpty()) {
			// No tiene renglones: no se puede finalizar
			return false;
		}

		// 3) Restar stock de cada producto
		for (DetallePedido d : detalles) {
			if (d.getProducto() == null || d.getProducto().getIdProducto() == null) {
				continue;
			}

			String idProd = d.getProducto().getIdProducto();
			int cant = d.getCantidad();

			// Buscar producto
			Producto prod = productoService.findById(idProd);
			if (prod == null) {
				continue; // algo inconsistente, lo saltamos
			}

			int stockActual = prod.getStock();
			int nuevoStock = stockActual - cant;
			if (nuevoStock < 0) {
				nuevoStock = 0; // no dejamos stock negativo
			}

			prod.setStock(nuevoStock);

			// Actualizar producto en BD
			productoService.updateProducto(prod); // usa el método real que tengas
		}

		// 4) Recalcular total del pedido
		Double total = pedidoDAO.calcularTotalPedido(idPedido);
		if (total == null)
			total = 0.0;

		p.setTotal(total);
		p.setDireccionEntrega(direccionEntrega);

		// OJO: NO cambiamos estado a true acá.
		// estado=false + direccionEntrega != null => "INICIADO / EN PROCESO"
		// El admin será quien más tarde lo marque como finalizado (estado=true).
		// Si querés asegurarte de que quede en false:
		p.setEstado(false);

		// 5) Guardar cambios del pedido
		return pedidoDAO.update(p) != null;
	}

	@Override
	public boolean addDetallePedido(DetallePedido detalle) {
		// Validaciones básicas para evitar NPE raros
		if (detalle == null || detalle.getPedido() == null || detalle.getProducto() == null
				|| detalle.getPedido().getIdPedido() == 0 || detalle.getProducto().getIdProducto() == null) {
			return false;
		}

		return pedidoDAO.addDetallePedido(detalle);
	}
	
	
	@Override
	public boolean agregarProductoAlCarrito(
	        int idUsuario,
	        String idProductoParam,
	        String cantidadParam,
	        Map<String, String> errors
	) {
	    if (errors == null) {
	        errors = new java.util.HashMap<>();
	    }

	    // Validar producto
	    if (idProductoParam == null || idProductoParam.isBlank()) {
	        errors.put("producto", "Producto inválido.");
	        return false;
	    }

	    // Cantidad
	    int cantidad = 1;
	    if (cantidadParam != null && !cantidadParam.isBlank()) {
	        try {
	            cantidad = Integer.parseInt(cantidadParam);
	            if (cantidad <= 0) {
	                cantidad = 1;
	            }
	        } catch (NumberFormatException e) {
	            errors.put("cantidad", "Cantidad inválida.");
	            return false;
	        }
	    }

	    // 1) Buscar/crear pedido abierto (carrito)
	    Pedido pedido = this.findPedidoAbiertoByUsuario(idUsuario);
	    if (pedido == null) {
	        // lo creamos sin dirección aún
	        pedido = this.crearPedido(idUsuario, null);
	    }
	    if (pedido == null) {
	        errors.put("global", "No se pudo crear el pedido.");
	        return false;
	    }

	    // 2) Buscar producto
	    IProductoService productoService = new ProductoServiceImpl();
	    Producto prod = productoService.findById(idProductoParam);
	    if (prod == null) {
	        errors.put("producto", "El producto seleccionado no existe.");
	        return false;
	    }

	    double precioUnitario = (prod.getPrecio() != null ? prod.getPrecio() : 0.0);

	    // 3) Crear detalle
	    DetallePedido det = new DetallePedido();
	    det.setPedido(pedido);
	    det.setProducto(prod);
	    det.setCantidad(cantidad);
	    det.setPrecioUnitario(precioUnitario);

	    boolean ok = this.addDetallePedido(det);
	    if (!ok) {
	        errors.put("global", "No se pudo agregar el producto al pedido.");
	    }

	    return ok;
	}

	@Override
	public boolean finalizarPedidoDesdeFormulario(
	        int idUsuario,
	        String idPedidoParam,
	        String direccionEntrega,
	        Map<String, String> errors
	) {
	    if (errors == null) {
	        errors = new java.util.HashMap<>();
	    }

	    Integer idPedido = null;

	    // Validar ID pedido
	    if (idPedidoParam == null || idPedidoParam.isBlank()) {
	        errors.put("idPedido", "ID de pedido inválido.");
	    } else {
	        try {
	            idPedido = Integer.parseInt(idPedidoParam);
	        } catch (NumberFormatException e) {
	            errors.put("idPedido", "ID de pedido inválido.");
	        }
	    }

	    // Validar dirección
	    if (direccionEntrega == null || direccionEntrega.isBlank()) {
	        errors.put("direccionEntrega", "Debe ingresar una dirección de entrega.");
	    }

	    if (!errors.isEmpty()) {
	        return false;
	    }

	    // Verificar que el pedido exista y pertenezca al usuario
	    Pedido p = this.findById(idPedido);
	    if (p == null) {
	        errors.put("global", "El pedido no existe.");
	        return false;
	    }
	    if (p.getUsuario() == null || p.getUsuario().getIdUsuario() != idUsuario) {
	        errors.put("global", "El pedido no pertenece al usuario.");
	        return false;
	    }

	    // Usar la lógica que ya tenés en finalizarPedido(...)
	    boolean ok = this.finalizarPedido(idPedido, direccionEntrega);
	    if (!ok) {
	        errors.put("global", "No se pudo finalizar el pedido.");
	    }

	    return ok;
	}


}
