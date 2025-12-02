package dao.interfaces;

import java.util.List;

import model.DetallePedido;
import model.Pedido;

public interface IPedidoDAO {

	// CRUD básico
	List<Pedido> findAll();
	Pedido findById(int idPedido);
	Pedido create(Pedido pedido);
	Pedido update(Pedido pedido);
	boolean deleteById(int idPedido);
	boolean existsByUsuarioId(int idUsuario);
	int count();
	
	// Métodos específicos de negocio para Pedido
	List<Pedido> findByUsuarioId(int idUsuario);
	List<Pedido> findByEstado(boolean estado);
	List<DetallePedido> findDetallesByPedidoId(int idPedido);
	boolean addDetallePedido(DetallePedido detalle);
	boolean updateEstadoPedido(int idPedido, boolean nuevoEstado);
	Double calcularTotalPedido(int idPedido);
	DetallePedido findDetalleByPedidoAndProducto(int idPedido, String idProducto);
	boolean updateCantidadDetalle(int idDetalle, int nuevaCantidad);

	
}
