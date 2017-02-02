package uk.ac.ox.ndm.lcs

import grails.core.GrailsApplication
import org.labkey.remoteapi.*
import org.labkey.remoteapi.security.EnsureLoginCommand

class LabkeyConnectionService {

    GrailsApplication grailsApplication

    private Connection connection

    void initialise() {
        Map connectionDetails = grailsApplication.config.labkey.connection

        String protocol = connectionDetails.secure ? 'https' : 'http'
        String host = connectionDetails.host
        String port = connectionDetails.port ? ':' + connectionDetails.port : ''
        String basePath = connectionDetails.basePath ?: ''

        CredentialsProvider credentialsProvider = new GuestCredentialsProvider()
        if (connectionDetails.authentication.apiKey) {
            credentialsProvider = new ApiKeyCredentialsProvider(connectionDetails.authentication.apiKey)
        }
        else if (connectionDetails.authentication.user) {
            credentialsProvider = new BasicAuthCredentialsProvider(connectionDetails.authentication.user, connectionDetails.authentication.password)
        }

        connection = new Connection("${protocol}://${host}${port}/${basePath}", credentialsProvider)
        execute(new EnsureLoginCommand())
    }

    public <C extends Command,R extends CommandResponse> R execute(C command, String path = null) throws IOException, CommandException{
        command.execute(connection, path)
    }


}
