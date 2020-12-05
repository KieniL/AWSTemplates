package at.lernplattform.application.dao;

import java.util.List;
import java.util.Optional;

import at.lernplattform.application.database.model.Course;




public interface CourseDao{
	List<Course> getCourses(Optional<String> userId, boolean deleted);
	
	Course createCourse(String name, boolean deleted, int courseowner, int schema);

	Course getCourse(String courseId);

	Course updateCourse(int courseId, String name);
	
	void restoreCourse(int courseId);
	
	void deleteCourse(int courseId);
}
