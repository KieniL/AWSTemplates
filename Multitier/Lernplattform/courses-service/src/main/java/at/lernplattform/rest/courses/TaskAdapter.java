package at.lernplattform.rest.courses;

import at.lernplattform.domain.course.Task;
import at.lernplattform.rest.api.model.TaskModel;

public class TaskAdapter {

	private Task task;

	public TaskAdapter(Task task) {
		this.task = task;
	}

	public TaskModel createJson() {
		return new TaskModel().id(task.getId()).name(task.getName()).description(task.getDescription())
				.previousTask(task.getPreviousTask()).nextTask(task.getNextTask()).code(task.getCode());
	}

}
