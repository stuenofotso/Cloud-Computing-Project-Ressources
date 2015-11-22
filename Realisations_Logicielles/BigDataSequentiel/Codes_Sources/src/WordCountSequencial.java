import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;


public class WordCountSequencial {

	
	 private BufferedReader reader;
	 private HashMap<String, Integer> map;
	 
	 private String tmp;
	 private int count=0;
	 
	 
	public WordCountSequencial(String fileName) throws FileNotFoundException{
	
			reader = new BufferedReader(new FileReader(new File(fileName)));
			map = new HashMap<String, Integer>();
			
			String sentence = "";
			while((sentence=nextTuple())!=null){
				execute(sentence);
			}
			
			PrintWriter pw;
			
			pw = new PrintWriter("results_"+fileName);
			
			for(String str: map.keySet()){
				pw.println(str+" : "+map.get(str));
			}
			
			pw.close();
	
	}
	
	public String nextTuple() {


		
		String str;
		
		try {
			if ((str = reader.readLine()) != null) {
//				System.err.println("sending "+str+" from "+this.toString());
				return str;
				
//				newFileLength++;
			}
			else{
//				completed = true;
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading file", e);
		}
	



	}
	
	
	public void execute(String sentence) {  //on suppose que sentence est différent de null






			StringTokenizer st = new StringTokenizer(sentence);
			while (st.hasMoreTokens()) {
				
				tmp = st.nextToken();
				
				if(map.containsKey(tmp)) count = map.get(tmp);
				else count = 0;
				map.put(tmp, count+1);

			}
		}


	
	
	
	public static void main(String[] args){
		if(args.length<1){
			System.err.println("Vous devez spécifier le nom du fichier de mots");
			System.exit(-1);
		}
		
		try {
			new WordCountSequencial(args[0]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
