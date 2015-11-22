package tp.gpe;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * @author FLAMBEAU
 * Classe Utilitaire
 */
public class Utils {
    //Les fonctions lireChaine et LireEntier
	//Exemple d'utilisation
	//String nom = Utils.lireChaine("Entrer votre nom");
	public static String lireChaine(String question){
		InputStreamReader ir = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(ir);
		System.out.print(question);
		System.out.flush();
		String reponse = "";
		try{
			reponse = br.readLine();
		} catch (IOException ioe){}
		return reponse;
	}
	// La lecture d'un entier n'est qu'un parseInt de plus!!
	public static int lireEntier(String question){
		return Integer.parseInt(lireChaine(question));
	}
	//écriture d'une chaîne dans un BufferedWriter
	public static void saveString(BufferedWriter bw,String s){
		try{
			bw.write(s);
			bw.newLine();
		}
		catch(IOException ioe){}
	}
	//écriture d'un entier: on convertit en chaîne et on se ramène
	//à la procédure précédente
	public static void saveInt(BufferedWriter bw, int i){
		Integer q = new Integer(i);
		saveString(bw,q.toString());
	}
	//ecriture d'un double : on converti en chaîne et on se ramène à la procédure précédente
	public static void saveDouble(BufferedWriter bw,double d){
		Double q = new Double(d);
		saveString(bw,q.toString());
	}
	// ouverture en écriture d'un fichier
	// rend BufferedWriter -- null si pas possible de l'ouvrir
	public static BufferedWriter openWriteFile(File f){
		BufferedWriter bw = null;
		try{
			FileWriter fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
		}
		catch (IOException ioe){}
		if(bw==null) System.out.println(f.getAbsolutePath());
		return bw;
	}
	//ouverture en lecture d'un fichier
	//rend un BufferedReader -- null si pas possible de l'ouvrir
	public static BufferedReader openReadFile(File f){
		BufferedReader br=null;
		try{
			FileReader fr = new FileReader(f);
			br = new BufferedReader(fr);
		}
		catch (IOException ioe){}
		return br;
	}
	// Lecture dans un BufferedReader d'une chaine de caracteres
	public static String loadString(BufferedReader br){
		String s = "";
		try{
			s = br.readLine();
		}
		catch (IOException ioe){}
		return s;
	}
	// lecture d'un entier dans un BufferedReader
	public static int loadInt(BufferedReader br){
		return Integer.parseInt(loadString(br));
	}
	//lecture d'un double dans un Bufferedreader
	public static double loadDouble(BufferedReader br){
		return Double.parseDouble(loadString(br));
	}
}
