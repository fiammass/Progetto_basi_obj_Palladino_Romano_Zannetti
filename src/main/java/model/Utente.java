package model;

/**
 * Rappresenta un Utente del sistema.
 * Ogni utente ha un username, una password e 3 bacheche personali nelle quali può creare, modificare ed eliminare i todo
 */

public class Utente {

    private final String login;
    private String password;
    private Bacheca bacheca1;
    private Bacheca bacheca2;
    private Bacheca bacheca3;


    /**
     * Costruttore della Classe Utente
     * @param login
     * @param password
     */
    public Utente( String login ,String password){
        this.login = login;
        this.password = password;
        this.bacheca1 = new Bacheca("Università", "Bacheca dedicata all'Università");
        this.bacheca2 = new Bacheca("Lavoro", "Bacheca dedicata al Lavoro");
        this.bacheca3 = new Bacheca("Tempo Libero", "Bacheca dedicata al Tempo Libero");
    }

    /**
     * Restitusice il login dell utente
     * @return
     */

    public String getlogin() {
        return login;
    }

    /**
     * Restituisce la password dell utente
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce la bacheca 1
     * @return
     */

    public Bacheca getBacheca1() {
        return bacheca1;
    }

    /**
     * Imposta la bahcecha 1
     * @param bacheca1
     */
    public void setBacheca1(Bacheca bacheca1) {
        this.bacheca1 = bacheca1;
    }

    /**
     * Restituisce la bacheca 3
     * @return
     */
    public Bacheca getBacheca3() {
        return bacheca3;
    }

    /**
     * Imposta la bahcecha 3
     * @param bacheca3
     */
    public void setBacheca3(Bacheca bacheca3) {
        this.bacheca3 = bacheca3;
    }

    /**
     * Restituisce la bacheca 2
     * @return
     */
    public Bacheca getBacheca2() {
        return bacheca2;
    }

    /**
     * Imposta la bahcecha 2
     * @param bacheca2
     */
    public void setBacheca2(Bacheca bacheca2) {
        this.bacheca2 = bacheca2;
    }

    /**
     * imposta la password dell utente
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * restituisce il login dell utente
     * @return
     */
    public String getLogin() {
        return login;
    }

    public Bacheca getBachecaById(int id) {
        if (bacheca1 != null && bacheca1.getIdBa() == id) return bacheca1;
        if (bacheca2 != null && bacheca2.getIdBa() == id) return bacheca2;
        if (bacheca3 != null && bacheca3.getIdBa() == id) return bacheca3;
        return null;
    }
}
