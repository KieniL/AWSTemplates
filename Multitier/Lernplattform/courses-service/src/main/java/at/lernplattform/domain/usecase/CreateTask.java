package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.tasks.CreateTaskCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.course.Task;
import at.lernplattform.domain.course.TaskFactory;

public class CreateTask {

	private CourseRepository courseRepository;
	private CreateTaskCommand command;

	public CreateTask(CourseRepository courseRepository, CreateTaskCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public Task createTask() {
		Course course = courseRepository.findCourse(command.getCourseId());
		if(!course.getCreator().equals(command.getUserId())) {
			throw new CourseAccessProhibitedException();
		}
		Task task = TaskFactory.create(command);
		courseRepository.createTask(task, course.getId());
		//course.addTask(task);
		//courseRepository.save(course);
		return task;
	}

}
