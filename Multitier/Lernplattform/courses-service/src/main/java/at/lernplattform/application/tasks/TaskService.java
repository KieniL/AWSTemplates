package at.lernplattform.application.tasks;

import java.util.List;

import at.lernplattform.domain.course.Feedback;
import at.lernplattform.domain.course.Task;

public interface TaskService {

	Task getTask(String courseid, String taskid);

	List<Task> getTasks(String courseid);

	Task createTask(CreateTaskCommand command);

	void deleteTask(DeleteTaskCommand command);
	

	Task updateTask(UpdateTaskCommand command);

	Feedback getFeedback(CreateFeedbackCommand command);


}
