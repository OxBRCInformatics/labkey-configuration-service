package uk.ac.ox.ndm.lcs.principal

class Principal {

    static hasMany = [users: LabkeyUser, groups: LabkeyGroup]

    static constraints = {
        users nullable: true
        groups nullable: true
    }
}
