package tp.gpe;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

public class DataProvider {
	private static double[][] data;
	private static final int NBRE_COL = 2;
	private static final String SEP = "\t";

	public static double[][] readData(File f) {
		BufferedReader br=null;
		String cstr = "";
		String[] tstr = null;
		ArrayList<String> alstr = new ArrayList<String>();

		br = Utils.openReadFile(f);
		if(br != null) {
			while(( cstr = Utils.loadString(br) ) != null) { 
				alstr.add(cstr);
			}

			data = new double[alstr.size()][NBRE_COL];
			int i = -1;

			for (String string : alstr) {
				tstr = string.split(SEP);
				data[++i] = dataToDouble(tstr);
			}
		}

		return data;
	}

	private static double[] dataToDouble(String[] tstr) {
		double[] r = new double[tstr.length];

		for (int i = 0; i < r.length; i++) {
			try {
				r[i] = Double.parseDouble(tstr[i]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
				r[i] = 0.0;
			}
		}

		return r;
	}

	/*public static void main(String[] args) {
		double[][] data = DataProvider.readData(new File("results.csv"));
		for (double[] ds : data) {
			for (double d : ds) {
				System.out.print(" " + d);
			}
			System.out.println();
		}
		String r = "doc-2015-11-05 16:35:25+01:00";
		String[] t= r.split("doc-");
		System.out.println(t[1]);
	}*/
}
