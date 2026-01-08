package model;

/**
 * Rappresenta una singola attività da eseguire all'interno della CheckList di un ToDo.
 */
public class CheckList {

    private Integer idChecklist;
    private String nome;        // Corrisponde alla colonna DB 'nome'
    private Boolean stato;      // Corrisponde alla colonna DB 'stato'
    private Integer idToDo;     // Foreign Key per collegarsi al ToDo padre

    /**
     * Costruttore per caricare un'attività CheckList esistente dal database.
     * @param nome Nome dell'attività.
     * @param stato Stato di completamento.
     */
    public CheckList(String nome, Boolean stato){
        this.nome = nome;
        this.stato = stato;
    }

    /**
     * Costruttore per creare una nuova attività (di default non completata).
     * @param nome Nome dell'attività.
     */
    public CheckList(String nome){
        this.nome = nome;
        this.stato = false; // Di default non completato
    }

    /**
     * Restituisce il nome della Checklist
     * @return
     */

    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome della Checklist
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce lo stato della checklist
     * @return
     */
    public Boolean getStato() {
        return stato;
    }

    /**
     * Imposta lo stato della Checklist
     * @param stato
     */
    public void setStato(Boolean stato) {
        this.stato = stato;
    }

    /**
     * Restituisce l id della Checklist
     * @return
     */
    public Integer getIdChecklist() {
        return idChecklist;
    }

    /**
     * Imposta l id della Checklist
     * @param idChecklist
     */
    public void setIdChecklist(Integer idChecklist) {
        this.idChecklist = idChecklist;
    }

    /**
     * Restituisec l id del ToDo
     * @return
     */
    public Integer getIdToDo() {
        return idToDo;
    }

    /**
     * Imposta l id del ToDo
     * @param idToDo
     */
    public void setIdToDo(Integer idToDo) {
        this.idToDo = idToDo;
    }
}