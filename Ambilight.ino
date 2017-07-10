//Arduino code for Ambilight

//Instructions
//------------
//1. Configure LED_HEIGHT and LED_WIDTH to appropriate values for your setup
//2. Upload this code to your Arduino Uno (support for other boards is coming)
//3. Plug the data pin of your individually addressible LED strip into Arduino pin 11

//TODO - Send LED height and width over the COM port to allow for Arduino code that doesn't need to be individually customized
//TODO - Allow for bi-directional communication rather than just computer-to-arduino

#include <PololuLedStrip.h>

PololuLedStrip<11> ledStrip;

#define LED_HEIGHT 9
#define LED_WIDTH 16
//The variables above are here to improve efficiency; if
//values for height and width are greater than the actual
//height and width, the code will run fine. This is just
//a means of reducing wasted clock cycles on the arduino
//by not writing using ledStrip.write() to more LED's than
//is necessary; for maximum efficiency, put the same height
//and width values here as in the java program

#define LED_COUNT ((2 * LED_WIDTH) + (2 * (LED_HEIGHT - 2)))


rgb_color colors[LED_COUNT];

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1152000);
  Serial.setTimeout(1);
}

void loop() {
  if(Serial.available() > 16){
    bool readingPacket = true;
    int ledIndex = 0;
    int rVal = 0;
    int gVal = 0;
    int bVal = 0;
    String packet = "";
    while(readingPacket){
      char byteVal = Serial.read();
      if(packet.length() < 16){
        packet += (char)byteVal;
      }
      if(packet.length() == 16){
        ledIndex = (100 * ((int)(packet.charAt(0) - 48))) + (10 * ((int)(packet.charAt(1) - 48))) + (1 * ((int)(packet.charAt(2) - 48)));
        rVal = (100 * ((int)packet.charAt(4) - 48)) + (10 * ((int)packet.charAt(5) - 48)) + (1 * ((int)packet.charAt(6) - 48));
        gVal = (100 * ((int)packet.charAt(8) - 48)) + (10 * ((int)packet.charAt(9) - 48)) + (1 * ((int)packet.charAt(10) - 48));
        bVal = (100 * ((int)packet.charAt(12) - 48)) + (10 * ((int)packet.charAt(13) - 48)) + (1 * ((int)packet.charAt(14) - 48));
      }
      if(byteVal == '\n'){
        readingPacket = false;
      }
    }
    colors[ledIndex] = {rVal, gVal, bVal};
  }
  if(millis() % 30 == 0){
    ledStrip.write(colors, LED_COUNT);
  }
}

