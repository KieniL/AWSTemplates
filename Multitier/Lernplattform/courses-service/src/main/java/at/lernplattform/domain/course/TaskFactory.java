package at.lernplattform.domain.course;

import at.lernplattform.application.tasks.CreateTaskCommand;

public final class TaskFactory {

	private TaskFactory() {
	}

	public static Task create(CreateTaskCommand command) {
		return new Task(null, command.getName(), command.getDescription(), command.getCode());
	}
}
