package WordCount1;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class WordCountTopology implements Observer{

	//	static AtomicInteger numberOfCurrentActiveCounterBolts = new AtomicInteger(10), numberOfCurrentCollectedWords = new AtomicInteger(0);


	//	static AtomicBoolean flagGlobalTaskTerminated = new AtomicBoolean(false);
	private LocalCluster cluster;
	private static  int BLOCK_SIZE=1048576*64;  //tailles des blocs de fichiers générés, peut être mise à jour pour faire correspondre le nombre de blocs au nombre de splouts configuré

	public void execute(String[] args) throws IOException{
		Config config = new Config();
		TopologyBuilder builder = new TopologyBuilder();

		//découpage du fichier source en blocs 
		int nbre_blocs = 0;
		long fileLength = 0;

		try{
			fileLength = new File(args[0]).length();
		}catch(Exception e){
			e.printStackTrace();
		}

		if(fileLength!=0) BLOCK_SIZE = (int) (fileLength/ Integer.parseInt(args[3])); //si c'est possible de lire la taille du fichier, alors créé autant de blocs que de splouts

		byte[] buffer = new byte[BLOCK_SIZE];

		FileInputStream fis = new FileInputStream(args[0]);
		FileOutputStream fos;
		int chr=0;

		while((chr = fis.read(buffer))>0){ //chr prend le nombre de caracteres lus
			nbre_blocs++;
			fos = new FileOutputStream("blocs/bloc"+nbre_blocs);
			fos.write(buffer);

			chr = buffer[chr-1];

			while(((char)chr)!=' '&&((char)chr)!='\n'){ //alors le mot a été tronqué; agit...
				//				System.err.println("chr "+chr);
				chr=fis.read();
				if(chr==-1){ //alors j'ai atteint la fin du fichier
					break;
				}
				else{
					fos.write(chr);
				}


			}

			fos.close();
			buffer = new byte[BLOCK_SIZE];
		}
		//		nbre_blocs++;
		//		fos = new FileOutputStream("blocs/bloc"+nbre_blocs);
		//		
		fis.close();



		//les paramètres de configuration des splouts et bolts

		config.put("nbreBlocs", nbre_blocs);
		config.put("nbreSplouts", args[3]);
		config.put("nbreBoltsSpliters", args[4]);
		config.put("nbreBoltsCompters", args[5]);
		config.put("outputFile", args[1]);



		config.setDebug(false);
		try{
			//création de la topologie
			config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, Integer.parseInt(args[2]));

			GlobalWordCounterBolt gwcb = new GlobalWordCounterBolt();

			//			gwcb.addObserver(this);
			//			System.err.println("observer "+gwcb);


			builder.setSpout("file-sentence-spout", new FileSentenceSpout(),  Integer.parseInt(args[3]));
			builder.setBolt("word-spitter", new WordSpliterBolt(),  Integer.parseInt(args[4])).shuffleGrouping("file-sentence-spout");
			builder.setBolt("word-counter", new WordCounterBolt(),  Integer.parseInt(args[5])).shuffleGrouping("word-spitter" );
			builder.setBolt("global-word-counter", gwcb).globalGrouping("word-counter" );

			//			numberOfCurrentActiveCounterBolts.getAndSet(Integer.parseInt(args[5]));

			System.err.println("starting WordCount with :\n" +
					"InputFile: "+args[0]+"\n"+
					"OutputFile: "+args[1]+"\n"+
					"Topology_Max_Spout_Pending: "+args[2]+"\n"+
					"NumberOfConcurrentSpout: "+args[3]+"\n"+
					"NumberOfConcurrentSpliterBolt: "+args[4]+"\n"+
					"NumberOfConcurrentCounterBolt: "+args[5]+"\n");

		}catch(NumberFormatException e){
			e.printStackTrace();
			System.err.println("Error on Fild type, please check and correct... exple of parameters: input.txt output.txt 2 13 13 10");
			System.exit(1);
		}

		cluster = new LocalCluster();
		cluster.submitTopology("WordCountTopology", config, builder.createTopology());

		//		final Long start = System.currentTimeMillis();


		//
		//		Thread t = new Thread(new Runnable() {
		//			@Override
		//			public void run() {
		//				while(!flagGlobalTaskTerminated.get()){
		//					System.out.print("");
		//				}
		//				System.err.println("\n\nEnded Successfully \n\n");
		//
		//
		//				cluster.shutdown();
		//			}
		//		});
		//
		//		t.start();



	}

	
	@Override
	public void update(Observable o, Object arg) { // fonction a exécuter pour terminer le cluster à la réception du signal de fin d'exécution des bolts
		// TODO Auto-generated method stub
		System.err.println("\n\nEnded Successfully \n\n");
		cluster.killTopology("WordCountTopology");
		System.exit(0);
//		cluster.shutdown();
//		System.err.println("dddd");
		
		
	}

	static WordCountTopology wct= new WordCountTopology();

	public static void main(String[] args) throws Exception{

		System.setOut(new PrintStream(new OutputStream() {  //élimination de l'affichage par out.println pour accélérer l'exécution du programme
			@Override public void write(int b) throws IOException {}
		}));

		//		System.setErr(new PrintStream("error"));

		if(args.length <6){
			System.err.println("bad syntax... you must provide in this order  inputFileName, outputFileName, " +
					"Topology_max_spout_pending, Number of Spout, Number of spliterBolt and Number of CounterBolt");
			System.exit(1);
			//			config.put("inputFile", "file03");
			//			config.put("outputFile", "results");
		}
		wct.execute(args);




	}



}