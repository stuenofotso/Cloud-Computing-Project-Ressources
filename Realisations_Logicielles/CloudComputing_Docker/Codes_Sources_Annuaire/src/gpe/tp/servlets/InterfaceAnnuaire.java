package gpe.tp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import gpe.tp.forms.BusinessAnnuaire;
import gpe.tp.interfaces.crud;

@SuppressWarnings("serial")
public class InterfaceAnnuaire extends HttpServlet {
	public static final String CHAMP_UPDATE = "updatecontact";
	public static final String CHAMP_DELETE = "deletecontact";
	public static final String CHAMP_DATAS = "datas";

	public void doGet( HttpServletRequest request, HttpServletResponse
			response ) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher( "/hello.html" ).forward( request, response );
	}

	public void doPost( HttpServletRequest request, HttpServletResponse
			response ) throws ServletException, IOException {
		//HttpSession session = request.getSession();

		JSONObject jso = new JSONObject(), jsrep = new JSONObject();;

		String updatecontact = request.getParameter( CHAMP_UPDATE );
		String deletecontact = request.getParameter( CHAMP_DELETE );
		String datas = request.getParameter( CHAMP_DATAS );

		if(updatecontact != null && datas != null) {
			try {
				jso = new JSONObject(datas);
				int contactid = Integer.parseInt( (String) jso.get("contactid") );
				String name = (String) jso.get("name");
				String phone = (String) jso.get("phone");

				if ( ( name != null && name.length() < 3 ) || !( phone.matches("6 [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}") ) ) {
					jsrep.put("type", -1);
					jsrep.put("message", "Modification failed by "+InetAddress.getLocalHost().getHostName()+" - "+
					InetAddress.getLocalHost().getHostAddress()+" ! name short or format phone wrong.");
				} else {
					crud crud = new BusinessAnnuaire();
					int status = crud.update(contactid, name, phone);

					jsrep.put("type", status);
					if(status != -1) {
						jsrep.put("message", "Modification done by "+InetAddress.getLocalHost().getHostName()+" - "+InetAddress.getLocalHost().getHostAddress()+" !");
					} else {
						jsrep.put("message", "Modification failed by "+InetAddress.getLocalHost().getHostName()+" - "+InetAddress.getLocalHost().getHostAddress()+" !");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if(deletecontact != null && datas != null) {
			try {
				jso = new JSONObject(datas);
				int contactid = Integer.parseInt( (String) jso.get("contactid") );

				crud crud = new BusinessAnnuaire();
				int status = crud.delete(contactid);

				jsrep.put("type", status);
				if(status != -1) {
					jsrep.put("message", "Suppression done by "+InetAddress.getLocalHost().getHostName()+" - "+InetAddress.getLocalHost().getHostAddress()+" !");
				} else {
					jsrep.put("message", "Suppression failed by "+InetAddress.getLocalHost().getHostName()+" - "+InetAddress.getLocalHost().getHostAddress()+" !");					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				jsrep.put("type", -1);
				jsrep.put("message", "Security is there !");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		response.setHeader("Content-Type", "application/json");
		PrintWriter out = response.getWriter();  
		out.print(jsrep);out.flush();
	}
}
