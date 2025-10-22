package model;

public class Utente {

    private Integer idUtente;
    private String login;
    private String password;


    public Utente( String login ,String password){
        this.login = login;
        this.password = password;
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
