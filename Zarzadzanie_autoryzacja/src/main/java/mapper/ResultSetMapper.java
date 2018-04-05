package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import domain.User;

public class ResultSetMapper  {
	public User map(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setUsername(rs.getString("username"));
		u.setPassword(rs.getString("password"));
		u.setLevel(rs.getString("level"));
		return u;
	}
}
