package model;

import java.sql.Date;

public class Pedido {
	private int idPedido;
	private Date fecha;
	private Usuario usuario;
	private Boolean estado;
	private String direccionEntrega;
	private Double total;

	public Pedido(int idPedido, Date fecha, Usuario usuario, Boolean estado, String direccionEntrega, Double total) {

		this.idPedido = idPedido;
		this.fecha = fecha;
		this.usuario = usuario;
		this.estado = estado;
		this.direccionEntrega = direccionEntrega;
		this.total = total;
	}

	public Pedido() {
		// Constructor vacío
	}

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
