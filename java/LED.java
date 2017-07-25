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
	
	public LED(SerialPort port, int index, ColorWindow window) {
		this.index = index;
		this.port = port;
		this.rVal = 0;
		this.gVal = 0;
		this.bVal = 0;
		this.window = window;
	}
	public LED(SerialPort port, int index, BufferedImage image) {
		this.index = index;
		this.port = port;
		this.rVal = 0;
		this.gVal = 0;
		this.bVal = 0;
		this.window = new ColorWindow(image);
	}
	
	/**
	 * Sends LED's internal color to its COM port
	 * 
	 * Example data packet (with every character being a byte):
	 * (Start of Heading Char) + (Data Type Char) + (Data) + (End of Transmission Char)
	 * ---------------------------
	 * Data: '256256256'
	 */
	private void sendColor() {
		byte packet[] = new byte[12];
		
		packet[0] = (byte)(this.index % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[1] = (byte)(this.index % 100 / 10 + 48);
		packet[2] = (byte)(this.index % 10 + 48);
		
		packet[3] = (byte)(this.rVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[4] = (byte)(this.rVal % 100 / 10 + 48);
		packet[5] = (byte)(this.rVal % 10 + 48);
		
		packet[6] = (byte)(this.gVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[7] = (byte)(this.gVal % 100 / 10 + 48);
		packet[8] = (byte)(this.gVal % 10 + 48);
		
		packet[9] = (byte)(this.bVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
		packet[10] = (byte)(this.bVal % 100 / 10 + 48);
		packet[11] = (byte)(this.bVal % 10 + 48);
		
		Main.sendData(packet, 1, this.port);
	}
	
	/**
	 * Sends an outside color value to the COM port
	 * @param rVal
	 * @param gVal
	 * @param bVal
	 */
	public void sendData(int rVal, int gVal, int bVal) {
		byte[] ledPacket = new byte[4];
		ledPacket[0] = (byte)this.index;
		ledPacket[1] = (byte)rVal;
		ledPacket[2] = (byte)gVal;
		ledPacket[3] = (byte)bVal;
		this.port.writeBytes(ledPacket, (long)4);
		try {Thread.sleep(7);}catch(InterruptedException e){System.out.println("Couldn't Sleep!");}
	}
	
	/**
	 * Updates the color of this LED's window on screen and sends color to controller over COM port
	 * @param image
	 */
	public void updateColor(BufferedImage image) {
		Color color = window.getColor(image);
		this.rVal = color.getRed();
		this.gVal = color.getGreen();
		this.bVal = color.getBlue();
		this.sendColor();
	}
}
