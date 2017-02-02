package uk.ac.ox.ndm.lcs.principal

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PrincipalService)
class PrincipalServiceSpec extends Specification {

    def setup() {
        mockDomains(Principal, LabkeyGroup, LabkeyUser)
    }

    void "test verifyAllGroupsForUsersExist no groups or users"() {

        given: 'no users or groups'
        Principal principal = new Principal()

        when:
        principal = service.verifyAllGroupsForUsersExist(principal)

        then:
        !principal.groups

    }

    void "test verifyAllGroupsForUsersExist some groups and no users"() {

        given: 'no users or groups'
        Principal principal = new Principal()
        principal.addToGroups(name: 'test').addToGroups(name:'other')

        when:
        principal = service.verifyAllGroupsForUsersExist(principal)

        then:
        principal.groups.size() == 2

    }

    void "test verifyAllGroupsForUsersExist some groups and some users but no groups assigned"() {

        given: 'no users or groups'
        Principal principal = new Principal()
        principal.addToGroups(name: 'test').addToGroups(name:'other').addToUsers(email: 'test@email.com').addToUsers(email: 'other@email.com')

        when:
        principal = service.verifyAllGroupsForUsersExist(principal)

        then:
        principal.groups.size() == 2

    }

    void "test verifyAllGroupsForUsersExist some groups and some users who use named groups"() {

        given: 'no users or groups'
        Principal principal = new Principal()
        principal.addToGroups(name: 'test').addToGroups(name:'other')
                .addToUsers(new LabkeyUser(email: 'test@email.com').addToGroups(name:'test'))
                .addToUsers(new LabkeyUser(email: 'other@email.com').addToGroups(name: 'test').addToGroups(name: 'other'))

        when:
        principal = service.verifyAllGroupsForUsersExist(principal)

        then:
        principal.groups.size() == 2

    }

    void "test verifyAllGroupsForUsersExist some groups and some users who use named groups and extra groups"() {

        given: 'no users or groups'
        Principal principal = new Principal()
        principal.addToGroups(name: 'test').addToGroups(name:'other')
                .addToUsers(new LabkeyUser(email: 'test@email.com').addToGroups(name:'test').addToGroups(name: 'addme').addToGroups(name: 'andme'))
                .addToUsers(new LabkeyUser(email: 'other@email.com').addToGroups(name: 'test').addToGroups(name: 'other').addToGroups(name: 'addme'))

        when:
        principal = service.verifyAllGroupsForUsersExist(principal)

        then:
        principal.groups.size() == 4

    }
}
