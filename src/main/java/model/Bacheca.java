package model;

import java.util.ArrayList;

public class Bacheca {

    private Integer idBa;
    private String titolo;
    private String descrizione;
    private ArrayList<ToDo> todos;

    public Bacheca(String titolo , String descrizione){
        ArrayList<ToDo> tmp = new ArrayList<>();
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.todos = tmp;
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

    public ArrayList<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(ArrayList<ToDo> todos) {
        this.todos = todos;
    }

    public void addToDo(ToDo t)
    {
        todos.add(t);
    }
}
