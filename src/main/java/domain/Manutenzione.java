package domain;

import java.time.LocalDateTime;

public class Manutenzione {

    private String matricola;
    private String marca;
    private String modello;
    private LocalDateTime dataEvento;
    private String descrizione;

    public Manutenzione() {}

    public Manutenzione(String matricola, String marca, String modello,
                        LocalDateTime dataEvento, String descrizione) {
        this.matricola = matricola;
        this.marca = marca;
        this.modello = modello;
        this.dataEvento = dataEvento;
        this.descrizione = descrizione;
    }

    public String getMatricola() { return matricola; }
    public void setMatricola(String matricola) { this.matricola = matricola; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public LocalDateTime getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDateTime dataEvento) { this.dataEvento = dataEvento; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    @Override
    public String toString() {
        return matricola + "@" + dataEvento;
    }
}
