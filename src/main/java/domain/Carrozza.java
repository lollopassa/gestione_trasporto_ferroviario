package domain;

public class Carrozza {

    private String matricola;
    private String marca;
    private String modello;
    private int    nCarrozza;
    private String nomeClasse;   // PRIMA | SECONDA

    public Carrozza() {}

    public Carrozza(String matricola, String marca, String modello,
                    int nCarrozza, String nomeClasse) {
        this.matricola = matricola;
        this.marca = marca;
        this.modello = modello;
        this.nCarrozza = nCarrozza;
        this.nomeClasse = nomeClasse;
    }

    public String getMatricola() { return matricola; }
    public void setMatricola(String matricola) { this.matricola = matricola; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public int getnCarrozza() { return nCarrozza; }
    public void setnCarrozza(int nCarrozza) { this.nCarrozza = nCarrozza; }

    public String getNomeClasse() { return nomeClasse; }
    public void setNomeClasse(String nomeClasse) { this.nomeClasse = nomeClasse; }

    @Override
    public String toString() {
        return "Carrozza " + nCarrozza + " [" + nomeClasse + "]";
    }
}
