package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.RegistrationObject;

@WebServlet("/register")
public class Register extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String name = request.getParameter("Username");
		String password = request.getParameter("Password");
		String conpassword = request.getParameter("ConPassword");
		String email = request.getParameter("Email");
		RegistrationObject register = new RegistrationObject();

		if (name == null || name.equals("") || password == null || password.equals("") || conpassword == null
				|| conpassword.equals("") || email == null || email.equals("") || !conpassword.equals(password)) {
			response.sendRedirect("/Register.jsp");
		}
		register = createRegisterObject(register, name, password, email);
		// Do bazy danych wyslac
		response.setContentType("text/html");
		response.getWriter().print("<meta http-equiv=" + "\"refresh\"" + "content="
				+ "\"3; url=/Login.jsp\">Zarejestrowales sie, za 3 sekundy przekieruje Cie automatycznie do strony logowania.");
	}

	public RegistrationObject createRegisterObject(RegistrationObject r, String u, String p, String e) {
		r.setUsername(u);
		r.setPassword(p);
		r.setEmail(e);
		return r;
	}
}
