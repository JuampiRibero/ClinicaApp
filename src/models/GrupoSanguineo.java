package models;

public enum GrupoSanguineo {
    A_POS("A+"), A_NEG("A-"), B_POS("B+"), B_NEG("B-"), AB_POS("AB+"), AB_NEG("AB-"), O_POS("O+"), O_NEG("O-");
    
    private final String simbolo;
    
    GrupoSanguineo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    @Override
    public String toString() {
        return simbolo;
    }
}
