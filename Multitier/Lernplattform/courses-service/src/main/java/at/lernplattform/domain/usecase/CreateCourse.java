package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CreateCourseCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseFactory;
import at.lernplattform.domain.course.CourseRepository;

public class CreateCourse {

	private CourseRepository courseRepository;
	private CreateCourseCommand command;

	public CreateCourse(CourseRepository courseRepository, CreateCourseCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public Course createCourse() {
		Course course = CourseFactory.createNew(command);
		return courseRepository.save(course);
	}

}
