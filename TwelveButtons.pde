/*
See timertestthreading for timer 

*/
import java.net.SocketException;
import processing.net.*;
Client myClient1;                                       // Initialize http Client
Client myClient;                                        // Initialize http Client
Server myServer;                                        // Initialize http Server
String data;
String remoteIP = "172.16.10.119";                      // Smartthings Hub IP
int remotePort = 39500;                                 // Smartthings Hub listening port
int listenPort = 8090;                                  // PC listening port
int LedHpos[] = {40, 100, 160, 220, 280, 340};
int LedStat[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
int LedVpos[] = {30, 105};
color LedColor[] = {#ffcccd, #ffcd18, #ff1924};
String LedName[] = {" "," "," "," "," "," "," "," "," "," "," "," "};
int LedSize = 30;
int LedNum = 9; 
int overLed=0;
String label;

void setup(){
  size(385, 170);
  background(200);
  ellipseMode(CENTER);
  myServer = new Server(this, listenPort);                // Start http Server
  toHub("toggle66");                                      // Initial request device info/status
 // new dcvTimer("uPdate", 360000).start();                 // check status every 6 minutes
  }

void draw() {
  myClient1 = myServer.available();
  drawIt();
   if (myClient1 != null) {
      data = myClient1.readStringUntil('\n');     
      myClient1.clear();
      myClient1.write("HTTP/1.1 200 OK\n");
      myClient1.stop();
      parseIntstring();
      }
   }
   
//--------------------------------------------------------------------------------

void drawIt() {
   background(200);
   int varC = 0;
   for(int varB=0; varB<2; varB++) {
    for(int varA=0; varA<6; varA++) {
      fill(LedColor[LedStat[varC]]);
      ellipse(LedHpos[varA],LedVpos[varB], LedSize, LedSize);
      fill(70);
      textAlign(CENTER);
      if (varC <6 ) text(LedName[varC],LedHpos[varC],LedVpos[0]+30);
      else text(LedName[varC], LedHpos[varC-6],LedVpos[1]+30);
      varC++;
    }
   }
  }

    
  void uPdate() {
     toHub("toggle66");
     drawIt();
     }
