import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class Main {
	public static void main(String[] args) throws AWTException{
		SerialPort port = getPort();
		port.openPort();
		port.setBaudRate(1152000);
		Robot robot = new Robot();
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage image = robot.createScreenCapture(screenRect);
		while(true){
			image = robot.createScreenCapture(screenRect);
			
			LED led[] = new LED[20];
			for(int i = 0; i < 20; i++){
				led[i] = new LED(port, i);
				led[i].setWindow(image.getWidth() / led.length * i, image.getWidth() / led.length * (i + 1), 0, 100);
			}
			while(true){
				image = robot.createScreenCapture(screenRect);
				for(int i = 1; i < 20; i++){
					led[i].updateColor(image);
					led[i].sendData();
				}
			}
		}
	}
	
	
	public static void sendPacket(int ledIndex, int rVal, int gVal, int bVal, SerialPort port){
		if(ledIndex >= 0 && rVal <= 255 && rVal >= 0 && gVal <= 255 && gVal >= 0 && bVal <= 255 && bVal >= 0){
			byte packet[] = new byte[16];
			packet[0] = (byte)(ledIndex % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
			packet[1] = (byte)(ledIndex % 100 / 10 + 48);
			packet[2] = (byte)(ledIndex % 10 + 48);
			packet[3] = (byte)',';
			packet[4] = (byte)(rVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
			packet[5] = (byte)(rVal % 100 / 10 + 48);
			packet[6] = (byte)(rVal % 10 + 48);
			packet[7] = (byte)',';
			packet[8] = (byte)(gVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
			packet[9] = (byte)(gVal % 100 / 10 + 48);
			packet[10] = (byte)(gVal % 10 + 48);
			packet[11] = (byte)',';
			packet[12] = (byte)(bVal % 1000 / 100 + 48);//The '+ 48' converts the integer into the correct ASCII character
			packet[13] = (byte)(bVal % 100 / 10 + 48);
			packet[14] = (byte)(bVal % 10 + 48);
			packet[15] = (byte)'\n';
			
			for(int i = 0; i < packet.length; i++){
				System.out.print((char)packet[i]);
			}
			
			port.writeBytes(packet, 16);
		}else{
			System.err.println("Packet not sent; one of the values is too big or too small.");
		}
	}
	
	
	public static SerialPort getPort(){
		SerialPort ports[] = SerialPort.getCommPorts();
		SerialPort port = ports[getPortNum()];
		return port;
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
		return chosenPort - 1;
	}
}
