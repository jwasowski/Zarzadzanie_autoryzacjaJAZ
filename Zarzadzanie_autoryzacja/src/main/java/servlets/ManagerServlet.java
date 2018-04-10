package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.CommonMethods;

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String username = request.getParameter("Username");
		String level = request.getParameter("level");
		PreparedStatement updateUsers = null;
		Connection connection = null;
		Statement checkUser = null;
		checkFormFieldsForNulls(username, response);
		try {
			updateUsers = updateUsersSetupAndExec(connection, CommonMethods.url(), checkUser, updateUsers, updateSql(), level,
					username);
			response.setContentType("text/html");
			request.setAttribute("managerSuccess", managerSuccess());
			request.getRequestDispatcher("/Manager.jsp").forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("dbError", dbError());
			request.getRequestDispatcher("/Manager.jsp").forward(request, response);
		}finally {
			try {
				closeResource(connection, checkUser);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private void checkFormFieldsForNulls(String username, HttpServletResponse response) throws IOException {
		if (username == null || username.equals("")) {
			response.sendRedirect("/Login.jsp");
		}
	}

	private PreparedStatement updateUsersSetupAndExec(Connection con, String url, Statement statement, PreparedStatement preState,
			String sql, String level, String username) throws SQLException {
		con = DriverManager.getConnection(url);
		statement = con.createStatement();
		preState = con.prepareStatement(sql);
		preState.setString(1, level);
		preState.setString(2, username);
		preState.executeUpdate();
		return preState;
	}

	private String updateSql() {
		return "UPDATE USERS SET LEVEL=? WHERE USERNAME=?";
	}

	private String dbError() {
		return "Blad serwera bazy danych - Blad managera (skontaktuj sie z pomoca techniczna)";
	}

	private String managerSuccess() {
		return "Dokonano zmiany uprawnien uzytkownika.";
	}

	private void closeResource(Connection con, Statement statement) throws SQLException {
		if (con != null)
			con.close();
		if (statement != null)
			statement.close();
	}
}
