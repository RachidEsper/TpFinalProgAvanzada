package service.implementation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import dao.implementation.PedidoDAOImpl;
import dao.interfaces.IPedidoDAO;
import model.DetallePedido;
import model.Pedido;
import model.Usuario;
import service.interfaces.IPedidoService;

public class PedidoServiceImpl implements IPedidoService {

    private final IPedidoDAO pedidoDAO;

    public PedidoServiceImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
    }

    /**
     * Regla de negocio:
     * - Si el usuario ya tiene un pedido ABIERTO (estado = false),
     *   lo devuelve (ese funciona como "carrito").
     * - Si no tiene, crea uno nuevo con total=0 y estado=false.
     */
    @Override
    public Pedido crearPedido(int idUsuario, String direccionEntrega) {

        // 1) Ver si ya tiene un pedido abierto
        List<Pedido> pedidosUsuario = pedidoDAO.findByUsuarioId(idUsuario);
        for (Pedido p : pedidosUsuario) {
            Boolean estado = p.getEstado();
            // Tomamos estado = false como "abierto"
            if (estado != null && !estado) {
                // Cargo detalles del pedido y lo devuelvo
                List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(p.getIdPedido());
                p.setDetalles(dets);
                return p;
            }
        }

        // 2) Si no hay pedido abierto, creo uno nuevo
        Pedido nuevo = new Pedido();

        // java.sql.Date aunque en BD tengas DATETIME; para el TP alcanza
        nuevo.setFecha(new Date(System.currentTimeMillis()));

        Usuario u = new Usuario();
        u.setIdUsuario(idUsuario);
        nuevo.setUsuario(u);

        nuevo.setEstado(false);                 // false = abierto
        nuevo.setDireccionEntrega(direccionEntrega);
        nuevo.setTotal(0.0);                    // sin renglones todavía

        Pedido creado = pedidoDAO.create(nuevo);
        if (creado != null) {
            creado.setDetalles(new ArrayList<>()); // lista vacía
        }
        return creado;
    }

    @Override
    public Pedido findById(int idPedido) {
        Pedido p = pedidoDAO.findById(idPedido);
        if (p == null) return null;

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
     * Devuelve el pedido ABIERTO (estado=false) de un usuario,
     * o null si no tiene ninguno.
     */
    @Override
    public Pedido findPedidoAbiertoByUsuario(int idUsuario) {
        List<Pedido> pedidos = pedidoDAO.findByUsuarioId(idUsuario);
        for (Pedido p : pedidos) {
            Boolean estado = p.getEstado();
            if (estado != null && !estado) {
                List<DetallePedido> dets = pedidoDAO.findDetallesByPedidoId(p.getIdPedido());
                p.setDetalles(dets);
                return p;
            }
        }
        return null;
    }

    /**
     * Finalizar pedido:
     * - recalcula el total a partir de los detalles
     * - marca estado=true (cerrado/finalizado)
     */
    @Override
    public boolean finalizarPedido(int idPedido) {
        Pedido p = pedidoDAO.findById(idPedido);
        if (p == null) return false;

        Double total = pedidoDAO.calcularTotalPedido(idPedido);
        if (total == null) total = 0.0;

        p.setTotal(total);
        p.setEstado(true); // true = finalizado

        return pedidoDAO.update(p) != null;
    }

    /**
     * Cancelar pedido:
     * Para simplificar el TP, lo manejamos como un delete.
     * Podrías diferenciarlo en el futuro con otra columna de estado.
     */
    @Override
    public boolean cancelarPedido(int idPedido) {
        return pedidoDAO.deleteById(idPedido);
    }

    /**
     * Eliminar pedido (acción más “administrativa”).
     * Por ahora hace lo mismo que cancelar: borrar pedido y sus detalles.
     */
    @Override
    public boolean eliminarPedido(int idPedido) {
        return pedidoDAO.deleteById(idPedido);
    }

	@Override
	public boolean cambiarEstadoPedido(int idPedido, boolean nuevoEstado) {
		// TODO Auto-generated method stub
		return false;
	}
}
