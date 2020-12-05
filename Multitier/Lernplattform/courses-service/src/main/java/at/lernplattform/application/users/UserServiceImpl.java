package at.lernplattform.application.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import at.lernplattform.domain.usecase.LoginUser;
import at.lernplattform.domain.user.UserRepository;
import at.lernplattform.infrastructure.TokenService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public String[] login(LoginUserCommand command) {
		return new LoginUser(userRepository, command, tokenService, passwordEncoder).login();
	}

}
