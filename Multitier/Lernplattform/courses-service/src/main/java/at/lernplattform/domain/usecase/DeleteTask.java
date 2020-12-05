package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.tasks.DeleteTaskCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;

public class DeleteTask {

	private CourseRepository courseRepository;
	private DeleteTaskCommand command;

	public DeleteTask(CourseRepository courseRepository, DeleteTaskCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public void deleteTask() {
		Course course = courseRepository.findCourse(command.getCourseId());
		if (!course.getCreator().endsWith(command.getUserId())) {
			throw new CourseAccessProhibitedException();
		}
		courseRepository.removeTask(command.getTaskId());
		//course.addTask(task);
		//courseRepository.save(course);
		//course.removeTask(command.getTaskId());
		//courseRepository.save(course);
	}

}
