package at.lernplattform.domain.usecase;

import java.util.List;

import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.course.Task;

public class GetTasks {

	private CourseRepository courseRepository;
	private String courseId;

	public GetTasks(CourseRepository courseRepository, String courseId) {
		this.courseRepository = courseRepository;
		this.courseId = courseId;
	}

	public List<Task> getTasks() {
		Course course = courseRepository.findCourse(courseId);
		return course.getTasks();
	}

}
