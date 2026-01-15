package model;

/**
 * Rappresenta una singola voce (sottoattività) all'interno di una CheckList associata a un ToDo.
 * Ogni attività ha un nome e uno stato (completato o non completato).
 */
public class CheckList {

    private Integer idChecklist;
    private String nome;
    private Boolean stato;      // true = completato, false = non completato
    private Integer idToDo;     // Riferimento al ToDo padre

    /**
     * Costruttore completo per oggetti recuperati dal Database.
     *
     * @param nome  Il testo dell'attività.
     * @param stato Lo stato di completamento.
     */
    public CheckList(String nome, Boolean stato) {
        this.nome = nome;
        this.stato = stato;
    }

    /**
     * Costruttore per nuove attività.
     * Lo stato viene impostato di default a false (non completato).
     *
     * @param nome Il testo dell'attività.
     */
    public CheckList(String nome) {
        this.nome = nome;
        this.stato = false; // Default richiesto dalla traccia
    }

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID univoco della checklist.
     * @return L'ID intero.
     */
    public Integer getIdChecklist() {
        return idChecklist;
    }

    /**
     * Imposta l'ID della checklist.
     * @param idChecklist Il nuovo ID.
     */
    public void setIdChecklist(Integer idChecklist) {
        this.idChecklist = idChecklist;
    }

    /**
     * Restituisce il testo dell'attività.
     * @return Il nome dell'attività.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il testo dell'attività.
     * @param nome Il nuovo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce lo stato di completamento.
     * @return True se completato, False altrimenti.
     */
    public Boolean getStato() {
        return stato;
    }

    /**
     * Imposta lo stato di completamento.
     * @param stato True per completare, False per riaprire.
     */
    public void setStato(Boolean stato) {
        this.stato = stato;
    }

    /**
     * Restituisce l'ID del ToDo a cui questa attività appartiene.
     * @return L'ID del ToDo padre.
     */
    public Integer getIdToDo() {
        return idToDo;
    }

    /**
     * Imposta l'ID del ToDo padre.
     * @param idToDo L'ID del ToDo.
     */
    public void setIdToDo(Integer idToDo) {
        this.idToDo = idToDo;
    }

    @Override
    public String toString() {
        return nome + (stato ? " [X]" : " [ ]");
    }
}