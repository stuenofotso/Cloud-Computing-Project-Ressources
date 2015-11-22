package gpe.tp.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import gpe.tp.beans.Contact;
import gpe.tp.forms.EnregistrementForm;
import gpe.tp.forms.VisualisationForm;

@SuppressWarnings("serial")
public class Enregistrement extends HttpServlet {
	private static final String PATH_VIEW = "/WEB-INF/addcontact.jsp";
	public static final String ATT_FORM = "form";
	public static final String ATT_CONTACT = "contact";
	public static final String ATT_CONTACTS = "contacts";
	public static final String ACCES_PUBLIC = "/login.jsp";
	public static final String ATT_SESSION_USER = "sessionUser";

	//private static String PATH_VIEW_LOGIN = "/WEB-INF/index.jsp";

	//@SuppressWarnings("unused")
	public void doGet( HttpServletRequest request, HttpServletResponse
			response ) throws ServletException, IOException {
		//HttpSession session = request.getSession();

//		if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
//			/* Redirection vers la page publique */
//			this.getServletContext().getRequestDispatcher( PATH_VIEW_LOGIN ).forward( request, response );
//		} else {
			VisualisationForm vf = new VisualisationForm();

			ArrayList<Map<String, String>> contacts =  vf.getContact();

			request.setAttribute( ATT_CONTACTS, contacts );

			this.getServletContext().getRequestDispatcher( PATH_VIEW ).forward( request, response );
//		}
	}

	public void doPost( HttpServletRequest request, HttpServletResponse
			response ) throws ServletException, IOException {

		EnregistrementForm form = new EnregistrementForm();

		Contact contact = form.addContact(request);

		VisualisationForm vf = new VisualisationForm();

		ArrayList<Map<String, String>> contacts =  vf.getContact();

		request.setAttribute( ATT_FORM, form );
		request.setAttribute( ATT_CONTACT, contact );
		request.setAttribute( ATT_CONTACTS, contacts );
		
		this.getServletContext().getRequestDispatcher( PATH_VIEW ).forward( request, response );
	}
}
