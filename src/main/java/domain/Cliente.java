package domain;

public class Cliente {
    private String nome;
    private String cognome;

    public Cliente() {}
    public Cliente(String nome, String cognome) {
        this.nome = nome; this.cognome = cognome;
    }

    public String getNome()       { return nome; }
    public String getCognome()    { return cognome; }
    public void setNome(String n) { this.nome = n; }
    public void setCognome(String c) { this.cognome = c; }

    @Override public String toString() { return nome + " " + cognome; }
}
