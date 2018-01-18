/**
 *  Default settings
 *
 *  Copyright 2018 DCVN
 *
 */
metadata {
	definition (name: "Default settings", namespace: "OurPlace", author: "DCVN") {
		capability "Button"

		command "doSomething"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'button' attribute
	// TODO: handle 'numberOfButtons' attribute

}

// handle commands
def doSomething() {
	log.debug "Executing 'doSomething'"
	// TODO: handle 'doSomething' command
}