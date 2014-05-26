package mx.nic.task.exception;

/**
 * Excepci�n que es lanzada cuando ocurre un Error en el WS.
 * 
 * @author mgonzalez
 *
 */
public class OperationFailed extends Exception {

	/**
	 * Ayuda a serializaci�n.
	 */
	private static final long serialVersionUID = 1L;
	
	public OperationFailed(String message) {
		super(message);
	}
	
	public OperationFailed(String message, Throwable t) {
		super(message, t);
	}

}
