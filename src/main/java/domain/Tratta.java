package domain;

public class Tratta {

    private String depNomeStazione, depCitta, depProvincia;
    private String arrNomeStazione, arrCitta, arrProvincia;
    private int km;
    private int numTreniOperativi;

    public Tratta() {}

    public Tratta(String depNomeStazione, String depCitta, String depProvincia,
                  String arrNomeStazione, String arrCitta, String arrProvincia,
                  int km, int numTreniOperativi) {
        this.depNomeStazione = depNomeStazione;
        this.depCitta = depCitta;
        this.depProvincia = depProvincia;
        this.arrNomeStazione = arrNomeStazione;
        this.arrCitta = arrCitta;
        this.arrProvincia = arrProvincia;
        this.km = km;
        this.numTreniOperativi = numTreniOperativi;
    }

    /* getter & setter */
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

    public int getKm() { return km; }
    public void setKm(int km) { this.km = km; }

    public int getNumTreniOperativi() { return numTreniOperativi; }
    public void setNumTreniOperativi(int n) { this.numTreniOperativi = n; }

    @Override
    public String toString() {
        return depCitta + " → " + arrCitta + " (" + km + " km)";
    }
}
