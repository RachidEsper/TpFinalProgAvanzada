package service.interfaces;

import java.util.List;

import model.Pedido;

public interface IPedidoService {
	// definir los metodos que van a tener cada implementacion, 
			//y despues en la implementacion hago la logica de negocio
			// engancha lo que llega con lo que tiene que salir y en el medio se mete logica adicional, modificaciones,validaciones, etc
	
	 /**
     * Crea un pedido nuevo en estado "abierto" para el usuario indicado.
     * Suele usarse cuando el usuario todavía no tiene carrito.
     */
    Pedido crearPedido(int idUsuario, String direccionEntrega);

    /**
     * Busca un pedido por su ID (sirve tanto para admin como para usuario).
     */
    Pedido findById(int idPedido);

    /**
     * Lista TODOS los pedidos del sistema.
     * Uso típico: panel del administrador.
     */
    List<Pedido> findAll();

    /**
     * Lista todos los pedidos de un usuario en particular
     * (historial de pedidos del usuario).
     */
    List<Pedido> findByUsuario(int idUsuario);

    /**
     * Devuelve el pedido "abierto" (carrito) del usuario,
     * o null si no tiene ninguno.
     * Regla de negocio: cada usuario puede tener a lo sumo 1 pedido abierto.
     */
    Pedido findPedidoAbiertoByUsuario(int idUsuario);

    /**
     * Marca el pedido como finalizado (por ejemplo, estado = true).
     * Después de esto, el usuario podría crear otro pedido/carrito nuevo.
     */
    boolean finalizarPedido(int idPedido);

    /**
     * Cancela el pedido (si tu modelo lo permite),
     * por ejemplo para revertir un carrito.
     */
    boolean cancelarPedido(int idPedido);

    /**
     * Elimina físicamente un pedido (uso más bien administrativo).
     */
    boolean eliminarPedido(int idPedido);
	
    boolean cambiarEstadoPedido(int idPedido, boolean nuevoEstado);

	
	
	
	
	
	
	
	
	
	

}
			/*
			 * ej finalizarpedido(){
			 * validar que los productos esten en stock 
			 * ver si hay productos en el pedido
			 * 
			 * }
			 */

