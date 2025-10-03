package model;

public class Usuario {
	private int idUsuario;
	private TipoUsuario tipo;
	private String nombre;
	private String email;
	private String contrasenia;
	private String telefono;

	public Usuario(int idUsuario, TipoUsuario tipo, String nombre, String email, String contrasenia, String telefono) {

		this.idUsuario = idUsuario;
		this.tipo = tipo;
		this.nombre = nombre;
		this.email = email;
		this.contrasenia = contrasenia;
		this.telefono = telefono;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
