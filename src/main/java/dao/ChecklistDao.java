package dao;

import model.CheckList;
import java.util.List;

/**
 * Interfaccia per la gestione della persistenza dei dati relativi alla CheckList.
 * Definisce le operazioni CRUD (Create, Read, Update, Delete) per le sottoattività.
 */
public interface ChecklistDao {

    /**
     * Salva una nuova attività della CheckList nel database.
     *
     * @param attivita L'oggetto CheckList da salvare.
     * @param idToDo   L'ID del ToDo a cui associare l'attività.
     */
    void salvaCheckListAttivita(CheckList attivita, int idToDo);

    /**
     * Recupera tutte le attività della CheckList associate a uno specifico ToDo.
     *
     * @param idToDo L'ID del ToDo di cui recuperare la checklist.
     * @return Una lista di oggetti CheckList.
     */
    List<CheckList> getCheckListByToDoId(int idToDo);

    /**
     * Aggiorna lo stato (completato/non completato) di una singola attività.
     *
     * @param idCheckList L'ID dell'attività da aggiornare.
     * @param completato  Il nuovo stato booleano.
     */
    void updateStatoCheckList(int idCheckList, boolean completato);

    /**
     * Elimina definitivamente un'attività dalla CheckList.
     *
     * @param idCheckList L'ID dell'attività da eliminare.
     */
    void eliminaCheckListAttivita(int idCheckList);
}