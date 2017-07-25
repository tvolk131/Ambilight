import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Represents a section of the screen that can be used to easily calculate the average color of a section of a screen output
 * @author Tommy Volk
 *
 */
public class ColorWindow {
	private int windowXMin;
	private int windowXMax;
	private int windowYMin;
	private int windowYMax;
	
	/**
	 * Sets the window's search box to the size of the image
	 * 
	 * Pass in a screenshot of your desktop to
	 * make the window scan the entire screen
	 * @param image The image to check the resolution of
	 */
	public ColorWindow(BufferedImage image) {
		this.windowXMin = 0;
		this.windowXMax = image.getWidth();
		this.windowYMin = 0;
		this.windowYMax = image.getHeight();
	}
	/**
	 * Sets the window's search box to the coordinates specified
	 * @param windowXMin
	 * @param windowXMax
	 * @param windowYMin
	 * @param windowYMax
	 */
	public ColorWindow(int windowXMin, int windowXMax, int windowYMin, int windowYMax) {
		// TODO - Check screen resolution and throw exception if coords are out of bounds
		this.windowXMin = windowXMin;
		this.windowXMax = windowXMax;
		this.windowYMin = windowYMin;
		this.windowYMax = windowYMax;
	}
	
	/**
	 * Scans the image using the window's search box
	 * @param image A screenshot of the user's screen
	 * @return The average color of this window's search box in the image passed in
	 */
	public Color getColor(BufferedImage image) {
		long rSum = 0;
		long gSum = 0;
		long bSum = 0;
		int pixelCount = 0;
		for(int h = windowYMin; h < windowYMax; h++) {
			for(int w = windowXMin; w < windowXMax; w++) {
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
		if(pixelCount > 0) {
			rAvg = (int)(rSum / (long)pixelCount);
			gAvg = (int)(gSum / (long)pixelCount);
			bAvg = (int)(bSum / (long)pixelCount);
		} else {
			rAvg = 0;
			gAvg = 0;
			bAvg = 0;
		}
		Color color = new Color(rAvg, gAvg, bAvg);
		return color;
	}
}