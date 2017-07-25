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

#define START_OF_PACKET B00000001
#define END_OF_PACKET B00000100
#define MAX_PACKET_LENGTH 16
#define MAX_LED_COUNT 512

rgb_color colors[MAX_LED_COUNT];
int ledCount = 0; // Defaults to zero LEDs

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1152000);
  Serial.setTimeout(1);
}

void sendData(byte data[MAX_PACKET_LENGTH], int packetType) {
  byte *packet = new byte[MAX_PACKET_LENGTH + 3];
  packet[0] = START_OF_PACKET;
  packet[1] = packetType;
  for(int i = 0; i < MAX_PACKET_LENGTH; i++) {
    packet[i + 2] = data[i];
  }
  packet[MAX_PACKET_LENGTH + 3 - 1] = END_OF_PACKET;
  Serial.write(data, MAX_PACKET_LENGTH + 3);
}

void readData(byte *emptyPacket) {
  byte *packet = new byte[MAX_PACKET_LENGTH + 3];
  int packetLength = 0;
  while(true) {
    if(Serial.available() > 0) {
      byte currentByte = Serial.read();
      if(packetLength == 0 && !(currentByte = START_OF_PACKET)) {
        resetPacket(packet, packetLength); // TODO - Remove this line and refactor
      } else if(currentByte == START_OF_PACKET) {
        resetPacket(packet, packetLength);
        addByteToPacket(currentByte, packet, packetLength);
      } else if(packetLength == MAX_PACKET_LENGTH) {
        resetPacket(packet, packetLength);
      } else if(currentByte == END_OF_PACKET) {
        addByteToPacket(currentByte, packet, packetLength);
        emptyPacket = packet;
        resetPacket(packet, packetLength);
      } else {
        addByteToPacket(currentByte, packet, packetLength);
      }
    }
  }
}

void resetPacket(byte *packet, int packetLength) {
  packetLength = 0;
  packet = new byte[MAX_PACKET_LENGTH];
}
void addByteToPacket(byte data, byte *packet, int packetLength) {
  packet[packetLength] = data;
  packetLength++;
}
void processPacket(byte *packet) {
//  int packetType = packet[1] - 48;
//  if(packetType == 1) {
//    colors = rgb_color[(100 * ((int)packet[4] - 48)) + (10 * ((int)packet[5] - 48)) + (1 * ((int)packet[6] - 48))];
//  }
}

byte *packet;
void loop() {
  readData(packet);
  processPacket(packet);
  
//  byte *outPacket = new byte[MAX_PACKET_LENGTH];
//  for(int i = 0; i < MAX_PACKET_LENGTH; i++) {
//    outPacket[i] = B00110100;
//  }
//  sendData(outPacket, 3);
  
  if(millis() % 30 == 0){
    ledStrip.write(colors, ledCount);
  }
}

