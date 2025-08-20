package domain;

public class Posto {
    private String matricola;
    private String marca;
    private String modello;
    private int    nCarrozza;
    private String nPosto;

    public Posto() {}

    public Posto(String matricola, String marca, String modello, int nCarrozza, String nPosto) {
        this.matricola = matricola;
        this.marca     = marca;
        this.modello   = modello;
        this.nCarrozza = nCarrozza;
        this.nPosto    = nPosto;
    }

    public String getMatricola() { return matricola; }
    public void setMatricola(String matricola) { this.matricola = matricola; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }
    public int getNCarrozza() { return nCarrozza; }
    public void setNCarrozza(int nCarrozza) { this.nCarrozza = nCarrozza; }
    public String getNPosto() { return nPosto; }
    public void setNPosto(String nPosto) { this.nPosto = nPosto; }
}
