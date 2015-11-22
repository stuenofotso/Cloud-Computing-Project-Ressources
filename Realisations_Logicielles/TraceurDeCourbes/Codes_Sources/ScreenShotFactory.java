package tp.gpe;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenShotFactory {
	public final static String IMAGE_TYPE_JPEG = "jpeg";

    public final static String IMAGE_TYPE_GIF = "gif";

    public final static String IMAGE_TYPE_PNG = "png";
    static Graphics g1;
    private static BufferedImage buf = null; // Notre capture d'écran originale
    private static  BufferedImage bufFinal = null; // Notre capture d'écran redimensionnée
    public static GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    public static void screenShot(Rectangle screenArea,
            Dimension screenshotFinalDimension, String pictureName) {
       

        try {
            // Création de notre capture d'écran
            buf = new Robot().createScreenCapture(screenArea);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        // Création de la capture finale et  Redimensionnement de la capture originale
        bufFinal = configuration.createCompatibleImage((int)screenshotFinalDimension.width,  (int)screenshotFinalDimension.height);
        Graphics2D g = (Graphics2D) bufFinal.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
       // g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
      
        g.drawImage(buf, 0, 0,(int) screenshotFinalDimension.width,
                (int)screenshotFinalDimension.height, 0, 0, buf.getWidth(), buf.getHeight(),  null);
        g.dispose();
        
       
        //System.out.println("taille: "+screenshotFinalDimension.width+"   "+screenshotFinalDimension.height);

        //  Ecriture de notre capture d'écran redimensionnée
        try {
            ImageIO.write(bufFinal, IMAGE_TYPE_PNG, new File(pictureName));
        } catch (IOException e) {
       //    e.printStackTrace();
        }
        
       
    }
    
  

    /*public static void main(String[] args) {
        ScreenShotFactory.screenShot(new Rectangle(0, 0, 700, 700),
                new Dimension(700, 700), "test1.png");
    }*/
}
