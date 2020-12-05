package at.lernplattform.domain.course;

import at.lernplattform.application.tasks.UpdateTaskCommand;

public class Task {

	private String id;
	private String name;
	private String description;
	private String code;

	private String previousTask;
	private String nextTask;

	public Task(String id, String name, String description, String code) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.code = code;
	}

	public Task(String id, String name, String description, String previousTask, String nextTask, String code) {
		this(id, name, description, code);
		this.previousTask = previousTask;
		this.nextTask = nextTask;
	}

	public void update(UpdateTaskCommand command) {
		name = command.getName();
		description = command.getDescription();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPreviousTask() {
		return previousTask;
	}

	public String getNextTask() {
		return nextTask;
	}

	public void setPreviousTask(String previousTask) {
		this.previousTask = previousTask;
	}

	public void setNextTask(String nextTask) {
		this.nextTask = nextTask;
	}

	public String getCode() {
		return code;
	}

}
