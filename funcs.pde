
void mousePressed() {
   int varC=0;
   String cmd;
   if (mouseButton == LEFT) cmd="toggle"; else cmd="dim";
   for(int varB=0; varB<2; varB++) {
       for(int varA=0; varA<6; varA++) {
       if (overLed(LedHpos[varA],LedVpos[varB], LedSize)) toHub(cmd+(varC+1)); 
       varC++;  
     }
   }
 }

 
//--------------------------------------------------------------------------------

class dcvTimer extends Thread{
   String fName;
   int time;
 
   public dcvTimer(String fName, int time) {
      this.fName = fName;
      this.time = time;
     }

   public void run(){
    int Start=millis();
    while(millis()< Start + time);
    thread(fName);                             // function to call
    run();                                     // call itself to run again
   }
 }
 
//------------------------------------------------------------------------------------
  
Boolean overLed(int x, int y, int diameter) {              // checks to see where mouse is
  float disX = x - mouseX;
  float disY = y - mouseY;
  if (sqrt(sq(disX) + sq(disY)) < diameter/2 ) return true;
  else return false;
  }

//--------------------------------------------------------------------------------

 void parseIntstring() {
    //println(data);
    if (data.contains("Status")){                          // Data update from SmartApp or WebCore
      String pdata[] = split(data, " ");                   // Split data based upon spaces
      String[] sdata = split(pdata[1], ":");               // Split 2nd data string by ":"
      int obj = int(sdata[0]);
      LedStat[obj] = int(sdata[1]);
      LedName[obj]= sdata[2].replace('_','\n');            // Long Labels over 2 lines
      }     
    }

//--------------------------------------------------------------------------------
    
 String doLabel(String label) {
    String[] mlLabel = split(label, "_");
    println(mlLabel.length);
    for(int varA=0; varA<mlLabel.length; varA++) {
     
    }
    return label;
  }

//--------------------------------------------------------------------------------
 
class MyTimer extends Thread{
   int time;
   public MyTimer(int time){
   this.time = time;
   }
   public void run(){
    int Start=millis();
    while(millis()< Start + time);
    toHub("toggle66");
   }
 }  
  
  
//--------------------------------------------------------------------------------
 
 void toHub(String cmd) {
    try {
      myClient = new Client(this, remoteIP, remotePort);
      if (myClient.active()) {
        myClient.write("POST / HTTP/1.1\n");
        myClient.write("HOST: 172.16.10.119:39500\n");
        myClient.write("CONTENT-TYPE: text\n");
        myClient.write("CONTENT-LENGTH: " + cmd.length() + "\r\n");
        myClient.write("\r\n");
        myClient.write(cmd +"\n");
        println("Sending "+cmd);
        myClient.stop();
       }   
     }
     catch(Exception e) {
      System.out.println("Exception ");
      e.printStackTrace();
     }
  }
