package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.LoginObject;
import domain.User;
import mapper.ResultSetMapper;

@WebServlet("/login")
public class Login extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PreparedStatement selectAll;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String name = request.getParameter("Username");
		String password = request.getParameter("Password");
		String selectCheckUser = "SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?";
		ResultSetMapper mapper = new ResultSetMapper();
		if (name == null || name.equals("") || password == null || password.equals("")) {
			response.sendRedirect("/Login.jsp");
		}
		LoginObject userLoggingIn = new LoginObject();
		userLoggingIn.setUsername(name);
		userLoggingIn.setPassword(password);
		// Sprawdza w bazie uzytkownika
		String url = "jdbc:hsqldb:hsql://localhost/workdb";

		try {
			Connection connection = DriverManager.getConnection(url);
			Statement checkUser = connection.createStatement();
			selectAll = connection.prepareStatement(selectCheckUser);
			selectAll.setString(1, name);
			selectAll.setString(2, password);

			List<User> result = new ArrayList<User>();
			ResultSet rs = selectAll.executeQuery();
			while (rs.next()) {
				result.add(mapper.map(rs));
			}
			checkUser.close();
			connection.close();
			response.setContentType("text/html");
			if (!result.isEmpty()) {
				response.getWriter().print("<meta http-equiv=" + "\"refresh\"" + "content="
						+ "\"3; url=/userprofile\">Zalogowales sie, za 3 sekundy zostaniesz przekierowany do podgladu swojego profilu.");
			} else {
				response.getWriter().print("<meta http-equiv=" + "\"refresh\"" + "content="
						+ "\"3; url=/Login.jsp\">Niepoprawny login albo haslo, za 3 sekundy zostaniesz przekierowany do ekranu logowania.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().print("<meta http-equiv=" + "\"refresh\"" + "content="
					+ "\"3; url=/Login.jsp\">Niepoprawny login albo haslo, za 3 sekundy zostaniesz przekierowany do ekranu logowania.");
		}

	}
}
