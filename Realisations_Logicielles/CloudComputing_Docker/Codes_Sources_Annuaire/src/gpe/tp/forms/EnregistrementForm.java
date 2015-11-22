package gpe.tp.forms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import gpe.tp.beans.Contact;
import gpe.tp.db.GestionDB;

public class EnregistrementForm {
	private static final String CHAMP_NOM = "nom";
	private static final String CHAMP_TEL = "tel";

	private String results;
	private Map<String, String> errors = new HashMap<String, String>();
	private GestionDB gdb = new GestionDB(); 
	Connection con = null;

	public String getResult() {
		return results;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
		String valeur = request.getParameter( nomChamp );
		if ( valeur == null || valeur.trim().length() == 0 ) {
			return null;
		} else {
			return valeur.trim();
		}
	}

	private void setErreur( String champ, String message ) {
		errors.put( champ, message );
	}

	private void validateNom( String nom ) throws Exception {
		if ( nom != null && nom.length() < 3 ) {
			throw new Exception( "Le nom doit contenir au moins 3 caractères." );
		}
	}

	private void validateTel( String tel ) throws Exception {
		if ( tel != null && !tel.matches("6 [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}") ) {
			throw new Exception( "Le numéro de téléphone doit respecter le format." );
		}
	}

	public Contact addContact(HttpServletRequest request) {
		String nom = getValeurChamp( request, CHAMP_NOM );
		String tel = getValeurChamp( request, CHAMP_TEL );

		Contact contact = new Contact();

		try {
			validateNom( nom );
		} catch ( Exception e ) {
			setErreur( CHAMP_NOM, e.getMessage() );
		}
		contact.setNom(nom);

		try {
			validateTel( tel );
		} catch ( Exception e ) {
			setErreur( CHAMP_TEL, e.getMessage() );
		}
		contact.setTel(tel);

		if ( errors.isEmpty() ) {
			ArrayList<String> myresult = null;
			myresult = saveContact(nom, tel);

			if(myresult.isEmpty()) {
				this.results = "L'ajout a échoué. Veuillez contacter l'administrateur !";				
			} else {
				this.results = "Succès de l'ajout.";
			}

		} else {
			results = "Échec de l'ajout.";
		}

		return contact;
	}

	public ArrayList<String> saveContact(String nom, String tel) {
		ArrayList<String> result = new ArrayList<String>();

		try {
			gdb.openConnexion();
		} catch (Exception e) {
			setErreur("db", "Problème lors de l'ouverture de la base de données !");
		}

		con = gdb.getCon();
		PreparedStatement preparedStatement = null;

		String query = "INSERT INTO contact(nom, tel) VALUES(?, ?);";
		try {
			preparedStatement = con.prepareStatement( query );
			preparedStatement.setString( 1, nom );
			preparedStatement.setString( 2, tel );

			/* Exécution de la requête */
			int statut = preparedStatement.executeUpdate();

			if(statut > 0) {
				result.add("true");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gdb.closeConnexion();

		return result;
	}
}
