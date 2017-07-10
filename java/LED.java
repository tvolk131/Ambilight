import java.awt.AWTException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import com.fazecast.jSerialComm.SerialPort;

public class LED{
	private int index;
	private SerialPort port;
	private int rVal;
	private int gVal;
	private int bVal;
	private ColorWindow window;
	
	public LED(SerialPort port, int index) throws AWTException{
		this.index = index;
		this.port = port;
		this.rVal = 0;
		this.gVal = 0;
		this.bVal = 0;
		this.window = new ColorWindow();
	}
	public LED(SerialPort port, int index, BufferedImage image) throws AWTException{
		this.index = index;
		this.port = port;
		this.rVal = 0;
		this.gVal = 0;
		this.bVal = 0;
		this.window = new ColorWindow(image);
	}
	
	/**
	 * Sends LED's internal color to its COM port
	 */
	public void sendData(){
		byte packet[] = new byte[16];
		packet[0] = (byte)(this.index % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[1] = (byte)(this.index % 100 / 10 + 48);
		packet[2] = (byte)(this.index % 10 + 48);
		packet[3] = (byte)',';
		packet[4] = (byte)(this.rVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[5] = (byte)(this.rVal % 100 / 10 + 48);
		packet[6] = (byte)(this.rVal % 10 + 48);
		packet[7] = (byte)',';
		packet[8] = (byte)(this.gVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[9] = (byte)(this.gVal % 100 / 10 + 48);
		packet[10] = (byte)(this.gVal % 10 + 48);
		packet[11] = (byte)',';
		packet[12] = (byte)(this.bVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[13] = (byte)(this.bVal % 100 / 10 + 48);
		packet[14] = (byte)(this.bVal % 10 + 48);
		packet[15] = (byte)'\n';
		
		for(int i = 0; i < packet.length; i++){
			System.out.print((char)packet[i]);
		}
		
		port.writeBytes(packet, 16);
	}
	
	/**
	 * Sends an outside color value to the COM port
	 * @param rVal
	 * @param gVal
	 * @param bVal
	 */
	public void sendData(int rVal, int gVal, int bVal){
		byte[] ledPacket = new byte[4];
		ledPacket[0] = (byte)this.index;
		ledPacket[1] = (byte)rVal;
		ledPacket[2] = (byte)gVal;
		ledPacket[3] = (byte)bVal;
		this.port.writeBytes(ledPacket, (long)4);
		try {Thread.sleep(7);}catch(InterruptedException e){System.out.println("Couldn't Sleep!");}
	}
	
	public void updateColor(BufferedImage image){
		Color color = window.getColor(image);
		this.rVal = color.getRed();
		this.gVal = color.getGreen();
		this.bVal = color.getBlue();
	}
	
	public void setWindow(int windowXMin, int windowXMax, int windowYMin, int windowYMax){
		this.window.setWindow(windowXMin, windowXMax, windowYMin, windowYMax);
		System.out.println("Window Set! XMin: " + windowXMin + ", xMax: " + windowXMax);
	}
	public void setWindow(ColorWindow window){
		this.window.setWindow(window);
	}
}
