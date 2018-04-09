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
import javax.servlet.http.HttpSession;

import domain.LoginObject;
import domain.User;
import mapper.ResultSetMapper;
import services.CommonMethods;

@WebServlet("/login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		String name = request.getParameter("Username");
		String password = request.getParameter("Password");
		ResultSet rs = null;
		Connection connection = null;
		Statement checkUser = null;
		PreparedStatement selectAll = null;
		List<User> result = null;
		LoginObject userLoggingIn = addFormFieldData(name, password);
		checkFormFieldsForNulls(userLoggingIn, response);
		// Sprawdza w bazie czy uzytkownik istnieje
		try {
			selectAll = selectAllSetup(connection, CommonMethods.url(), checkUser, selectAll, sql(), userLoggingIn);
			result = mapResultSet(rs, selectAll);
			response.setContentType("text/html");
			if (!result.isEmpty()) {
				setSessionAttributes(session, userLoggingIn, result);
				response.getWriter().print(logInSuccess());
			} else {
				request.setAttribute("wrongLoginOrPassword", wrongLoginOrPassword());
				request.getRequestDispatcher("/Login.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("dbError", dbError());
			request.getRequestDispatcher("/Login.jsp").forward(request, response);
		} finally {
			try {
				closeResource(connection, checkUser, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect("/Login.jsp");

	}

	private LoginObject addFormFieldData(String b, String c) {
		LoginObject a = new LoginObject();
		a.setUsername(b);
		a.setPassword(c);
		return a;
	}

	private void closeResource(Connection con, Statement statement, ResultSet rs) throws SQLException {
		if (con != null)
			con.close();
		if (statement != null)
			statement.close();
		if (rs != null)
			rs.close();
	}

	private void setSessionAttributes(HttpSession a, LoginObject b, List<User> c) {
		int first = 0;
		a.setAttribute("user", b.getUsername());
		a.setAttribute("level", c.get(first).getLevel());
	}

	private PreparedStatement selectAllSetup(Connection con, String url, Statement statement,
			PreparedStatement preState, String sql, LoginObject loginObj) throws SQLException {
		con = DriverManager.getConnection(url);
		statement = con.createStatement();
		preState = con.prepareStatement(sql);
		preState.setString(1, loginObj.getUsername());
		preState.setString(2, loginObj.getPassword());
		return preState;
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

	private String sql() {
		return "SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?";
	}

	private String wrongLoginOrPassword() {
		return "Niepoprawny login albo haslo";
	}

	private String dbError() {
		return "Blad serwera bazy danych (skontaktuj sie z pomoca techniczna)";
	}

	private String logInSuccess() {
		return "<meta http-equiv=\"refresh\"content=\"3; url=/userprofile\"></form>"
				+ "Zalogowales sie, za 3 sekundy zostaniesz przekierowany do podgladu swojego profilu.";
	}

	private void checkFormFieldsForNulls(LoginObject uli, HttpServletResponse response) throws IOException {
		if (uli.getUsername() == null || uli.getUsername().equals("") || uli.getPassword() == null
				|| uli.getPassword().equals("")) {
			response.sendRedirect("/Login.jsp");
		}
	}
}
