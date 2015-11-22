package gpe.tp.forms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gpe.tp.db.GestionDB;

public class VisualisationForm {
	private GestionDB gdb = new GestionDB();
	private Connection con = null;
	private ArrayList<Map<String, String>> contacts = new ArrayList<>();

	public Connection getCon() {
		return this.con;
	}

	public ArrayList<Map<String, String>> getContact() {
		try {
			gdb.openConnexion();
		} catch (Exception e) {
			e.printStackTrace();
		}

		con = gdb.getCon();
		Statement statement = gdb.getStatement();
		ResultSet resultat = null;

		String query = "SELECT DISTINCT * FROM contact";
		try {
			resultat = statement.executeQuery(query);
		} catch (SQLException e) {}

		try {
			while ( resultat.next() ) {
				int id = resultat.getInt( "id" );
				String nom = resultat.getString( "nom" );
				String tel = resultat.getString("tel" );

				Map<String, String> contact = new HashMap<>();
				contact.put("id", ""+id);
				contact.put("nom", nom);
				contact.put("tel", tel);

				contacts.add(contact);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return contacts;
	}
}
