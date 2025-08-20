package domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Tariffa {

    private String marca;
    private String modello;
    private String nomeClasse;           // ENUM PRIMA/SECONDA
    private String depNomeStazione;      // FK -> stazione.nome_stazione
    private String arrNomeStazione;      // FK -> stazione.nome_stazione
    private BigDecimal prezzo;           // DECIMAL(8,2) CHECK > 0

    public Tariffa() {}

    public Tariffa(String marca, String modello, String nomeClasse,
                   String depNomeStazione, String arrNomeStazione,
                   BigDecimal prezzo) {
        this.marca = marca;
        this.modello = modello;
        this.nomeClasse = nomeClasse;
        this.depNomeStazione = depNomeStazione;
        this.arrNomeStazione = arrNomeStazione;
        this.prezzo = prezzo;
    }

    // Getter/Setter
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public String getNomeClasse() { return nomeClasse; }
    public void setNomeClasse(String nomeClasse) { this.nomeClasse = nomeClasse; }

    public String getDepNomeStazione() { return depNomeStazione; }
    public void setDepNomeStazione(String depNomeStazione) { this.depNomeStazione = depNomeStazione; }

    public String getArrNomeStazione() { return arrNomeStazione; }
    public void setArrNomeStazione(String arrNomeStazione) { this.arrNomeStazione = arrNomeStazione; }

    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }

    @Override
    public String toString() { return nomeClasse + " " + prezzo + "€"; }

    // (opzionale) equals/hashCode sulla PK
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tariffa)) return false;
        Tariffa that = (Tariffa) o;
        return Objects.equals(marca, that.marca)
                && Objects.equals(modello, that.modello)
                && Objects.equals(nomeClasse, that.nomeClasse)
                && Objects.equals(depNomeStazione, that.depNomeStazione)
                && Objects.equals(arrNomeStazione, that.arrNomeStazione);
    }
    @Override
    public int hashCode() {
        return Objects.hash(marca, modello, nomeClasse, depNomeStazione, arrNomeStazione);
    }
}
