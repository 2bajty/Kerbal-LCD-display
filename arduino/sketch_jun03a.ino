#include <TM1638.h>

TM1638 module(5, 4, 6);

String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void onCommand(String cmd) {
  for(int i = cmd.length(); i < 16; i++) {
    cmd += " ";
  }
  
  module.setDisplayToString(cmd.substring(0,8));
  for(int i = 0; i < 8;i++) {
    char c = cmd.charAt(8+i);
    if(c == 'R') {
      module.setLED(TM1638_COLOR_GREEN, i);
    } else if(c == 'G') {
      module.setLED(TM1638_COLOR_RED, i);
    } else if(c == 'B') {
      module.setLED(TM1638_COLOR_GREEN + TM1638_COLOR_RED, i);
    } else if(c == 'N' || c == ' ') {
      module.setLED(TM1638_COLOR_NONE, i);
    }
  }
}

void setup() {
  module.setupDisplay(true, 1);
  Serial.begin(115200);
  inputString.reserve(200);
  Serial.println("Start");
}

void loop() {
  if (stringComplete) {
//    Serial.println(inputString); 
    onCommand(inputString);
    inputString = "";
    stringComplete = false;
  }
  
  while (Serial.available()) {
    char inChar = (char)Serial.read(); 
    if (inChar == '\n') {
      stringComplete = true;
    } else {
      inputString += inChar;
    }
  }
}
