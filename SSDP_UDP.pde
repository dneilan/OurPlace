import hypermedia.net.*;
 
 UDP udpTXR;                                                           // define the UDP object
 String remoteIP = "172.16.10.255";                                    // the remote IP address
 int remotePort = 1900;                                                // the destination port
 String buffer;                                                        // Input string from serial port
 int lf = 10;                                                          // ASCII linefeed 


//----------------------------------------------------
 
void setup() { 
  size(40,40); 
  udpTXR = new UDP(this, remotePort);
  //udpTXR.log( true );     // <-- printout the connection activity
  buffer= "M-SEARCH * HTTP/1.1\r\n";
  buffer=buffer +  "HOST: 239.255.255.250:1900\r\n";
  buffer=buffer +  "MAN: \"ssdp:discover\"\r\n";
  buffer=buffer +  "MX: "+"10"+ "\r\n";                              // seconds to delay response
  buffer=buffer +  "ST: "+ "ssdp:all" + "\r\n" + "\r\n";
 // buffer=buffer +  "ST: "+ "urn:schemas-upnp-org:device:InternetGatewayDevice:1" + "\r\n" + "\r\n";
  udpTXR.send(buffer, remoteIP, remotePort );
  println("Sent data:");
  println(buffer);
  udpTXR.listen( true );                                           // and wait for incoming message
  } 

//----------------------------------------------------
 
void draw() { 

  } 
  
 void receive( byte[] data, String sendIP, int portRX ) {  
      if(sendIP.contains("172.16.10.119")) {
      print("Received from:",sendIP);
      println(" on port:",portRX);
      String buffer = new String(data);
      println(buffer);
    }
   } 