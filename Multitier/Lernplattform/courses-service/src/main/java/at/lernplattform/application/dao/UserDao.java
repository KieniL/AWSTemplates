package at.lernplattform.application.dao;


import java.util.List;

import at.lernplattform.application.database.model.User;


public interface UserDao{
	List<User> getUser(String email);
}
