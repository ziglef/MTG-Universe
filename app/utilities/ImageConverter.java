package utilities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
 
public class ImageConverter {
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
 
    public static boolean convertFormat(File formImage, String destPath, String formatName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(destPath);
        BufferedImage tmpImage = ImageIO.read(formImage);
        
        // Scale
        BufferedImage inputImage = resize(tmpImage, 350, 350);
        
        boolean result = ImageIO.write(inputImage, formatName, outputStream);
         
        outputStream.close();
         
        return result;
    }
}