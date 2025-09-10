package domain;

import java.util.Objects;

public class Cliente {
    private String cf;
    private String nome;
    private String cognome;


    public Cliente(String cf, String nome, String cognome) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getCf() { return cf; }
    public void setCf(String cf) { this.cf = cf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cf, cliente.cf);
    }

    @Override public int hashCode() {
        return Objects.hash(cf);
    }

    @Override public String toString() {
        return "cliente{" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}
