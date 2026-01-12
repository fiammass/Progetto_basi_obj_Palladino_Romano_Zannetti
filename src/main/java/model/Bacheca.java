package model;

import java.util.ArrayList;


/**
 * Rappresenta una bacheca personale di un Utente.
 * Ogni utente possiede tre bahcehca (numerate da 1 a 3),
 * nelle quali puo inserire , organizzare e spostare i propri Todo.
 * La bacheca mantiene una lista di ToDo a esso associati.
 *
 */

public class Bacheca {

    private Integer idBa;
    private String titolo;
    private String descrizione;
    private ArrayList<ToDo> todos;

    /**
     * Costruttore della classe Bacheca
     *
     * @param titolo
     * @param descrizione
     */
    public Bacheca(String titolo, String descrizione) {
        ArrayList<ToDo> tmp = new ArrayList<>();
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.todos = tmp;
    }

    /**
     * Restituisce il titolo della Bacheca
     *
     * @return
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la descriszione della Bacheca
     *
     * @return
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il titolo della bacheca
     *
     * @param titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Imposta la descrizione della bacheca
     *
     * @param descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * restituisce l id della bahceca
     *
     * @return
     */
    public Integer getIdBa() {
        return idBa;
    }

    /**
     * Imposta l id della bahcheca
     *
     * @param idBa
     */
    public void setIdBa(Integer idBa) {
        this.idBa = idBa;
    }

    /**
     * restituisce l array di todo
     *
     * @return
     */
    public ArrayList<ToDo> getTodos() {
        return todos;
    }

    /**
     * imposta l array di todo
     *
     * @param todos
     */
    public void setTodos(ArrayList<ToDo> todos) {
        this.todos = todos;
    }

    /**
     * aggiunge un todo alla bahcehca
     *
     * @param todo
     */

    public void addTodo(ToDo todo) {
        if (this.todos == null) {
            this.todos = new ArrayList<>();
        }
        this.todos.add(todo);
    }
}
