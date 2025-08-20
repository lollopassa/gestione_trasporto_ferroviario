package domain;

public class Personale {

    public enum TipoPersonale { MACCHINISTA, CAPOTRENO }

    private String nome;
    private String cognome;
    private TipoPersonale tipo;
    private String username;

    public Personale() {}

    public Personale(String nome, String cognome, TipoPersonale tipo, String username) {
        this.nome     = nome;
        this.cognome  = cognome;
        this.tipo     = tipo;
        this.username = username;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public TipoPersonale getTipo() { return tipo; }
    public void setTipo(TipoPersonale tipo) { this.tipo = tipo; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return nome + " " + cognome + " (" + tipo + ")";
    }
}
