package gpe.tp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gpe.tp.beans.User;
import gpe.tp.forms.ConnexionForm;

@SuppressWarnings("serial")
public class Connexion extends HttpServlet {
	public static final String PATH_VIEW_LOGIN = "/WEB-INF/login.jsp";
	// public static final String PATH_VIEW_ADD_CONTACT =
	// "/WEB-INF/addcontact.jsp";
	public static final String ATT_USER = "user";
	public static final String ATT_SESSION_USER = "sessionUser";
	public static final String ATT_FORM = "form";
	public static final String URL_REDIRECTION = "/Annuaire/add";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(PATH_VIEW_LOGIN).forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ConnexionForm form = new ConnexionForm();

		User user = form.connectUser(request);

		HttpSession session = request.getSession();

		if (form.getErrors().isEmpty()) {
			session.setAttribute(ATT_SESSION_USER, user);
		} else {
			session.setAttribute(ATT_SESSION_USER, null);
		}

		request.setAttribute(ATT_FORM, form);
		request.setAttribute(ATT_USER, user);

		if (form.getErrors().isEmpty()) {
			response.sendRedirect(URL_REDIRECTION);
		} else {
			this.getServletContext().getRequestDispatcher(PATH_VIEW_LOGIN).forward(request, response);
		}
	}
}
