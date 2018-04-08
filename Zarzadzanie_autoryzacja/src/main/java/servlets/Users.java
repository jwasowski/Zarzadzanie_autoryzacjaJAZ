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

@WebServlet("/users")
public class Users extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PreparedStatement selectUsers;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String url = "jdbc:hsqldb:hsql://localhost/workdb";
		String selectUsersSql = "SELECT * FROM USERS";
		ResultSetMapper mapper = new ResultSetMapper();
		String returnHtml = "";

		try {
			Connection connection = DriverManager.getConnection(url);
			Statement printUsers = connection.createStatement();
			selectUsers = connection.prepareStatement(selectUsersSql);

			List<User> result = new ArrayList<User>();
			ResultSet rs = selectUsers.executeQuery();
			while (rs.next()) {
				result.add(mapper.map(rs));
			}
			printUsers.close();
			connection.close();
			returnHtml += "<html><head><style>table, th, td {border: 1px solid black;}</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"><title>List of Users</title></head><body>"
					/*+ "<form method=\"Post\"><input type=\"submit\" name=\"back\" value=\"Go Back\" formaction=\"Manager.jsp\" /></form>"*/
					+ "<table><tr><th>Username</th><th>Premium</th></tr>";
			for (int i = 0; i < result.size(); i++) {
				int szer = 2;
				returnHtml += "<tr>";
				for (int j = 0; j < 2; j++) {
					switch (j) {
					case 0:
						returnHtml += "<td>" + result.get(i).getUsername() + "</td>";
						break;
					case 1:
						returnHtml += "<td>" + isPremium(result.get(i).getLevel()) + "</td>";
						break;

					}

				}
				returnHtml += "</tr>";
			}
			returnHtml += "</table></body></html>";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html");
		request.setAttribute("returnHtml", returnHtml); 
	    request.getRequestDispatcher("/Manager.jsp").forward(request, response);
	}
	private String isPremium(String a){
		if(!a.equals(UserLevel.PREMIUM.toString()) && !a.equals(UserLevel.ADMIN.toString())){
			return a="No";
		}
		return a="Yes";
	}
	
}
