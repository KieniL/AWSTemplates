package at.lernplattform.application.database.model;

import java.util.List;

public class Course {

	private String id;
	private String name;
	private String creator;
	private Boolean deleted;
	private List<Task> tasks;
	private int schema;
	
	public Course(String name, String creator, Boolean deleted, List<Task> tasks, int schema) {
		this.name = name;
		this.creator = creator;
		this.deleted = deleted;
		this.setTasks(tasks);
		this.schema = schema;
	}
	
	public Course(String id, String name, String creator, Boolean deleted, int schema) {
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.deleted = deleted;
		this.schema = schema;
	}
	
	public Course(String id, String name, String creator, Boolean deleted, List<Task> tasks, int schema) {
		this(name, creator, deleted, tasks, schema);
		this.id = id;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}


	public List<Task> getTasks() {
		return tasks;
	}


	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public int getSchema() {
		return schema;
	}

	public void setSchema(int schema) {
		this.schema = schema;
	}
	
	
}
