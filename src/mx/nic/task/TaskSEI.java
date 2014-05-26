package mx.nic.task;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

import mx.nic.task.exception.OperationFailed;

/**
 * Interface que define los métodos del WS Tasks. 
 * @author mgonzalez
 *
 */
@WebService(name = "Tasks", targetNamespace="http://tasks.nic.mx/")
@WSDLDocumentationCollection(
    {
        @WSDLDocumentation("WS que permite administrar tareas"),
        @WSDLDocumentation(value = "WS que permite administrar tareas",
                           placement = WSDLDocumentation.Placement.TOP),
        @WSDLDocumentation(value = "Documentación de Data Binding",
                           placement = WSDLDocumentation.Placement.BINDING)
    }
)

public interface TaskSEI {

	@WebMethod(operationName = "addTask")
	@WSDLDocumentation("Añadir tarea")
	public void addTask(@WebParam(name = "task")Task task) throws OperationFailed;
	
	@WebMethod(operationName = "getTask")
	public Task getTask(@WebParam(name = "id")int id);
	
	@WebMethod
	@WSDLDocumentation("Borrar Tareas")
	public Task deleteTask(@WebParam(name = "id")int id);
	
	@WebMethod
	public List<Task> getTasks();

}
