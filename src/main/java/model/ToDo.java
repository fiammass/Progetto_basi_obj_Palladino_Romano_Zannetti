package model;

import java.awt.*;
import java.util.Date;

public class ToDo {

    private Integer idToDo;
    private String titolo;
    private String descrizione;
    private Date datescadenza;
    private String url;
    private Image image;
    private Color color;
    private Boolean completato ;

    public ToDo(String titolo,String descrizione,Date datescadenza, String url , Image image, Color color, Boolean completato){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.datescadenza = datescadenza;
        this.url = url;
        this.image = image;
        this.color = color;
        this.completato = false;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Date getDatescadenza() {
        return datescadenza;
    }

    public String getUrl() {
        return url;
    }

    public Image getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }

    public Boolean getCompletato() {
        return completato;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDatescadenza(Date datescadenza) {
        this.datescadenza = datescadenza;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public Integer getIdToDo() {
        return idToDo;
    }

    public void setIdToDo(Integer idToDo) {
        this.idToDo = idToDo;
    }
}


