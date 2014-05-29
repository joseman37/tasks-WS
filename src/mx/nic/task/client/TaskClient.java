package mx.nic.task.client;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import mx.nic.task.Task;
import mx.nic.task.TaskSEI;
import mx.nic.task.exception.OperationFailed;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

// 
/**
 * Ejemplo de un cliente de un servicio CXF JAX-WS. Consumo de servicios web con CXF
 * 
 * @author mgonzalez
 *
 */
public final class TaskClient {
	
	private static final String address = "http://localhost:8080/tasks-WS/services?wsdl";
	private static final String secureAddress = "https://localhost:8443/tasks-WS/services?wsdl";
	
	private static final boolean secureConnection = true;

	public static void main(String args[]) {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

		factory.setServiceClass(TaskSEI.class);
		factory.setAddress(secureConnection ? secureAddress : address);
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		TaskSEI client = (TaskSEI) factory.create();
		
		if(secureConnection) {
			addSecurity(client);
		}
		
		Task task = new Task(8, "Task 8", "Task", new Date());
		try {
			client.addTask(task);
			System.out.println("Task added: " + task);
		} catch (OperationFailed e) {
			System.out.println("Task could not be added: " + e.getMessage());
		}

		task = client.getTask(1);
		System.out.println("Get Task: " + task);

		task = client.deleteTask(1);
		System.out.println("deleted Task: " + task);

		List<Task> tasks = client.getTasks();

		System.out.println("All tasks:");
		for (Task t : tasks) {
			System.out.println(t);
		}

		System.exit(0);
	}
	
	/**
	 * Añade WS-Security de tipo usernameToken al cliente. También añade el
	 * conducto TLS.
	 * 
	 * @param proxyClient
	 */
	private static void addSecurity(TaskSEI proxyClient) {
		Client client = ClientProxy.getClient(proxyClient);

		HTTPConduit http = (HTTPConduit) client.getConduit();
		http.setTlsClientParameters(Utils.getTlsParams());

		Endpoint cxfEndpoint = client.getEndpoint();
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		// Se especifica el nombre de usuario
		outProps.put(WSHandlerConstants.USER, "jose");
		// Tipo de password : plain text
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		// Callback usado para obtener el password de usuario.
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordHandler.class.getName());
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);

	}
	
	/**
	 * Clase que obtiene el password de los usuarios.
	 * 
	 * @author Jose
	 * 
	 */
	public static class ClientPasswordHandler implements CallbackHandler {

		@Override
		public void handle(Callback[] callbacks) throws IOException,
				UnsupportedCallbackException {

			WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

			// Se espera que todos los usuarios actuales tengan esta contraseña
			pc.setPassword("pass");
		}

	}
}