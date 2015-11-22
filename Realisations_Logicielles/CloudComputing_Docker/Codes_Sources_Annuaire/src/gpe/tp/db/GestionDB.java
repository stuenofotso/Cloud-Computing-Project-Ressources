package gpe.tp.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class GestionDB {
	private static final String FICHIER_PROPERTIES = "gpe/tp/db/mysql-annuaire.properties";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_DRIVER = "driver";
	private static final String PROPERTY_USER_NAME = "username";
	private static final String PROPERTY_PASSWD = "passwd";

	private Connection con = null;
	private Statement statement = null;
	private ResultSet resultat = null;

	private String url;
	private String driver;
	private String username;
	private String passwd;

	public void openConnexion() throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

		if ( fichierProperties == null ) {
			throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
		}

		Properties properties = new Properties();

		try {
			properties.load( fichierProperties );
			url = properties.getProperty( PROPERTY_URL );
			driver = properties.getProperty( PROPERTY_DRIVER );
			username = properties.getProperty(PROPERTY_USER_NAME );
			passwd = properties.getProperty(PROPERTY_PASSWD );
		} catch ( IOException e ) {
			throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
		}

		try {
			Class.forName( driver );
		} catch ( ClassNotFoundException e ) {
			throw new Exception( "Le driver est introuvable dans le classpath.", e );
		}

		con = DriverManager.getConnection( url, username, passwd );

		/* Création de l'objet gérant les requêtes */
		statement = con.createStatement();

		//System.out.println("It's ok !");
	}

	public void closeConnexion() {
		if ( con != null ) {
			try {
				con.close();
			} catch ( SQLException ignore ) {}
		}
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultat() {
		return resultat;
	}

	public void setResultat(ResultSet resultat) {
		this.resultat = resultat;
	}

	public static void main(String[] args) {
		//System.out.println(System.getProperty("user.dir"));
		GestionDB gdb = new GestionDB();
		Connection con = null;

		try {
			gdb.openConnexion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

		con = gdb.getCon();

		System.out.println(con);

		try {
			if(con != null) con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
}
