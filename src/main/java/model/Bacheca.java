package model;

public class Bacheca {

    private Integer idBa;
    private String titolo;
    private String descrizione;

    public Bacheca(String titolo , String descrizione){
        this.titolo = titolo;
        this.descrizione = descrizione;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione(){
        return descrizione;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getIdBa() {
        return idBa;
    }

    public void setIdBa(Integer idBa) {
        this.idBa = idBa;
    }
}
