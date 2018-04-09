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

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PreparedStatement updateUsers;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String username = request.getParameter("Username");
		
		String level = request.getParameter("level");
		String url = "jdbc:hsqldb:hsql://localhost/workdb";
		String updateSql = "UPDATE USERS SET LEVEL=? WHERE USERNAME=?";

		String dbError = "Blad serwera bazy danych - Blad managera (skontaktuj sie z pomoca techniczna)";
		String managerSuccess = "Dokonano zmiany uprawnien uzytkownika.";

		if (username == null || username.equals("")) {
			response.sendRedirect("/Manager.jsp");
		}
		try {
			Connection connection = DriverManager.getConnection(url);
			Statement checkUser = connection.createStatement();
			updateUsers = connection.prepareStatement(updateSql);
			updateUsers.setString(1, level);
			updateUsers.setString(2, username);

			updateUsers.executeUpdate();
			checkUser.close();
			connection.close();
			response.setContentType("text/html");

			request.setAttribute("managerSuccess", managerSuccess);
			request.getRequestDispatcher("/Manager.jsp").forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("dbError", dbError);
			request.getRequestDispatcher("/Manager.jsp").forward(request, response);
		}

	}
}
