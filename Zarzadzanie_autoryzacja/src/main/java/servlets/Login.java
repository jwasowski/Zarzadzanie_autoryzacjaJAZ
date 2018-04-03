package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/login")
public class Login extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String name = request.getParameter("Username");
		String password = request.getParameter("Password");
		
		if (name == null || name.equals("") || password == null || password.equals("") ) {
			response.sendRedirect("/Login.jsp");
		}
		//Sprawdza w bazie uzytkownika
		
		response.setContentType("text/html");
		response.getWriter().print("<meta http-equiv=" + "\"refresh\"" + "content="
				+ "\"3; url=/userprofile\">Zalogowales sie, za 3 sekundy zostaniesz przekierowany do podgladu swojego profilu");
	}
}
