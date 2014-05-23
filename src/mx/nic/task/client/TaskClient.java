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

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

// CXF JAX-WS Client / Consuming Web Services With CXF

public final class TaskClient {

	public static void main(String args[]) {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

		factory.setServiceClass(TaskSEI.class);
		factory.setAddress("http://localhost:8080/tasks-WS/services?wsdl");
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		TaskSEI client = (TaskSEI) factory.create();
		
		addSecurity(client);
		
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

	private static void addSecurity(TaskSEI proxyClient) {
		org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy.getClient(proxyClient);
		org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		// Specify our username
		outProps.put(WSHandlerConstants.USER, "jose");
		// Password type : plain text
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		// Callback used to retrieve password for given user.
		 outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordHandler.class.getName());
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);

	}
	
	public static class ClientPasswordHandler implements CallbackHandler {

		@Override
		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

			WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

			// set the password for our message.
			// we expect that all current users have this password as this eases
			// unittesting
			pc.setPassword("pass");
		}

	}
}