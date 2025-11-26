package exception;

/**
 * Excepción personalizada para errores de lógica de negocio.
 * Se lanza cuando se violan reglas de negocio (validaciones, datos inválidos, etc.)
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor sin argumentos
	 */
	public BusinessException() {
		super();
	}

	/**
	 * Constructor con mensaje de error
	 * @param message El mensaje de error que se mostrará al usuario
	 */
	public BusinessException(String message) {
		super(message);
	}

	/**
	 * Constructor con mensaje y causa
	 * @param message El mensaje de error
	 * @param cause La excepción que causó este error
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor con causa
	 * @param cause La excepción que causó este error
	 */
	public BusinessException(Throwable cause) {
		super(cause);
	}
}
