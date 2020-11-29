package at.lernplattform.application.dao;

import java.util.List;
import java.util.Optional;

import at.lernplattform.application.database.model.Task;


public interface TaskDao{
	
	Task getTask(String courseid, String taskid);

	List<Task> getTasks(String courseid, Boolean deleted);

	Task createTask(String name, String description, String code, String courseid);

	void deleteTask(String id);

	Task updateTask(String id, String name, String description);
}
