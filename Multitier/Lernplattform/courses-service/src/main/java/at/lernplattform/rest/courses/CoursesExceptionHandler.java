package at.lernplattform.rest.courses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.courses.CourseNotFoundException;
import at.lernplattform.application.tasks.TaskNotFoundException;
import at.lernplattform.rest.api.model.ErrorModel;

@ControllerAdvice
public class CoursesExceptionHandler {

	@ExceptionHandler({ CourseNotFoundException.class })
	public final ResponseEntity<ErrorModel> handleException(CourseNotFoundException exception) {
		return new ResponseEntity<>(new ErrorModel().message("Course " + exception.getId() + " not found"),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ TaskNotFoundException.class })
	public final ResponseEntity<ErrorModel> handleException(TaskNotFoundException exception) {
		return new ResponseEntity<>(new ErrorModel().message("Task " + exception.getId() + " not found"),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ CourseAccessProhibitedException.class })
	public final ResponseEntity<ErrorModel> handleException(CourseAccessProhibitedException exception) {
		return new ResponseEntity<>(new ErrorModel().message("You are not allowed to modify this course"),
				HttpStatus.FORBIDDEN);
	}

}
