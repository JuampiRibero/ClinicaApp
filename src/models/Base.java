package models;

public abstract class Base {
    private Long id;
    private Boolean eliminado;
    
    public Base(Long id, Boolean eliminado){
        this.id = id;
        this.eliminado = eliminado;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean isEliminado() {
        return eliminado;
    }
    
    public Base() {}
    
}