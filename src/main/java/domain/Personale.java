package domain;

import java.util.Objects;

public class Personale {
    private String cf;
    private String nome;
    private String cognome;
    private String tipo;


    public Personale(String cf, String nome, String cognome, String tipo) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.tipo = tipo;
    }

    public String getCf() { return cf; }
    public void setCf(String cf) { this.cf = cf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personale)) return false;
        Personale that = (Personale) o;
        return Objects.equals(cf, that.cf);
    }

    @Override public int hashCode() {
        return Objects.hash(cf);
    }

    @Override public String toString() {
        return "personale{" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
