package model;

/**
 * Rappresenta un Utente del sistema.
 * Ogni utente ha un username, una password e 3 bacheche personali nelle quali può creare, modificare ed eliminare i todo
 */

public class Utente {

    private Integer idUtente;
    private String login;
    private String password;
    private Bacheca bacheca1;
    private Bacheca bacheca2;
    private Bacheca bacheca3;


    public Utente( String login ,String password){
        this.login = login;
        this.password = password;
        this.bacheca1 = new Bacheca("Università", "Bacheca dedicata all'Università");
        this.bacheca2 = new Bacheca("Lavoro", "Bacheca dedicata al Lavoro");
        this.bacheca3 = new Bacheca("Tempo Libero", "Bacheca dedicata al Tempo Libero");
    }

    public String getlogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Bacheca getBacheca1() {
        return bacheca1;
    }

    public void setBacheca1(Bacheca bacheca1) {
        this.bacheca1 = bacheca1;
    }

    public Bacheca getBacheca3() {
        return bacheca3;
    }

    public void setBacheca3(Bacheca bacheca3) {
        this.bacheca3 = bacheca3;
    }

    public Bacheca getBacheca2() {
        return bacheca2;
    }

    public void setBacheca2(Bacheca bacheca2) {
        this.bacheca2 = bacheca2;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public String getLogin() {
        return login;
    }
}
