package mx.nic.task;

import java.util.Date;
import java.util.Hashtable;

public class TasksModel {
	
	private static Hashtable<Integer, Task> TASKS = generateTasks();

	
	public static Hashtable<Integer, Task> getTasks() {
		return TASKS;
	}
	
	
	private static Hashtable<Integer, Task> generateTasks() {
		Hashtable<Integer, Task> tasks = new Hashtable<Integer, Task>();
		tasks.put(1, new Task(1,"Task 1 description", "TASK 1", new Date()));
		tasks.put(2, new Task(2,"Task 2 description", "TASK 2", new Date()));
		tasks.put(3, new Task(3,"Task 3 description", "TASK 3", new Date()));
		tasks.put(4, new Task(4,"Task 4 description", "TASK 4", new Date()));
		tasks.put(5, new Task(5,"Task 5 description", "TASK 5", new Date()));
		tasks.put(6, new Task(6,"Task 6 description", "TASK 6", new Date()));
		return tasks;
	}
	
	
	

}
