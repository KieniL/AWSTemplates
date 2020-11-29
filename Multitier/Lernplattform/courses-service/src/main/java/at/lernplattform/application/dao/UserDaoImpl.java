package at.lernplattform.application.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import at.lernplattform.application.database.model.User;
import at.lernplattform.application.rowmapper.UserRowMapper;

@Repository
public class UserDaoImpl implements UserDao{

	@Autowired
    private JdbcTemplate template;

	@Override
	public List<User> getUser(String email) {
		final String sql = "select * from internal.user where email = '"+email+"'";
		
		return template.query(sql, new UserRowMapper());
	}
	
	
}
