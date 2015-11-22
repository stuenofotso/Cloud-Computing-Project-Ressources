package gpe.tp.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import gpe.tp.beans.User;

public class ConnexionForm {
	private static String CHAMP_EMAIL = "email";
	private static String CHAMP_PASSWD = "passwd";

	private String result;
	private Map<String, String> errors = new HashMap<String, String>();

	public String getResult() {
		return result;
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

	private void validateEmail( String email ) throws Exception {
		if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
			throw new Exception( "Merci de saisir une adresse mail valide." );
		}
	}

	private void validatePasswd( String passwd ) throws Exception {
		if ( passwd == null || !passwd.equals("tp") ) {
			throw new Exception( "Problème avec votre mot de passe." );
		}
	}

	public User connectUser(HttpServletRequest request) {
		String email = getValeurChamp( request, CHAMP_EMAIL );
		String passwd = getValeurChamp( request, CHAMP_PASSWD );

		User user = new User();

		try {
			validateEmail( email );
		} catch ( Exception e ) {
			setErreur( CHAMP_EMAIL, e.getMessage() );
		}
		user.setEmail(email);

		try {
			validatePasswd(passwd);
		} catch ( Exception e ) {
			setErreur( CHAMP_PASSWD, e.getMessage() );
		}
		user.setPasswd(passwd);

		if ( errors.isEmpty() ) {
			result = "Succès de la connexion.";
		} else {
			result = "Échec de la connexion.";
		}

		return user;
	}
}
