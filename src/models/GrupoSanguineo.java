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
    
    public static GrupoSanguineo fromString(String text) {
        if (text == null) return null;

        
        try {
            
            return GrupoSanguineo.valueOf(text);
        } catch (IllegalArgumentException e) {
           
        }

       
        for (GrupoSanguineo g : values()) {
            if (g.simbolo.equalsIgnoreCase(text)) {
                return g;
            }
        }
        
        
        throw new IllegalArgumentException("No hay una constante en el Enum GrupoSanguineo para el texto: '" + text + "'");
    }
    
    
    
}
