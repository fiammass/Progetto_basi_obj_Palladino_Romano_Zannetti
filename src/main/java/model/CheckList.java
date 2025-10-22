package model;

public class CheckList {

    private Integer idChecklist;
    private String NomeAttivita;
    private Boolean completato;

    public CheckList(String nome, Boolean completato){
        this.NomeAttivita = nome;
        this.completato = false;
    }

    public String getNomeAttivita() {
        return NomeAttivita;
    }

    public void setNomeAttivita(String nomeAttivita) {
        this.NomeAttivita = nomeAttivita;
    }

    public Boolean getCompletato() {
        return completato;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }

    public Integer getIdChecklist() {
        return idChecklist;
    }

    public void setIdChecklist(Integer idChecklist) {
        this.idChecklist = idChecklist;
    }
}
