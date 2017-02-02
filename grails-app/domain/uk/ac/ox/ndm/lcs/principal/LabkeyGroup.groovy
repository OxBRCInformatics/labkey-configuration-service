package uk.ac.ox.ndm.lcs.principal

class LabkeyGroup {

    String name

    static constraints = {
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        LabkeyGroup that = (LabkeyGroup) o

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }
}
