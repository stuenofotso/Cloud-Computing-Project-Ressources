package WordCount1;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordSpliterBolt implements IRichBolt{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3440618750994570785L;
	private static HashMap<Object, Integer> mapSplitters = new HashMap<>();
	private static Integer  nbreBoltsSpliters=0;
	private OutputCollector collector;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		if(nbreBoltsSpliters == 0) nbreBoltsSpliters =  Integer.parseInt(stormConf.get("nbreBoltsSpliters").toString());

	}
	@Override
	public void execute(Tuple tuple) {



		String sentence = tuple.getString(0);


		if(sentence.equals(FileSentenceSpout.endString)){  //si c'est pour signaler la fin du fichier,  passe à l'état zoombie
			if(!mapSplitters.containsKey(this)){
				mapSplitters.put(this, 0);
				System.err.println("Fin du BoltSpliter "+mapSplitters.size()+"/"+nbreBoltsSpliters+" -> "+this);
			}

			if(mapSplitters.size()==nbreBoltsSpliters){ //si tous sont à l'état zoombie, envoi le signal de fin aux wordCounterBolt
				collector.emit(new Values(FileSentenceSpout.endString));
			}

		}

		else{

			if(mapSplitters.containsKey(this)){ //si tu reçoit une phrase, après être passé à l'état zoombie, gêre tout de même, mais notifie l'utilisateur du risque de non prise en compte
				System.err.println("recu après le signal de fin "+sentence);
			}

			StringTokenizer st = new StringTokenizer(sentence);
			//System.err.println(this+": reçu la phrase "+sentence);
			String str;
			while (st.hasMoreTokens()) { //découpe la phrase en mot et envoi les mots non vide

				str = st.nextToken();

				if(!str.equals(" ")){
					//System.err.println(this+": envoyé le mot "+str);
					collector.emit(new Values(str));
				}



				//			FileSentenceSpout.newFileLength++;
			}
		}


		collector.ack(tuple);
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public void cleanup() {
	}
	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}