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

import domain.RegistrationObject;
import domain.User;
import mapper.ResultSetMapper;

@WebServlet("/register")
public class Register extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PreparedStatement selectCheckUser;
	private PreparedStatement insert;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String name = request.getParameter("Username");
		String password = request.getParameter("Password");
		String conpassword = request.getParameter("ConPassword");
		String email = request.getParameter("Email");
		RegistrationObject register;
		String url = "jdbc:hsqldb:hsql://localhost/workdb";
		ResultSetMapper mapper = new ResultSetMapper();
		String selectCheckUserSql = "SELECT * FROM USERS WHERE USERNAME=? OR EMAIL=?";
		String insertSql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, LEVEL) VALUES (?,?,?,?)";
		String regError = "Login lub mail zajety";
		String regComplete = "Zarejestrowales sie pomyslnie.";
		String dbError = "Blad serwera bazy danych (skontaktuj sie z pomoca techniczna)";

		if (name == null || name.equals("") || password == null || password.equals("") || conpassword == null
				|| conpassword.equals("") || email == null || email.equals("") || !conpassword.equals(password)) {
			response.sendRedirect("/Register.jsp");
		}
		register = createRegisterObject(name, password, email);
		// Do bazy danych wyslac
		try {
			Connection connection = DriverManager.getConnection(url);
			Statement checkUser = connection.createStatement();
			selectCheckUser = connection.prepareStatement(selectCheckUserSql);
			selectCheckUser.setString(1, name);
			selectCheckUser.setString(2, email);

			List<User> result = new ArrayList<User>();
			ResultSet rs = selectCheckUser.executeQuery();
			while (rs.next()) {
				result.add(mapper.map(rs));
			}
			
			checkUser.close();
			
			//connection.close();
			response.setContentType("text/html");
			if (result.isEmpty()) {
				// wpisac do bazy
				//connection = DriverManager.getConnection(url);
				Statement insertIntoDb = connection.createStatement();
				insert = connection.prepareStatement(insertSql);
				insert.setString(1, register.getUsername());
				insert.setString(2, register.getPassword());
				insert.setString(3, register.getEmail());
				insert.setString(4, UserLevel.ZWYKLY.toString());
				insert.executeUpdate();
				connection.close();
				insertIntoDb.close();
				request.setAttribute("regComplete", regComplete);
				request.getRequestDispatcher("/Register.jsp").forward(request, response);
			} else {

				request.setAttribute("regError", regError);
				request.getRequestDispatcher("/Register.jsp").forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();

			request.setAttribute("dbError", dbError);
			request.getRequestDispatcher("/Register.jsp").forward(request, response);
		}

	}

	public RegistrationObject createRegisterObject(String u, String p, String e) {
		RegistrationObject r = new RegistrationObject();
		r.setUsername(u);
		r.setPassword(p);
		r.setEmail(e);
		return r;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect("/Register.jsp");

	}
}
