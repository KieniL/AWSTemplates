package at.lernplattform.domain.course;

import java.util.List;

import at.lernplattform.application.courses.RestoreCourseCommand;
import at.lernplattform.application.courses.UpdateCourseCommand;
import at.lernplattform.application.tasks.TaskNotFoundException;
import at.lernplattform.application.tasks.UpdateTaskCommand;

public class Course {

	private String id;
	private String name;
	private String creator;
	private Boolean deleted;
	private int schema;

	private List<Task> tasks;

	public Course(String id, String name, String creator, Boolean deleted, List<Task> tasks, int schema) {
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.tasks = tasks;
		this.deleted = deleted;
		this.schema = schema;
	}

	public void update(UpdateCourseCommand command) {
		this.name = command.getName();
	}
	
	public void restore(RestoreCourseCommand command) {
		this.deleted = false;
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public Task getTask(String taskId) {
		return tasks.stream().filter(item -> item.getId().equals(taskId)).findFirst()
				.orElseThrow(() -> new TaskNotFoundException(taskId));
	}

	public void removeTask(String taskId) {
		tasks.removeIf(item -> item.getId().equals(taskId));
	}

	public Task updateTask(UpdateTaskCommand command) {
		Task task = getTask(command.getTaskId());
		task.update(command);
		return task;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCreator() {
		return creator;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	// nur testweise für in memory, später löschen
	public void setId(String id) {
		this.id = id;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getSchema() {
		return schema;
	}

	public void setSchema(int schema) {
		this.schema = schema;
	}



}
