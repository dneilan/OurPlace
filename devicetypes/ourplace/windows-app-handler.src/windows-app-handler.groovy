/**
 *  Windows App Handler
 *
 *  Copyright 2017 DCVN
 *
 */
metadata {
	definition (name: "Windows App Handler", namespace: "OurPlace", author: "DCVN") {
		capability "Button"
        capability "Holdable Button"
        capability "Actuator"
        capability "Switch"
        capability "Polling"
        capability "Switch Level"
          
        command "updated"
        command "initialize"
        command "doSomething"
        command "parseStatus"
        
	}

// Preferences
preferences {
		input "ip", "text", title: "Windows PC IP Address", description: "ip", required: true, displayDuringSetup: true
		input "port", "text", title: "Windows PC Port", description: "port", required: true, displayDuringSetup: true
		input "mac", "text", title: "Windows PC MAC Addr", description: "mac", required: true, displayDuringSetup: true
	}

	tiles {												// UI tile definitions						
		multiAttributeTile(name: "rich-control", type: "generic", width: 1, height: 1) {
			tileAttribute("device.button", key: "PRIMARY_CONTROL") {
				attributeState "default", label: ' ', action: "", icon: "st.unknown.zwave.remote-controller", backgroundColor: "#ffffff"
			}
         }
        standardTile("refresh", "device.power", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'update Buttons', action:"initialize", icon:"st.secondary.refresh"
		}
        childDeviceTiles("outlets")
	  }  
}

def parse(String description) {										// parse events into attributes
	//log.debug "Parsing '${description}'"
    def msg = parseLanMessage(description)				// parse into following structure: header, headers, body (String)
   // log.debug "Header - ${msg.header}"
   // log.debug "Status - ${msg.status}"
   // log.debug "Body - ${msg.body}"
   	if (msg.body) {       								// wow it was not null - carry on do the rest of the code 
    	def results
        def value = msg.body
        def btn
    	//log.debug "Received  ${value}" 										 
        if (value.startsWith('on')) {	
        	btn = value.replaceAll('on','')
            results = createEvent(name: "button", value: "pushed", data: [buttonNumber: btn.toInteger()], isStateChange: true, displayed:false)
  	  	 }
       if (value.startsWith('toggle')) {
      		 btn = value.replaceAll('toggle','')
             results = createEvent(name: "button", value: "pushed", data: [buttonNumber: btn.toInteger()], isStateChange: true, displayed:false)
  	  	 }
        if (value.startsWith('dim')) {
        	btn = value.replaceAll('dim','')
            results = createEvent(name: "button", value: "held", data: [buttonNumber: btn.toInteger()], isStateChange: true, displayed:false)
  	  	 }
    /*    if (value.startsWith('off')) {
        	btn = value.replaceAll('off','')
            results = createEvent(name: "button", value: "pushed", data: [buttonNumber: btn.toInteger()], isStateChange: true, displayed:false)
     	 }	*/
        //log.debug "createEvent ${results}"
        return results
 	  } 
}

def sendEthernet(message) {														// Send Ethernet message
	//log.debug "Http to controller - ${message}"
	new physicalgraph.device.HubAction(method: "POST",	path: "${message}", headers: [ HOST: "${settings.ip + ":" + settings.port}" ])
	}

def configure() {
	sendEvent(name: "numberOfButtons", value: 13)
	def cmds = configurationCmds()
    log.debug("Sending configuration: $cmds")
	return cmds
	}

//-------------------------------------------------------------------------------------------------------------------------

def parseStatus(dev, stat, lvl, label) {									// called/passed from webCore or SmartApp	
	label = "${label.replaceAll(' ','_')}"									// replace spaces in label for later parsing
	if (stat=='on') { 
        if (lvl >= 50) stat=2 							     			    // 2 = on
    	else stat=1															// 1 = dim
        }
    else stat=0																// 0 = off	
    def cmds = "${dev}:${stat}:${label}:Status"							    // device ID : status : level : label 
    sendEthernet(cmds)														// send to Controller via http parse method
	}
    
//-------------------------------------------------------------------------------------------------------------------------       

def installed() {
	log.debug "Installing"
	initialize()
	}
    
def updated() {	
	}    

def initialize() {
	log.debug "Initializing"
	sendEvent(name: "numberOfButtons", value: 12)
    sendEvent(name: "WhosWhatsit", value: 42)
    //createChildDevices()
	}
    
private void createChildDevices() {
	def children = getChildDevices()
	log.debug "device has ${children.size()} children"
    }