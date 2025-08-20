package domain;

public class Tratta {
    private String depNomeStazione;
    private String arrNomeStazione;
    private int    km;
    private int    numTreniOperativi;

    public Tratta() { }

    public Tratta(String depNomeStazione, String arrNomeStazione,
                  int km, int numTreniOperativi) {
        this.depNomeStazione   = depNomeStazione;
        this.arrNomeStazione   = arrNomeStazione;
        this.km                = km;
        this.numTreniOperativi = numTreniOperativi;
    }

    public String getDepNomeStazione() { return depNomeStazione; }
    public void setDepNomeStazione(String depNomeStazione) { this.depNomeStazione = depNomeStazione; }
    public String getArrNomeStazione() { return arrNomeStazione; }
    public void setArrNomeStazione(String arrNomeStazione) { this.arrNomeStazione = arrNomeStazione; }
    public int getKm() { return km; }
    public void setKm(int km) { this.km = km; }
    public int getNumTreniOperativi() { return numTreniOperativi; }
    public void setNumTreniOperativi(int numTreniOperativi) { this.numTreniOperativi = numTreniOperativi; }

    @Override
    public String toString() {
        return String.format("%s → %s (%dkm)", depNomeStazione, arrNomeStazione, km);
    }
}
