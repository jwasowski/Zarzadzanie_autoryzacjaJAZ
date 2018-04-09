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
import services.CommonMethods;

@WebServlet("/register")
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String name = request.getParameter("Username");
		String password = request.getParameter("Password");
		String conpassword = request.getParameter("ConPassword");
		String email = request.getParameter("Email");
		RegistrationObject register;
		ResultSet rs = null;
		PreparedStatement selectCheckUserInDb = null, insert = null;
		Connection connection = null;
		Statement checkUser = null, insertIntoDb = null;
		List<User> result = null;
		register = createRegistrationObject(name, password, conpassword, email);
		checkFormFieldsForNulls(register, response);
		// Sprawdza w bazie danych czy nie powtarza sie login i email
		try {
			selectCheckUserInDb = selectCheckUserInDbSetup(connection, CommonMethods.url(), checkUser,
					selectCheckUserInDb, selectCheckUserSql(), register);
			result = mapResultSet(rs, selectCheckUserInDb);
			response.setContentType("text/html");
			// Zapisuje w bazie danych nowego uzytkownika
			if (result.isEmpty()) {
				insert = insertSetup(connection, CommonMethods.url(), insertIntoDb, insert, insertSql(), register);
				request.setAttribute("regComplete", regComplete());
				request.getRequestDispatcher("/Register.jsp").forward(request, response);
			} else {
				request.setAttribute("regError", regError());
				request.getRequestDispatcher("/Register.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("dbError", dbError());
			request.getRequestDispatcher("/Register.jsp").forward(request, response);
		} finally {
			try {
				closeResource(connection, checkUser, insertIntoDb, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public RegistrationObject createRegistrationObject(String usr, String pass, String conpass, String email) {
		RegistrationObject r = new RegistrationObject();
		r.setUsername(usr);
		r.setPassword(pass);
		r.setConPassword(conpass);
		r.setEmail(email);
		return r;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect("/Register.jsp");
	}

	private void checkFormFieldsForNulls(RegistrationObject reg, HttpServletResponse response) throws IOException {
		if (reg.getUsername() == null || reg.getUsername().equals("") || reg.getPassword() == null
				|| reg.getPassword().equals("") || reg.getConPassword() == null || reg.getConPassword().equals("")
				|| reg.getEmail() == null || reg.getEmail().equals("")
				|| !reg.getConPassword().equals(reg.getPassword())) {
			response.sendRedirect("/Register.jsp");
		}
	}

	private PreparedStatement selectCheckUserInDbSetup(Connection con, String url, Statement statement,
			PreparedStatement select, String sql, RegistrationObject register) throws SQLException {
		con = DriverManager.getConnection(url);
		statement = con.createStatement();
		select = con.prepareStatement(sql);
		select.setString(1, register.getUsername());
		select.setString(2, register.getEmail());
		return select;
	}

	private List<User> mapResultSet(ResultSet rs, PreparedStatement selectAll) throws SQLException {
		List<User> result = new ArrayList<User>();
		ResultSetMapper mapper = new ResultSetMapper();
		rs = selectAll.executeQuery();
		while (rs.next()) {
			result.add(mapper.map(rs));
		}
		return result;
	}

	private PreparedStatement insertSetup(Connection con, String url, Statement statement, PreparedStatement insert,
			String sql, RegistrationObject register) throws SQLException {
		con = DriverManager.getConnection(url);
		statement = con.createStatement();
		insert = con.prepareStatement(sql);
		insert.setString(1, register.getUsername());
		insert.setString(2, register.getPassword());
		insert.setString(3, register.getEmail());
		insert.setString(4, UserLevel.ZWYKLY.toString());
		insert.executeUpdate();
		return insert;
	}

	private void closeResource(Connection con, Statement statementA, Statement statementB, ResultSet rs)
			throws SQLException {
		if (con != null)
			con.close();
		if (statementA != null)
			statementA.close();
		if (statementB != null)
			statementB.close();
		if (rs != null)
			rs.close();
	}

	private String selectCheckUserSql() {
		return "SELECT * FROM USERS WHERE USERNAME=? OR EMAIL=?";
	}

	private String insertSql() {
		return "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, LEVEL) VALUES (?,?,?,?)";
	}

	private String regError() {
		return "Login lub mail zajety";
	}

	private String regComplete() {
		return "Zarejestrowales sie pomyslnie.";
	}

	private String dbError() {
		return "Blad serwera bazy danych (skontaktuj sie z pomoca techniczna)";
	}
}
