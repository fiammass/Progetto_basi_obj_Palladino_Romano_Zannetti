package dao;

import model.CheckList;
import java.util.List;

public interface ChecklistDao {

    /**
     * Salva una nuova attività della CheckList nel database.
     * @param attivita L'attività CheckList da salvare.
     * @param idToDo L'ID del ToDo a cui è associata l'attività.
     */
    void salvaCheckListAttivita(CheckList attivita, int idToDo);

    /**
     * Recupera tutte le attività della CheckList associate a un ToDo.
     * @param idToDo L'ID del ToDo.
     * @return Una lista di attività CheckList.
     */
    List<CheckList> getCheckListByToDoId(int idToDo);

    /**
     * Aggiorna lo stato di completamento di una singola attività CheckList.
     * @param idCheckList L'ID dell'attività CheckList.
     * @param completato Il nuovo stato.
     */
    void updateStatoCheckList(int idCheckList, boolean completato);

    /**
     * Elimina una singola attività CheckList.
     * @param idCheckList L'ID dell'attività CheckList da eliminare.
     */
    void eliminaCheckListAttivita(int idCheckList);

}