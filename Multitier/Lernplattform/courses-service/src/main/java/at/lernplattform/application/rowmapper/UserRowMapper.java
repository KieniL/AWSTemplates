package at.lernplattform.application.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import at.lernplattform.application.database.model.User;



public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User(
				rs.getString("id"),
				rs.getString("email"),
				rs.getString("password"));

		return user;
	}

}
