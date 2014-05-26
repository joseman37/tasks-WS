package mx.nic.task;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import mx.nic.task.exception.OperationFailed;

/**
 * Clase que implementa el WS Tasks.
 * @author mgonzalez
 *
 */
@WebService(endpointInterface = "mx.nic.task.TaskSEI", portName = "TasksPort", serviceName = "TasksService")
public class TaskServer implements TaskSEI {
	
	
	
	@Override
	public void addTask(Task task) throws OperationFailed {
		if(!TasksModel.getTasks().containsKey(task.getId())) {
			TasksModel.getTasks().put(task.getId(), task);
		} else {
			throw new OperationFailed("Task already exists");
		}
	}

	@Override
	public Task getTask(int id) {
		return TasksModel.getTasks().get(id);
	}

	@Override
	public Task deleteTask(int id) {
		return TasksModel.getTasks().remove(id);
		
	}

	@Override
	public List<Task> getTasks() {
		return  new ArrayList<Task>(TasksModel.getTasks().values());
	}

}
