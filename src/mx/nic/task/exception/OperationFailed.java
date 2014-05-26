package mx.nic.task.exception;

/**
 * Excepción que es lanzada cuando ocurre un Error en el WS.
 * 
 * @author mgonzalez
 *
 */
public class OperationFailed extends Exception {

	/**
	 * Ayuda a serialización.
	 */
	private static final long serialVersionUID = 1L;
	
	public OperationFailed(String message) {
		super(message);
	}
	
	public OperationFailed(String message, Throwable t) {
		super(message, t);
	}

}
