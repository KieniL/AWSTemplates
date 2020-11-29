package at.lernplattform.application.courses;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.usecase.CreateCourse;
import at.lernplattform.domain.usecase.DeleteCourse;
import at.lernplattform.domain.usecase.GetCourse;
import at.lernplattform.domain.usecase.GetCourses;
import at.lernplattform.domain.usecase.RestoreCourse;
import at.lernplattform.domain.usecase.UpdateCourse;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Override
	public List<Course> getCourses(Optional<String> userId, boolean deleted) {
		return new GetCourses(courseRepository).getCourses(userId, deleted);
	}

	@Override
	public Course createCourse(CreateCourseCommand command) {
		return new CreateCourse(courseRepository, command).createCourse();
	}

	@Override
	public Course getCourse(String courseId) {
		return new GetCourse(courseRepository, courseId).getCourse();
	}

	@Override
	public Course updateCourse(UpdateCourseCommand command) {
		return new UpdateCourse(courseRepository, command).updateCourse();
	}

	@Override
	public void deleteCourse(DeleteCourseCommand command) {
		new DeleteCourse(courseRepository, command).deleteCourse();
	}

	@Override
	public Course restoreCourse(RestoreCourseCommand command) {
		return new RestoreCourse(courseRepository, command).restoreCourse();
	}

}
