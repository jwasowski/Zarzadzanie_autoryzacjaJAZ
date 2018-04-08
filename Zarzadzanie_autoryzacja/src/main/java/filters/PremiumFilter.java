package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlets.UserLevel;

@WebFilter("/Premium.jsp")
public class PremiumFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession(true);

		System.out.print(session.getAttribute("level"));
		if (session.getAttribute("level") == null || (!session.getAttribute("level").equals(UserLevel.PREMIUM.toString())
				&& !session.getAttribute("level").equals(UserLevel.ADMIN.toString()))) {

			HttpServletResponse resp = (HttpServletResponse) response;
			/*resp.getWriter().print("<meta http-equiv=" + "\"refresh\"" + "content="
					+ "\"3; url=/userprofile\">Brak wykupionego pakietu PREMIUM, wykup PREMIUM, aby uzyskac dostep.");*/
			resp.sendRedirect("/userprofile");
			
		} 

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
