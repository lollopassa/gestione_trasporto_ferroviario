package domain;

import java.math.BigDecimal;

public class Tariffa {

    private String marca, modello;
    private String nomeClasse;
    private String depNomeStazione, depCitta, depProvincia;
    private String arrNomeStazione, arrCitta, arrProvincia;
    private BigDecimal prezzo;

    public Tariffa() {}

    public Tariffa(String marca, String modello, String nomeClasse,
                   String depNomeStazione, String depCitta, String depProvincia,
                   String arrNomeStazione, String arrCitta, String arrProvincia,
                   BigDecimal prezzo) {
        this.marca = marca;
        this.modello = modello;
        this.nomeClasse = nomeClasse;
        this.depNomeStazione = depNomeStazione;
        this.depCitta = depCitta;
        this.depProvincia = depProvincia;
        this.arrNomeStazione = arrNomeStazione;
        this.arrCitta = arrCitta;
        this.arrProvincia = arrProvincia;
        this.prezzo = prezzo;
    }

    /* getter & setter */
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public String getNomeClasse() { return nomeClasse; }
    public void setNomeClasse(String nomeClasse) { this.nomeClasse = nomeClasse; }

    public String getDepNomeStazione() { return depNomeStazione; }
    public void setDepNomeStazione(String s) { this.depNomeStazione = s; }

    public String getDepCitta() { return depCitta; }
    public void setDepCitta(String c) { this.depCitta = c; }

    public String getDepProvincia() { return depProvincia; }
    public void setDepProvincia(String p) { this.depProvincia = p; }

    public String getArrNomeStazione() { return arrNomeStazione; }
    public void setArrNomeStazione(String s) { this.arrNomeStazione = s; }

    public String getArrCitta() { return arrCitta; }
    public void setArrCitta(String c) { this.arrCitta = c; }

    public String getArrProvincia() { return arrProvincia; }
    public void setArrProvincia(String p) { this.arrProvincia = p; }

    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }

    @Override
    public String toString() {
        return nomeClasse + " " + prezzo + "€";
    }
}
