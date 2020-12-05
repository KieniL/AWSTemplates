package at.lernplattform.rest.courses;

import at.lernplattform.domain.course.Course;
import at.lernplattform.rest.api.model.CourseModel;
import at.lernplattform.rest.api.model.CourseOverviewModel;

class CourseOverviewAdapter {

	private Course course;

	CourseOverviewAdapter(Course course) {
		this.course = course;
	}

	CourseOverviewModel createJson() {
		return new CourseOverviewModel().id(course.getId()).name(course.getName()).deleted(course.getDeleted()).schema(course.getSchema());
	}

}
