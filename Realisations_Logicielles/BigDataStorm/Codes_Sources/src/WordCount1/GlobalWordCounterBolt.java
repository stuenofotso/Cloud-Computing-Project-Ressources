package WordCount1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class GlobalWordCounterBolt extends Observable implements IRichBolt{  //il ne doit a tout instant exister qu'une seule instance de ce bolt
	/**
	 * 
	 */
	private static final long serialVersionUID = -3964286247073097749L;
	static AtomicInteger  numberOfCurrentCollectedWords = new AtomicInteger(-1);
	Map<String, Integer> counters;
	File outputFile;
	private OutputCollector collector;




	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		counters = new HashMap<String, Integer>();
		this.collector = collector;
		outputFile = new File(stormConf.get("outputFile").toString());

		//		
		//		System.err.println(stormConf+"  "+stormConf.get("ref"));
		//		this.addObserver((Observer) stormConf.get("ref"));
		this.addObserver(WordCountTopology.wct); //ajoute le WordCountTopology comme observateur du globalWordCounterBolt

	}
	@Override
	public void execute(Tuple tuple) {
		String str = tuple.getString(0);

		if(!str.equals(FileSentenceSpout.endString)){ // si la chaine reçu n'est pas la chaine de fin de fichier, ajoute la chaine et son nombre d'occurrence reçu d'un boltcounter au compteur final
			//			System.err.println("received  "+str+" from "+tuple.getInteger(1));

			Integer count = counters.get(str);
			if (count == null)        count = 0;
			count+=tuple.getInteger(1);
			counters.put(str, count);
			collector.ack(tuple);

			//System.err.println(this+": reçu  "+str+" "+count);

			synchronized (GlobalWordCounterBolt.numberOfCurrentCollectedWords) {
				numberOfCurrentCollectedWords.decrementAndGet();  // met a jour le nombre de mots collectés restant à gérer
			}

			//			System.err.println(this.toString()+" "+str + "   "+WordCountTopology.numberOfCurrentCollectedWords);

		}

		synchronized (GlobalWordCounterBolt.numberOfCurrentCollectedWords) {
//			System.err.println(str+"  "+numberOfCurrentCollectedWords.get());

			if(str.equals(FileSentenceSpout.endString)&& numberOfCurrentCollectedWords.get()==0){ //plus aucun counterbolt actif et plus aucun mot non compte... fait le decompte global, ecrit dans le fichier et stoppe le cluster
				//mais si des mots supplémentaires arrivent après le signal de fin de fichier, gère les tout de même

				System.err.println("reçu le signal de fin ou un mot après le signal de fin "+str);
				try {
					end();	//ecrit le compteur global dans le fichier de resultats
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//			WordCountTopology.flagGlobalTaskTerminated.getAndSet(true);
				//			System.err.println("fin fin fin count="+this.countObservers());
				//			System.err.println("observer "+this);


				this.notifyObservers();  //notifie les observateurs de la fin global de l'exécution  pour extinction du cluster


				WordCountTopology.wct.update(this, null);



			}
		}



	}

	@Override
	public void cleanup() {

	}

	public void end() throws IOException {

		FileOutputStream fis;
		try {
			fis = new FileOutputStream(outputFile);

			//			System.err.println(outputFile.getAbsolutePath());
			for(Map.Entry<String, Integer> entry:counters.entrySet()){
				fis.write((entry.getKey()+" : " + entry.getValue()+"\n").getBytes());
				//				System.err.println(entry.getKey()+" : " + entry.getValue());
			}

			fis.close();



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}