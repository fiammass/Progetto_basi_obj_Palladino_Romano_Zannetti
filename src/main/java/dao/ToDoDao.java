package dao;

import model.Bacheca;
import model.ToDo;
import model.Utente;

/**
 * Interfaccia DAO che definisce i metodi per la gestione del ToDo.
 * Le operazioni di persistenza principali che possono essere eseguite su un ToDo,
 * indipoendentemente dal database utilizzato.
 */


public interface ToDoDao  {

    /**
     * Metodo per salvare un ToDo.
     * @param todo
     * @param idBacheca
     * @param autore
     */

    void salvaToDo ( ToDo todo , int idBacheca , Utente autore);

    /**
     * Metodo per popolare la Bacheca.
     * @param idBacheca
     * @param bacheca
     * @param autore
     */

    void popolabacheche (int idBacheca , Bacheca bacheca ,  Utente autore);


    /**
     * Metodo per aggiornare un ToDo esistente dopo la sua modifica.
     * @param ToDo
     * @param idTodo
     */

    void updateToDo ( ToDo ToDo , int idTodo);

    /**
     * Metodo per cambaire Bacheca a un ToDo esistente nel DB.
     * @param ToDo
     * @param idBacheca
     * @param idToDo
     */

    void cambiabacheca (ToDo ToDo , int idBacheca, int idToDo);

    /**
     * Metodo per liberare una bacheca completamente.
     * @param idBacheca
     */

    void svuotabahcea (int idBacheca);

    /**
     * Metodo per elimanare un ToDo da una Bacheca.
     * @param ToDo
     * @param idToDo
     */


    void eliminaToDo ( ToDo ToDo , int idToDo);

    /**
     * Metodo per trovare un autore di un ToDo gia esistente nel DB.
     * @param idToDo
     * @return
     */

    String getautore (int idToDo);

    /**
     * Metodo per l utente che ha effetuato un login di cui si popola la bahchea SOLO dei ToDo che sono stait conidvisi con lui , aggiungendoli al model.
     * @param utentecorrente
     * @param bacheca
     * @param numeroBachecaDest
     */

    void popolaToDocondivisi (Utente utentecorrente , Bacheca bacheca , int numeroBachecaDest);




}
