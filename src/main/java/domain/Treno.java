package domain;

import java.util.Objects;

public class Treno {
    private String matricola;
    private Integer idTratta;
    private String nomePart;
    private String cittaPart;
    private String provPart;
    private String nomeArr;
    private String cittaArr;
    private String provArr;


    public Treno(String matricola, Integer idTratta, String nomePart, String cittaPart, String provPart, String nomeArr, String cittaArr, String provArr) {
        this.matricola = matricola;
        this.idTratta = idTratta;
        this.nomePart = nomePart;
        this.cittaPart = cittaPart;
        this.provPart = provPart;
        this.nomeArr = nomeArr;
        this.cittaArr = cittaArr;
        this.provArr = provArr;
    }

    public String getMatricola() { return matricola; }
    public void setMatricola(String matricola) { this.matricola = matricola; }
    public Integer getIdTratta() { return idTratta; }
    public void setIdTratta(Integer idTratta) { this.idTratta = idTratta; }
    public String getNomePart() { return nomePart; }
    public void setNomePart(String nomePart) { this.nomePart = nomePart; }
    public String getCittaPart() { return cittaPart; }
    public void setCittaPart(String cittaPart) { this.cittaPart = cittaPart; }
    public String getProvPart() { return provPart; }
    public void setProvPart(String provPart) { this.provPart = provPart; }
    public String getNomeArr() { return nomeArr; }
    public void setNomeArr(String nomeArr) { this.nomeArr = nomeArr; }
    public String getCittaArr() { return cittaArr; }
    public void setCittaArr(String cittaArr) { this.cittaArr = cittaArr; }
    public String getProvArr() { return provArr; }
    public void setProvArr(String provArr) { this.provArr = provArr; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treno)) return false;
        Treno treno = (Treno) o;
        return Objects.equals(matricola, treno.matricola);
    }

    @Override public int hashCode() {
        return Objects.hash(matricola);
    }

    @Override public String toString() {
        return "treno{" +
                "matricola='" + matricola + '\'' +
                ", idTratta=" + idTratta +
                ", nomePart='" + nomePart + '\'' +
                ", cittaPart='" + cittaPart + '\'' +
                ", provPart='" + provPart + '\'' +
                ", nomeArr='" + nomeArr + '\'' +
                ", cittaArr='" + cittaArr + '\'' +
                ", provArr='" + provArr + '\'' +
                '}';
    }
}
