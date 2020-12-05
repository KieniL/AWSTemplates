package at.lernplattform.rest.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import at.lernplattform.application.courses.CourseNotFoundException;
import at.lernplattform.application.tasks.TaskNotFoundException;
import at.lernplattform.application.users.WrongCredentialsException;
import at.lernplattform.rest.api.model.ErrorModel;

@ControllerAdvice
public class UsersExceptionHandler {

	@ExceptionHandler({ WrongCredentialsException.class })
	public final ResponseEntity<ErrorModel> handleException(WrongCredentialsException exception) {
		return new ResponseEntity<>(new ErrorModel().message("Wrong login credentials"), HttpStatus.FORBIDDEN);
	}

}
