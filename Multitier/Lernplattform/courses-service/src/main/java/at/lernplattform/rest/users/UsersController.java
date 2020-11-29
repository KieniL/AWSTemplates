package at.lernplattform.rest.users;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import at.lernplattform.application.users.LoginUserCommand;
import at.lernplattform.application.users.UserService;
import at.lernplattform.rest.api.UsersApi;
import at.lernplattform.rest.api.model.LoginModel;
import at.lernplattform.rest.api.model.TokenModel;

@RestController
public class UsersController implements UsersApi {

	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<TokenModel> login(@Valid @RequestBody LoginModel loginModel) {
		LoginUserCommand command = new LoginUserCommand(loginModel.getEmail(), loginModel.getPassword());
		String[] userCred = userService.login(command);
		TokenModel response = new TokenAdapter(userCred).createJson();
		return ResponseEntity.ok(response);
	}

}
