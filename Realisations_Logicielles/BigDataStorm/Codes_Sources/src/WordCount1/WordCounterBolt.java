package WordCount1;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordCounterBolt implements IRichBolt{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6575187836283596975L;
	Map<String, Integer> counters;
	private OutputCollector collector;
	private static HashMap<Object, Integer> mapCounters = new HashMap<>();
	private static Integer  nbreBoltsCounters=0;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		counters = new HashMap<String, Integer>();
		this.collector = collector;

		//recupère les paramètres
		if(nbreBoltsCounters == 0) nbreBoltsCounters =  Integer.parseInt(stormConf.get("nbreBoltsCompters").toString());

	}
	@Override
	public void execute(Tuple tuple) {
		String str = tuple.getString(0);

		//		System.err.println(this.toString()+" receiving "+str);

		if(str.equals(FileSentenceSpout.endString)){  //si c'est le signal de fin de fichier, envoit les mots collectés au globalWordCounterBolt et passe à l'état zoombie 

			if(!mapCounters.containsKey(this)){  //si tu n'es pas encore à l'état zoombie, vas y
				synchronized (GlobalWordCounterBolt.numberOfCurrentCollectedWords) {
					if(GlobalWordCounterBolt.numberOfCurrentCollectedWords.get()==-1){
						GlobalWordCounterBolt.numberOfCurrentCollectedWords.getAndSet(end());
					}
					else{
						GlobalWordCounterBolt.numberOfCurrentCollectedWords.addAndGet(end());
					}
				}
				
				mapCounters.put(this, 0);
				System.err.println("Fin du BoltCounter "+mapCounters.size()+"/"+nbreBoltsCounters+" -> "+this);
			}

			else{	//si tous les WordCounterBolts sont à l'état zoombie, envoyer le signal de fin au globalWordCounterBolt
				if(mapCounters.size()==nbreBoltsCounters){
					collector.emit(new Values(FileSentenceSpout.endString, 1));
				}
			}





		}



		//		if(end){  //si la fin du fichier a deja ete atteinte
		//
		//			if(!str.equals(FileSentenceSpout.endString)){  //alors il s'agit d'un mot qui a survecut; envoit le direct au globalwordcounter
		//				
		//				WordCountTopology.numberOfCurrentCollectedWords.addAndGet(1);
		//			}
		//			collector.emit(new Values(str, 1));
		//		}
		else{
			//System.err.println(this+": reçu le mot "+str);
			//			if(str.equals(FileSentenceSpout.endString)){  //si la fin du fichier a deja ete atteinte, fait les comptes et envoi au globalwordcounter, puis passe a l'etat zombie
			//				WordCountTopology.numberOfCurrentActiveCounterBolts.decrementAndGet();
			//				WordCountTopology.numberOfCurrentCollectedWords.addAndGet(end());
			//				System.err.println("End from count-bolt "+this.toString());
			//				end = true;
			//			}
			//			else{  //sinon continue a remplir ton compteur
			//		System.err.println("received  "+str+" from "+this.toString());

			if(mapCounters.containsKey(this) ){ //si tu es à l'état zoombie et que tu reçoit un mot, vraiment par le plus grand des hasard, redirige le vers le globalWordCounterBolt s'il est toujours actif
				System.err.println("recu après le signal de fin"+str);
				synchronized (GlobalWordCounterBolt.numberOfCurrentCollectedWords) {
					if(GlobalWordCounterBolt.numberOfCurrentCollectedWords.get()==-1){
						GlobalWordCounterBolt.numberOfCurrentCollectedWords.getAndSet(1);
					}
					else{
						GlobalWordCounterBolt.numberOfCurrentCollectedWords.incrementAndGet();
					}
				}
				
				
				collector.emit(new Values(str, 1));

			}else{		//ajoute le mot à ton compteur si tu est à l'état actif
				Integer count = counters.get(str);
				if (count == null)        count = 0;
				count++;
				counters.put(str, count);
			}



			//			}
		}
		collector.ack(tuple);


	}
	@Override
	public void cleanup() {


	}

	public int end(){  //fontion qui collecte les mots recensés et les envoit au globalWordCounterBolt
		for(Map.Entry<String, Integer> entry:counters.entrySet()){
			//System.err.println(this+": envoyé  "+entry.getKey()+" "+entry.getValue());
			collector.emit(new Values(entry.getKey(), entry.getValue()));

		}
		return counters.size();
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}