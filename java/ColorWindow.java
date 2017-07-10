import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Represents a section of the screen that can be used to easily calculate the average color of a section of a screen output
 * @author Tommy
 *
 */
public class ColorWindow{
	private int windowXMin;
	private int windowXMax;
	private int windowYMin;
	private int windowYMax;
	
	public ColorWindow() throws AWTException{
		this.windowXMin = 0;
		this.windowXMax = 10;
		this.windowYMin = 0;
		this.windowYMax = 10;
	}
	public ColorWindow(BufferedImage image) throws AWTException{
		this.windowXMin = 0;
		this.windowXMax = image.getWidth();
		this.windowYMin = 0;
		this.windowYMax = image.getHeight();
	}
	public ColorWindow(int windowXMin, int windowXMax, int windowYMin, int windowYMax){
		this.windowXMin = windowXMin;
		this.windowXMax = windowXMax;
		this.windowYMin = windowYMin;
		this.windowYMax = windowYMax;
	}
	
	public Color getColor(BufferedImage image){
		long rSum = 0;
		long gSum = 0;
		long bSum = 0;
		int pixelCount = 0;
		for(int h = windowYMin; h < windowYMax; h++){
			for(int w = windowXMin; w < windowXMax; w++){
				Color c = new Color(image.getRGB(w, h));
				rSum += c.getRed();
				gSum += c.getGreen();
				bSum += c.getBlue();
				pixelCount++;
			}
		}
		int rAvg;
		int gAvg;
		int bAvg;
		if(pixelCount > 0){
			rAvg = (int)(rSum / (long)pixelCount);
			gAvg = (int)(gSum / (long)pixelCount);
			bAvg = (int)(bSum / (long)pixelCount);
		}else{
			rAvg = 0;
			gAvg = 0;
			bAvg = 0;
		}
		Color color = new Color(rAvg, gAvg, bAvg);
		return color;
	}
	
	public void setWindow(int windowXMin, int windowXMax, int windowYMin, int windowYMax){
		this.windowXMin = windowXMin;
		this.windowXMax = windowXMax;
		this.windowYMin = windowYMin;
		this.windowYMax = windowYMax;
	}
	public void setWindow(ColorWindow window){
		this.windowXMin = window.windowXMin;
		this.windowXMax = window.windowXMax;
		this.windowYMin = window.windowYMin;
		this.windowYMax = window.windowYMax;
	}
}