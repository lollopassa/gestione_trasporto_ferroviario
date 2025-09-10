package domain;

import java.util.Objects;

public class Stazione {
    private String nome;
    private String citta;
    private String provincia;


    public Stazione(String nome, String citta, String provincia) {
        this.nome = nome;
        this.citta = citta;
        this.provincia = provincia;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stazione)) return false;
        Stazione that = (Stazione) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(citta, that.citta) &&
                Objects.equals(provincia, that.provincia);
    }

    @Override public int hashCode() {
        return Objects.hash(nome, citta, provincia);
    }

    @Override public String toString() {
        return "stazione{" +
                "nome='" + nome + '\'' +
                ", citta='" + citta + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }
}
