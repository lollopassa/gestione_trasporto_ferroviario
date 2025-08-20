package domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {

    private String nome;
    private String cognome;
    private LocalDate dataServ;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String matricola, marca, modello;

    public Turno() {}

    public Turno(String nome, String cognome, LocalDate dataServ,
                 LocalTime oraInizio, LocalTime oraFine,
                 String matricola, String marca, String modello) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataServ = dataServ;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.matricola = matricola;
        this.marca = marca;
        this.modello = modello;
    }

    /* getter & setter */
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public LocalDate getDataServ() { return dataServ; }
    public void setDataServ(LocalDate dataServ) { this.dataServ = dataServ; }

    public LocalTime getOraInizio() { return oraInizio; }
    public void setOraInizio(LocalTime oraInizio) { this.oraInizio = oraInizio; }

    public LocalTime getOraFine() { return oraFine; }
    public void setOraFine(LocalTime oraFine) { this.oraFine = oraFine; }

    public String getMatricola() { return matricola; }
    public void setMatricola(String matricola) { this.matricola = matricola; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    @Override
    public String toString() {
        return nome + " " + cognome + " – " + dataServ;
    }
}
