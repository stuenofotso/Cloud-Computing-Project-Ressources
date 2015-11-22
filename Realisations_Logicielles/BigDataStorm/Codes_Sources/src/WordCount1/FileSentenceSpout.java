package WordCount1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
public class FileSentenceSpout implements IRichSpout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8202733153905334670L;
	private SpoutOutputCollector collector;
	private FileInputStream reader;
	public static String endString="$*$";
	private static Integer nbreBlocs=0, count=0, nbreSplouts=0;
	private byte[] buffer;
	private static HashMap<Object, Integer> mapSpouts = new HashMap<>();
	private static  int BLOCK_SIZE=100;

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		buffer = new byte[BLOCK_SIZE];

		//recuperation des parametres
		if(nbreBlocs == 0) nbreBlocs =  Integer.parseInt(conf.get("nbreBlocs").toString());
		if(nbreSplouts == 0) nbreSplouts =  Integer.parseInt(conf.get("nbreSplouts").toString());

		//initialisation des spouts avec chacun son bloc
		synchronized (count) { //section critique
			count++;
			String fileName= "blocs/bloc"+count;
			System.err.println(" bloc  "+count +" pris par "+this+" dans l'initialisation");
			//si le splout s'initialise et il reste encore des blocs non traités, il prend un et commence son traitement, sinon il ne traite rien
			if(count<=nbreBlocs)
			{
				try {
					reader = new FileInputStream(fileName);

					//				fileLength = new File("blocs/bloc"+count.toString()).length();

					//				System.err.println("file length "+fileLength);

				} catch (FileNotFoundException e) {
					throw new RuntimeException("Error reading file "
							+ "blocs/bloc"+count);
				}
			}

		}


		this.collector = collector;
		//		if(reader == null ) reader = new BufferedReader(fileReader);
	}

	public boolean readFile() throws IOException{ //fonction de lecture d'un bloc de mots dans un fichier de bloc, retourne true en cas de succès et false en cas de fin de fichier
		String str="";
		int chr=0;
		if ((reader.read(buffer)) >0) {  
			//					System.err.println("bien1");
			//				System.err.println("sending "+str+" from "+this.toString());

			str = new String(buffer) ;
			while(!str.endsWith(" ")&&!str.endsWith("\n")){ //alors le mot a été tronqué; agit...

				if((chr=reader.read())==-1){ //alors j'ai atteint la fin du fichier
					break;
				}
				else{
					str+=((char)chr);
				}


			}
			//			System.err.println(this+": envoyé la phrase "+str);
			this.collector.emit(new Values(str), str);

			//				newFileLength++;
			buffer = new byte[BLOCK_SIZE];
			return true;
		}
		return false;
	}

	@Override
	public void nextTuple() {

		//		if(completed){
		//			WordCountTopology.terminated = true;
		//			
		//
		//		}

		if(reader==null){ //declare le spout comme ayant fini son traitement si reader n'est pas initialisé
			//			System.err.println("bien2");

			if(!mapSpouts.containsKey(this)){
				mapSpouts.put(this, 0);
				System.err.println("Fin du splout  "+mapSpouts.size()+"/"+nbreSplouts+" parce que le reader est null "+" -> "+this);
			}

			if(mapSpouts.size() == nbreSplouts){
				this.collector.emit(new Values(endString), endString);
			}
			return;
		}





		try {

			if(!this.readFile()){	//si échec de la lecture du fichier, alors le fichier de bloc est terminé
				//				completed = true;
				synchronized (count) { 
					if(count < nbreBlocs){ //s'il reste encore des blocs à traiter, prend ce bloc et lit le


						try {
							String fileName= "blocs/bloc"+(count+1);
							System.err.println(" bloc  "+(count+1) +" pris par "+this+" en plus");
							reader.close();
							reader = new FileInputStream(fileName);
							if(!this.readFile()){ //après avoir initialiser le reader avec le bloc disponible, essait de le lire
								System.err.println("fichier "+ "blocs/bloc"+(count+1)+" vide");

								//System.err.println("succès de "+ "blocs/bloc"+(count+1));
							}
							count++;
							//				fileLength = new File("blocs/bloc"+count.toString()).length();

							//				System.err.println("file length "+fileLength);

						} catch (FileNotFoundException e) {
							System.err.println("Error reading file "
									+ "blocs/bloc"+(count+1));

						}

					}



					else{ //s'il n'y a plus aucun bloc non traité, alors termine toi et envoit en continue le signal de fin de fichier aux bolts

						if(!mapSpouts.containsKey(this)){
							mapSpouts.put(this, 0);
							System.err.println("Fin du splout "+mapSpouts.size()+"/"+nbreSplouts+" parce que plus rien à faire "+" -> "+this);
						}
						if(mapSpouts.size() == nbreSplouts){
							this.collector.emit(new Values(endString), endString);
						}
					}
				}



			}
		} catch (Exception e) {
			System.err.println("Error reading file");
		}




	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	@Override
	public void close() {
		try {
			if(reader!=null)reader.close(); // a l'arret du splout, ferme le reader
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public boolean isDistributed() {
		return false;
	}

	@Override
	public void ack(Object arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Object arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}



}