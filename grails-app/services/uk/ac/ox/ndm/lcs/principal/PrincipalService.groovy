package uk.ac.ox.ndm.lcs.principal

import org.labkey.remoteapi.CommandException
import org.labkey.remoteapi.security.*
import uk.ac.ox.ndm.lcs.LabkeyConnectionService

class PrincipalService {

    LabkeyConnectionService labkeyConnectionService

    @SuppressWarnings("GroovyUncheckedAssignmentOfMemberOfRawType")
    Principal updateLabkeyWithPrincipals(Principal principal) {

        principal = verifyAllGroupsForUsersExist(principal)

        principal.groups.each {group ->

            try {
                CreateGroupResponse response = labkeyConnectionService.execute(new CreateGroupCommand(group.name))
                log.info "Group ${group.name} successfully created"
            } catch (CommandException ex) {
                if (!ex.responseText.contains('already exists')) log.error("Failed to create group ${group.name}", ex)
            }
        }

        principal.users.each {user ->
            try {
                CreateUserCommand command = new CreateUserCommand(user.email)
                command.sendEmail = user.sendEmail
                CreateUserResponse response = labkeyConnectionService.execute(command)

                log.info "User ${user.email} successfully created"
            } catch (CommandException ex) {
                if (!ex.responseText.contains('already exists')) log.error("Failed to create user ${user.email}", ex)
            }
        }

        /**
         * Still need to add users to groups, however we need group IDs to be able to do this and we cannot find group ids
         */

        principal
    }

    Principal verifyAllGroupsForUsersExist(Principal principal) {
        def listOfGroups = (principal.users.collect {it.groups}.flatten() as Set) - null
        listOfGroups.findAll {!principal.groups.contains(it)}.each {principal.addToGroups(it)}
        principal
    }
}
