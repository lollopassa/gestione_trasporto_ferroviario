package domain;

import java.time.LocalDate;
import java.util.Objects;

public class Prenotazione {
    private Integer idPrenotazione;
    private String cfPasseggero;
    private String nomePasseggero;
    private String cognomePasseggero;
    private LocalDate dataNascitaPasseggero;
    private String ccLast4;
    private LocalDate dataViaggio;
    private String cfPrenotante;
    private Integer idTratta;
    private Integer numeroPosto;
    private Integer idComponente;
    private String idTreno;


    public Prenotazione(Integer idPrenotazione, String cfPasseggero, String nomePasseggero, String cognomePasseggero,
                        LocalDate dataNascitaPasseggero, String ccLast4, LocalDate dataViaggio,
                        String cfPrenotante, Integer idTratta,
                        Integer numeroPosto, Integer idComponente, String idTreno) {
        this.idPrenotazione = idPrenotazione;
        this.cfPasseggero = cfPasseggero;
        this.nomePasseggero = nomePasseggero;
        this.cognomePasseggero = cognomePasseggero;
        this.dataNascitaPasseggero = dataNascitaPasseggero;
        this.ccLast4 = ccLast4;
        this.dataViaggio = dataViaggio;
        this.cfPrenotante = cfPrenotante;
        this.idTratta = idTratta;
        this.numeroPosto = numeroPosto;
        this.idComponente = idComponente;
        this.idTreno = idTreno;
    }

    public Integer getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(Integer idPrenotazione) { this.idPrenotazione = idPrenotazione; }
    public String getCfPasseggero() { return cfPasseggero; }
    public void setCfPasseggero(String cfPasseggero) { this.cfPasseggero = cfPasseggero; }
    public String getNomePasseggero() { return nomePasseggero; }
    public void setNomePasseggero(String nomePasseggero) { this.nomePasseggero = nomePasseggero; }
    public String getCognomePasseggero() { return cognomePasseggero; }
    public void setCognomePasseggero(String cognomePasseggero) { this.cognomePasseggero = cognomePasseggero; }
    public LocalDate getDataNascitaPasseggero() { return dataNascitaPasseggero; }
    public void setDataNascitaPasseggero(LocalDate dataNascitaPasseggero) { this.dataNascitaPasseggero = dataNascitaPasseggero; }
    public String getCcLast4() { return ccLast4; }
    public void setCcLast4(String ccLast4) { this.ccLast4 = ccLast4; }
    public LocalDate getDataViaggio() { return dataViaggio; }
    public void setDataViaggio(LocalDate dataViaggio) { this.dataViaggio = dataViaggio; }
    public String getCfPrenotante() { return cfPrenotante; }
    public void setCfPrenotante(String cfPrenotante) { this.cfPrenotante = cfPrenotante; }
    public Integer getIdTratta() { return idTratta; }
    public void setIdTratta(Integer idTratta) { this.idTratta = idTratta; }
    public Integer getNumeroPosto() { return numeroPosto; }
    public void setNumeroPosto(Integer numeroPosto) { this.numeroPosto = numeroPosto; }
    public Integer getIdComponente() { return idComponente; }
    public void setIdComponente(Integer idComponente) { this.idComponente = idComponente; }
    public String getIdTreno() { return idTreno; }
    public void setIdTreno(String idTreno) { this.idTreno = idTreno; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prenotazione)) return false;
        Prenotazione that = (Prenotazione) o;
        return Objects.equals(idPrenotazione, that.idPrenotazione);
    }

    @Override public int hashCode() {
        return Objects.hash(idPrenotazione);
    }

    @Override public String toString() {
        return "prenotazione{" +
                "idPrenotazione=" + idPrenotazione +
                ", cfPasseggero='" + cfPasseggero + '\'' +
                ", nomePasseggero='" + nomePasseggero + '\'' +
                ", cognomePasseggero='" + cognomePasseggero + '\'' +
                ", dataNascitaPasseggero=" + dataNascitaPasseggero +
                ", ccLast4='" + ccLast4 + '\'' +
                ", dataViaggio=" + dataViaggio +
                ", cfPrenotante='" + cfPrenotante + '\'' +
                ", idTratta=" + idTratta +
                ", numeroPosto=" + numeroPosto +
                ", idComponente=" + idComponente +
                ", idTreno='" + idTreno + '\'' +
                '}';
    }
}
