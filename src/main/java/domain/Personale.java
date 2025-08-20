// src/main/java/domain/Personale.java
package domain;

public class Personale {

    public enum TipoPersonale { MACCHINISTA, CAPOTRENO }

    private String nome;
    private String cognome;
    private TipoPersonale tipo;

    public Personale() {}

    public Personale(String nome, String cognome, TipoPersonale tipo) {
        this.nome = nome;
        this.cognome = cognome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public TipoPersonale getTipo() {
        return tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setTipo(TipoPersonale tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " (" + tipo + ")";
    }
}
