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

import domain.User;
import mapper.ResultSetMapper;
import services.CommonMethods;

@WebServlet("/users")
public class Users extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String returnHtml = "";
		PreparedStatement selectUsers = null;
		List<User> result = null;
		ResultSet rs = null;
		Connection connection = null;
		Statement printUsers = null;
		try {
			selectUsers = selectUsersSetup(connection, CommonMethods.url(), printUsers, selectUsers, selectUsersSql());
			result = mapResultSet(rs, selectUsers);
			returnHtml = htmlResponseBuilder(returnHtml, result);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("dbError", dbError());
			request.getRequestDispatcher("/Manager.jsp").forward(request, response);
		} finally {
			try {
				closeResource(connection, printUsers, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			response.setContentType("text/html");
			request.setAttribute("returnHtml", returnHtml);
			request.getRequestDispatcher("/Manager.jsp").forward(request, response);
		}
	}

	private String isPremium(String a) {
		if (!a.equals(UserLevel.PREMIUM.toString()) && !a.equals(UserLevel.ADMIN.toString())) {
			return a = "No";
		}
		return a = "Yes";
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

	private PreparedStatement selectUsersSetup(Connection con, String url, Statement statement,
			PreparedStatement preState, String sql) throws SQLException {
		con = DriverManager.getConnection(url);
		statement = con.createStatement();
		preState = con.prepareStatement(sql);
		return preState;
	}

	private void closeResource(Connection con, Statement statement, ResultSet rs) throws SQLException {
		if (con != null)
			con.close();
		if (statement != null)
			statement.close();
		if (rs != null)
			rs.close();
	}

	private String selectUsersSql() {
		return "SELECT * FROM USERS";
	}

	private String dbError() {
		return "Blad serwera bazy danych - Blad listy uzytkownikow (skontaktuj sie z pomoca techniczna)";
	}

	private String htmlResponseBuilder(String responseFile, List<User> result) {
		responseFile += "<html><head><style>table, th, td {border: 1px solid black;}</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"><title>List of Users</title></head><body>"
				+ "<table><tr><th>Username</th><th>Premium</th></tr>";
		for (int i = 0; i < result.size(); i++) {
			int szer = 2;
			responseFile += "<tr>";
			for (int j = 0; j < szer; j++) {
				switch (j) {
				case 0:
					responseFile += "<td>" + result.get(i).getUsername() + "</td>";
					break;
				case 1:
					responseFile += "<td>" + isPremium(result.get(i).getLevel()) + "</td>";
					break;
				}
			}
			responseFile += "</tr>";
		}
		responseFile += "</table></body></html>";
		return responseFile;
	}
}
