package app.mvc.model;
public class Coup {

    private int numColonneJ1;
    private int numColonneJ2;

    public Coup(int numColonneJ1) {
        this.numColonneJ1 = numColonneJ1;
    }

    public int getCoupJ1() {
        return numColonneJ1;
    }

    public int getCoupJ2() {
        return numColonneJ2;
    }

    public void setCoupJ1(int numColonneJ1) {
        this.numColonneJ1 = numColonneJ1;
    }

    public void setCoupJ2(int numColonneJ2) {
        this.numColonneJ2 = numColonneJ2;
    }

    @Override
    public String toString() {
        return "Coup [J1: Colonne " + numColonneJ1 + ", J2: Colonne " + numColonneJ2 + "]";
    }
}

