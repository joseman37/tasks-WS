package mx.nic.task;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import mx.nic.task.exception.OperationFailed;

//@WebService(name = "Tasks", targetNamespace = "http://notes.nic.mx/")
@WebService(name = "Tasks")
public interface TaskSEI {

	@WebMethod(operationName = "addTask")
	public void addTask(@WebParam(name = "task")Task task) throws OperationFailed;
	
	@WebMethod(operationName = "getTask")
	public Task getTask(@WebParam(name = "id")int id);
	
	@WebMethod
	public Task deleteTask(@WebParam(name = "id")int id);
	
	@WebMethod
	public List<Task> getTasks();

}
