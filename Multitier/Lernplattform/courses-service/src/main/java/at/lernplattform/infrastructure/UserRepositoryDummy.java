package at.lernplattform.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import at.lernplattform.application.dao.CourseDaoImpl;
import at.lernplattform.application.dao.UserDaoImpl;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.user.User;
import at.lernplattform.domain.user.UserRepository;

@Service
public class UserRepositoryDummy implements UserRepository {

	private List<User> users = new ArrayList<>();

	// TODO nur temporär, wird dann später im service gemacht, nur um dummy data zu
	// initializieren
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Autowired
	@Qualifier("userDaoImpl")
	private UserDaoImpl userdao;
	/*
	@PostConstruct
	public void initDummyData() {
		users.add(new User("1", "lernplattform@gmail.com", passwordEncoder.encode("password")));
		users.add(new User("2", "testuser@gmail.com", passwordEncoder.encode("password")));
	}*/

	@Override
	public Optional<User> findUser(String email) {
	//public Optional<User> findUser(String email) {
		
		List<at.lernplattform.application.database.model.User> user2 = userdao.getUser(email);
		users = new ArrayList<>();
		
		for (at.lernplattform.application.database.model.User u : user2)
		{
			users.add(new User(u.getId(), u.getEmail(), u.getPassword()));
		}
		
		return users.stream().filter(item -> item.getEmail().equals(email)).findFirst();
	}

}
