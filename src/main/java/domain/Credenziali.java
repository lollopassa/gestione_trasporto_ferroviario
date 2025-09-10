package domain;

import java.util.Objects;

public class Credenziali {
    private String cf;
    private String username;
    private String password;
    private Ruolo ruolo;


    public Credenziali(String cf, String username, String password, Ruolo ruolo) {
        this.cf = cf;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getCf() { return cf; }
    public void setCf(String cf) { this.cf = cf; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credenziali)) return false;
        Credenziali that = (Credenziali) o;
        return Objects.equals(cf, that.cf);
    }

    @Override public int hashCode() {
        return Objects.hash(cf);
    }

    @Override public String toString() {
        return "credenziali{" +
                "cf='" + cf + '\'' +
                ", username='" + username + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }
}
