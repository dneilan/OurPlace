/**
 *		Arduino with Ethernet sheild
 *                            
 */
 
metadata {
	definition (name: "On/Off Ethernet", namespace: "OurPlace", author: "DCVN") {
		capability "Actuator"
		capability "Switch"
		capability "Sensor"
        
     for(int i in 1..4) {								// Create all on & off variants
         command "on$i"
		 command "off$i"
      }
	}

    
    // Preferences
	preferences {
		input "ip", "text", title: "Arduino IP Address", description: "ip", required: true, displayDuringSetup: true
		input "port", "text", title: "Arduino Port", description: "port", required: true, displayDuringSetup: true
		input "mac", "text", title: "Arduino MAC Addr", description: "mac", required: true, displayDuringSetup: true
	}  
	
	tiles {												// UI tile definitions
     (1..4).each { n ->									// Create all Arduino tiles
			standardTile("switch$n", "device.switch$n") {
            	state "off", label: '${name}', action: "on$n", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
				state "on", label: '${name}', action: "off$n", icon: "st.switches.switch.on", backgroundColor: "#79b821"
            }
        } 
		main (["switch1"])
		details (["switch1", "switch2", "switch3", "switch4"])
	  }
    
}

//--------------------------------------- parse events into attributes ------------------------------------------

def parse(String description) {
	//log.debug "Parsing '${description}'"
	def msg = parseLanMessage(description)				// parse into following structure: header, headers, body (String)
	def headerString = msg.header
	if (!headerString) { 
    	log.debug "headerstring was null"  
    	return 
        }
    if (msg.body) {       // wow it was not null - carry on do the rest of the code 
    	def value = msg.body
    	log.debug "Received = '${value}'"  
       if (value[0..1] == 'on') return createEvent (name:"switch${value[2]}", value:"on")    
       if (value[0..2] == 'off') return createEvent (name:"switch${value[3]}", value:"off") 
 	  }  
   } 

//------------------------------------------------------------------------------------------- 

private getHostAddress() {
    def ip = settings.ip
    def port = settings.port
	log.debug "Using ip: ${ip} and port: ${port} for device: ${device.id}"
    return ip + ":" + port
}

def sendEthernet(message) {
	log.debug "Sending to Arduino ${message}"
	//log.debug "Executing 'sendEthernet' ${message}"
	new physicalgraph.device.HubAction(	method: "POST",	path: "/${message}?", headers: [ HOST: "${getHostAddress()}" ]	)
}

// Commands sent to the device
def on1() {
	log.debug "Turning on relay 1"
    sendEthernet("on1") }
    
def off1() {
	log.debug "Turning off relay 1"
	sendEthernet("off1") } 
 
def on2() {
    log.debug "Turning on relay 2"
    sendEthernet("on2") }
    
def off2() {
    log.debug "Turning off relay 2" 
    sendEthernet("off2") }

def on3() {
    log.debug "Turning on relay 3"
    sendEthernet("on3") }
    
def off3() {
    log.debug "Turning off relay 3" 
    sendEthernet("off3") }

def on4() {
    log.debug "Turning on relay 4"
    sendEthernet("on4") }
    
def off4() {
    log.debug "Turning off relay 4" 
    sendEthernet("off4") }

def configure() {
	log.debug "Executing 'configure'"
    if(device.deviceNetworkId!=settings.mac) {
    	log.debug "setting device network id"
        sendEthernet("setting device network id")
    	device.deviceNetworkId = settings.mac
   	 }
}
