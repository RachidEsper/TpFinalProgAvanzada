package model;

public class TipoUsuario {
	private int idTipoUsuario;
	private String descripcion;

	public TipoUsuario(int idTipoUsuario, String descripcion) {

		this.idTipoUsuario = idTipoUsuario;
		this.descripcion = descripcion;
	}

	public TipoUsuario() {
		// Constructor vacío
	}

	public int getIdTipoUsuario() {
		return idTipoUsuario;
	}

	public void setIdTipoUsuario(int idTipoUsuario) {
		this.idTipoUsuario = idTipoUsuario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
