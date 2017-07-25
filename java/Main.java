import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class Main {
	public static final int LED_COUNT = 20;
	public static final byte START_OF_PACKET = Byte.parseByte("1");
	public static final byte END_OF_PACKET = Byte.parseByte("4");
	public static final byte NULL_BYTE = Byte.parseByte("0");
	
	public static void main(String[] args) throws AWTException{
		SerialPort port = getPort();
		port.openPort();
		port.setBaudRate(1152000);
		Robot robot = new Robot();
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage image = robot.createScreenCapture(screenRect);
		while(true){
			image = robot.createScreenCapture(screenRect);
			
			LED led[] = new LED[LED_COUNT];
			for(int i = 0; i < LED_COUNT; i++){
				ColorWindow window = new ColorWindow(image.getWidth() / led.length * i, image.getWidth() / led.length * (i + 1), 0, 100);
				led[i] = new LED(port, i, window);
			}
			while(true){
				image = robot.createScreenCapture(screenRect);
				for(int i = 0; i < LED_COUNT; i++){
					led[i].updateColor(image);
				}
				while(port.bytesAvailable() > 0) {
					byte[] array = new byte[port.bytesAvailable()];
					port.readBytes(array, array.length);
					for(int i = 0; i < array.length; i++) {
						byte data = array[i];
						if(data != START_OF_PACKET && data != NULL_BYTE) {
							if(data == END_OF_PACKET) {
								System.out.println();
							} else {
								System.out.print((char)data);
							}
						}
					}
				}
			}
		}
	}
	
	public static SerialPort getPort(){
		SerialPort ports[] = SerialPort.getCommPorts();
		if(ports.length == 0) {
			throw new IllegalArgumentException("No open ports available!");
		} else {
			while(true) {
				int portNum = getPortNum();
				if(portNum >= 0 && portNum < ports.length) {
					return ports[portNum];
				}
			}
		}
	}
	public static int getPortNum(){
		SerialPort ports[] = SerialPort.getCommPorts();
		System.out.println("Select a port:");
		int i = 1;
		for(SerialPort port : ports) {
			System.out.println(i++ + ". " + port.getSystemPortName());
		}
		Scanner s = new Scanner(System.in);
		int chosenPort = s.nextInt();
		s.close();
		return chosenPort - 1;
	}
	/**
	 * Sends data over the COM port
	 * @param data Data to send
	 * @param packetType Marks what type of data is being sent
	 * @param port The port to send the data to
	 */
	public static void sendData(byte[] data, int packetType, SerialPort port) {
		byte[] packet = new byte[data.length + 3];
		packet[0] = START_OF_PACKET;
		packet[1] = Byte.parseByte((packetType + 48) + "");
		for(int i = 0; i < data.length; i++) {
			packet[i + 2] = data[i];
		}
		packet[packet.length - 1] = END_OF_PACKET;
		port.writeBytes(packet, packet.length);
		
		
		String dataString = "";
		for(int i = 0; i < data.length; i++) {
			dataString += (char)data[i];
		}
		System.out.println(dataString);
	}
}
