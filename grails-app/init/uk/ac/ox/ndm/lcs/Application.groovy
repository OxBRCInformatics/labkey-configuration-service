package uk.ac.ox.ndm.lcs

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    void doWithApplicationContext() {
        LabkeyConnectionService labkeyConnectionService = applicationContext.getBean(LabkeyConnectionService)
        labkeyConnectionService.initialise()
    }
}