package uk.ac.ox.ndm.lcs.principal

class LabkeyUser {

    String email
    boolean sendEmail = true

    static hasMany = [groups: LabkeyGroup]

    static constraints = {
        groups nullable: true
        email email: true
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        LabkeyUser that = (LabkeyUser) o

        if (email != that.email) return false

        return true
    }

    int hashCode() {
        return (email != null ? email.hashCode() : 0)
    }
}
