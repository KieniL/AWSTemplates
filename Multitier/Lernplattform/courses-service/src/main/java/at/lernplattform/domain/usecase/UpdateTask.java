package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.tasks.UpdateTaskCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.course.Task;

public class UpdateTask {

	private CourseRepository courseRepository;
	private UpdateTaskCommand command;

	public UpdateTask(CourseRepository courseRepository, UpdateTaskCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public Task updateTask() {
		Course course = courseRepository.findCourse(command.getCourseId());
		if (!course.getCreator().equals(command.getUserId())) {
			throw new CourseAccessProhibitedException();
		}
		Task updatedTask = course.updateTask(command);
		courseRepository.updateTask(updatedTask, course.getId());
		//course.addTask(task);
		//courseRepository.save(course);
		return updatedTask;
	}

}
