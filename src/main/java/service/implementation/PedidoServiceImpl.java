package service.implementation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.implementation.PedidoDAOImpl;
import dao.implementation.UsuarioDAOImpl;        
import dao.interfaces.IPedidoDAO;
import dao.interfaces.IUsuarioDAO;             

import model.DetallePedido;
import model.Pedido;
import model.Producto;
import model.Usuario;
import service.interfaces.IPedidoService;
import service.interfaces.IProductoService;

public class PedidoServiceImpl implements IPedidoService {

    private final IPedidoDAO pedidoDAO;
    private final IUsuarioDAO usuarioDAO;     

    public PedidoServiceImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl(); 
    }

    /**
     * Regla de negocio:
     *  - Si el usuario ya tiene un pedido ABIERTO (estado = false), lo devuelve.
     *  - Si no tiene, crea uno nuevo con total=0 y estado=false.
     */
    @Override
    public Pedido crearPedido(int idUsuario, String direccionEntrega) {

        // 1) Ver si ya tiene un "carrito" abierto
        List<Pedido> pedidosUsuario = pedidoDAO.findByUsuarioId(idUsuario);
        Pedido pedidoAbierto = null;

        for (Pedido p : pedidosUsuario) {
            Boolean estado = p.getEstado();
            boolean esAbierto = (estado == null || !estado) && p.getDireccionEntrega() == null;

            if (esAbierto) {
                if (pedidoAbierto == null || p.getIdPedido() > pedidoAbierto.getIdPedido()) {
                    pedidoAbierto = p;
                }
            }
        }

        // Si ya tenía un carrito, cargo detalles y usuario y lo devuelvo
        if (pedidoAbierto != null) {
            cargarDetalles(pedidoAbierto);   // 🔹 helper
            cargarUsuario(pedidoAbierto);    // 🔹 helper
            return pedidoAbierto;
        }

        // 2) Si no hay carrito abierto, creo uno nuevo
        Pedido nuevo = new Pedido();

        nuevo.setFecha(new Date(System.currentTimeMillis()));

        Usuario u = new Usuario();
        u.setIdUsuario(idUsuario);
        nuevo.setUsuario(u);

        nuevo.setEstado(false);              // false = abierto/carrito
        nuevo.setDireccionEntrega(direccionEntrega); // normalmente null para carrito
        nuevo.setTotal(0.0);                 // sin renglones todavía

        Pedido creado = pedidoDAO.create(nuevo);
        if (creado != null) {
            creado.setDetalles(new ArrayList<>()); // lista vacía
            cargarUsuario(creado);                 // 🔹 por si lo querés mostrar inmediatamente
        }
        return creado;
    }

    @Override
    public Pedido findById(int idPedido) {
        Pedido p = pedidoDAO.findById(idPedido);
        if (p == null) return null;

        cargarDetalles(p);   // 🔹
        cargarUsuario(p);    // 🔹

        return p;
    }

    @Override
    public List<Pedido> findAll() {
        List<Pedido> pedidos = pedidoDAO.findAll();
        for (Pedido p : pedidos) {
            cargarDetalles(p);   // 🔹
            cargarUsuario(p);    // 🔹
        }
        return pedidos;
    }

    @Override
    public List<Pedido> findByUsuario(int idUsuario) {
        List<Pedido> pedidos = pedidoDAO.findByUsuarioId(idUsuario);
        for (Pedido p : pedidos) {
            cargarDetalles(p);   // 🔹
            cargarUsuario(p);    // 🔹 (igual ya sabemos quién es, pero queda consistente)
        }
        return pedidos;
    }

    /**
     * Devuelve el pedido ABIERTO (estado=false) de un usuario, o null si no tiene.
     */
    @Override
    public Pedido findPedidoAbiertoByUsuario(int idUsuario) {
        List<Pedido> pedidos = pedidoDAO.findByUsuarioId(idUsuario);

        Pedido pedidoAbierto = null;
        for (Pedido p : pedidos) {
            Boolean estado = p.getEstado();
            boolean esAbierto = (estado == null || !estado) && p.getDireccionEntrega() == null;

            if (esAbierto) {
                if (pedidoAbierto == null || p.getIdPedido() > pedidoAbierto.getIdPedido()) {
                    pedidoAbierto = p;
                }
            }
        }

        if (pedidoAbierto != null) {
            cargarDetalles(pedidoAbierto);  
            cargarUsuario(pedidoAbierto);    
        }

        return pedidoAbierto;
    }

    @Override
    public boolean cancelarPedido(int idPedido) {
        return pedidoDAO.deleteById(idPedido);
    }

    @Override
    public boolean eliminarPedido(int idPedido) {
        return pedidoDAO.deleteById(idPedido);
    }

    @Override
    public boolean cambiarEstadoPedido(int idPedido, boolean nuevoEstado) {
        Pedido pedido = pedidoDAO.findById(idPedido);
        if (pedido == null) {
            return false;
        }

        Boolean estadoActual = pedido.getEstado();
        if (estadoActual != null && estadoActual == nuevoEstado) {
            return true;
        }

        boolean actualizado = pedidoDAO.updateEstadoPedido(idPedido, nuevoEstado);
        if (actualizado) {
            pedido.setEstado(nuevoEstado);
        }

        return actualizado;
    }

    @Override
    public boolean finalizarPedido(int idPedido, String direccionEntrega) {
        Pedido p = pedidoDAO.findById(idPedido);
        IProductoService productoService = new ProductoServiceImpl();

        if (p == null) return false;

        if (Boolean.TRUE.equals(p.getEstado())) {
            return false;
        }

        List<DetallePedido> detalles = pedidoDAO.findDetallesByPedidoId(idPedido);
        if (detalles == null || detalles.isEmpty()) {
            return false;
        }

        // Restar stock
        for (DetallePedido d : detalles) {
            if (d.getProducto() == null || d.getProducto().getIdProducto() == null) {
                continue;
            }

            String idProd = d.getProducto().getIdProducto();
            int cant = d.getCantidad();

            Producto prod = productoService.findById(idProd);
            if (prod == null) {
                continue;
            }

            int stockActual = prod.getStock();
            int nuevoStock = stockActual - cant;
            if (nuevoStock < 0) {
                nuevoStock = 0;
            }

            prod.setStock(nuevoStock);
            productoService.updateProducto(prod);
        }

        Double total = pedidoDAO.calcularTotalPedido(idPedido);
        if (total == null) total = 0.0;

        p.setTotal(total);
        p.setDireccionEntrega(direccionEntrega);
        p.setEstado(false);   // sigue "en proceso"

        return pedidoDAO.update(p) != null;
    }

    @Override
    public boolean addDetallePedido(DetallePedido detalle) {
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

        if (idProductoParam == null || idProductoParam.isBlank()) {
            errors.put("producto", "Producto inválido.");
            return false;
        }

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

        Pedido pedido = this.findPedidoAbiertoByUsuario(idUsuario);
        if (pedido == null) {
            pedido = this.crearPedido(idUsuario, null);
        }
        if (pedido == null) {
            errors.put("global", "No se pudo crear el pedido.");
            return false;
        }

        IProductoService productoService = new ProductoServiceImpl();
        Producto prod = productoService.findById(idProductoParam);
        if (prod == null) {
            errors.put("producto", "El producto seleccionado no existe.");
            return false;
        }

        double precioUnitario = (prod.getPrecio() != null ? prod.getPrecio() : 0.0);

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

        if (idPedidoParam == null || idPedidoParam.isBlank()) {
            errors.put("idPedido", "ID de pedido inválido.");
        } else {
            try {
                idPedido = Integer.parseInt(idPedidoParam);
            } catch (NumberFormatException e) {
                errors.put("idPedido", "ID de pedido inválido.");
            }
        }

        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            errors.put("direccionEntrega", "Debe ingresar una dirección de entrega.");
        }

        if (!errors.isEmpty()) {
            return false;
        }

        Pedido p = this.findById(idPedido);  
        if (p == null) {
            errors.put("global", "El pedido no existe.");
            return false;
        }
        if (p.getUsuario() == null || p.getUsuario().getIdUsuario() != idUsuario) {
            errors.put("global", "El pedido no pertenece al usuario.");
            return false;
        }

        boolean ok = this.finalizarPedido(idPedido, direccionEntrega);
        if (!ok) {
            errors.put("global", "No se pudo finalizar el pedido.");
        }

        return ok;
    }

    // ===================== HELPERS PRIVADOS =====================

    /** Carga los detalles en un pedido (si no están ya). */
    private void cargarDetalles(Pedido p) {
        if (p == null) return;
        List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(p.getIdPedido());
        p.setDetalles(dets);
    }

    /** Carga el Usuario completo desde BD usando el id_usuario del pedido. */
    private void cargarUsuario(Pedido p) {
        if (p == null) return;
        Usuario u = p.getUsuario();
        if (u == null) return;

        int idUsu = u.getIdUsuario();
        if (idUsu <= 0) return;

        Usuario uCompleto = usuarioDAO.findById(idUsu);
        if (uCompleto != null) {
            p.setUsuario(uCompleto);
        }
    }
}
