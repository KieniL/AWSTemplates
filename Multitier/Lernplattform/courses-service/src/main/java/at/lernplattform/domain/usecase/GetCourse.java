package at.lernplattform.domain.usecase;

import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;

public class GetCourse {

	private CourseRepository courseRepository;
	private String courseId;

	public GetCourse(CourseRepository courseRepository, String courseId) {
		this.courseRepository = courseRepository;
		this.courseId = courseId;
	}

	public Course getCourse() {
		return courseRepository.findCourse(courseId);
	}

}
