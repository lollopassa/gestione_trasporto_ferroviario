package domain;

public class Classe {

    public enum NomeClasse { PRIMA, SECONDA }

    private NomeClasse nomeClasse;

    public Classe() {}
    public Classe(NomeClasse nomeClasse) { this.nomeClasse = nomeClasse; }

    public NomeClasse getNomeClasse() { return nomeClasse; }
    public void setNomeClasse(NomeClasse nomeClasse) { this.nomeClasse = nomeClasse; }

    @Override public String toString() { return nomeClasse.name(); }
}
