package tp.gpe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DrawGraphic2D {
	//private static final int DELAY = 300;

	XYSeries[] courbes;
	JFrame frame;

	public DrawGraphic2D(String prefixeNomCourbe, int[] indicesDesCourbes){

		courbes = new XYSeries[indicesDesCourbes.length];

		for(int i=0; i<indicesDesCourbes.length; i++){
			courbes[i] = new XYSeries(prefixeNomCourbe+indicesDesCourbes[i]);
		}

	}

	public void addPoint(int idCourbe, double X, double Y){
		courbes[idCourbe].add(X, Y);
	}

	public  void drawGraphic(String XaxisName, String YaxisName){

		XYSeriesCollection collection1 = new XYSeriesCollection();

		for(XYSeries serie:courbes){
			collection1.addSeries(serie);
		}


		XYItemRenderer renderer1 = new StandardXYItemRenderer();
		NumberAxis xAxis1 = new NumberAxis(XaxisName);
		XYPlot subplot1 = new XYPlot(collection1, xAxis1, null, renderer1);
		subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		//		XYTextAnnotation annotation
		//		= new XYTextAnnotation("Hello!", 50.0, 10000.0);
		//		annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
		//		annotation.setRotationAngle(Math.PI / 4.0);
		//		subplot1.addAnnotation(annotation);


		CombinedRangeXYPlot plot = new CombinedRangeXYPlot(new NumberAxis(
				YaxisName));
		plot.setGap(10.0);

		plot.add(subplot1, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		JFreeChart chart = new JFreeChart(plot);

		frame = new JFrame("courbes");

		frame.setSize(500, 500);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(new ChartPanel(chart), BorderLayout.CENTER);

		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}


	public static void main(String[] args) {
		DrawGraphic2D dg1 = null;
		int[] indices = null;

		if(args.length == 1) {
			File dir = new File("./"+args[0]);

			if(dir == null || !dir.isDirectory()) {
				System.out.println("Le dossier '"+args[0]+"' n'existe pas ou bien n'est pas un dossier !");
				System.exit(-1);
			}

			File[] spouts = dir.listFiles();

			if (spouts != null) {
				int delay = 0;
			   for (File spout : spouts) {
			      // Do something with child
				   File[] spliters = spout.listFiles();
				   indices = new int[spliters.length];
				   int nc = 0, k = -1;

				   for (File spliter : spliters) {
					   indices[++k] = Integer.parseInt(spliter.getName());
				   }

				   dg1 = new DrawGraphic2D("Bolt-Spliter : ", indices);

				   for (File spliter : spliters) {
					   File[] results = spliter.listFiles();

					   if(results != null) {
						   double[][] datas = DataProvider.readData(results[0]);

						   for (int i = 0; i < datas.length; i++) {
							   dg1.addPoint(nc, datas[i][0], datas[i][1]);
						   }

						   nc++;
					   }
				   }

				   try {
					   dg1.drawGraphic("Bolt-Counter", "Time");
					   Thread.sleep(2000+delay);
					   JFrame frame = dg1.getFrame();
					   int x = frame.getX(), y = frame.getY(), width = frame.getWidth(), height = frame.getHeight();

					   String prefix = args[0].split("doc-")[1];

					   ScreenShotFactory.screenShot(new Rectangle(x, y, width, height), new Dimension(width, height), 
							   "./img-"+prefix+"/"+spout.getName()+"-spouts.png");
					   //delay += DrawGraphic2D.DELAY;
					   frame.setVisible(false);
					} catch (InterruptedException e) {}
			   }
			   System.exit(0);
			} else {
				System.out.println("Oops... !");
			}
		} else {
			System.out.println("Oops... ! Le dossier spécifié est introuvable.");
		}
	}
}