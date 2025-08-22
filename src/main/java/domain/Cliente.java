package domain;

import java.util.Objects;

public class Cliente {
    private final String cf; // CHAR(16)

    public Cliente(String cf) {
        this.cf = cf;
    }

    public String getCf() { return cf; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente) o;
        return Objects.equals(cf, c.cf);
    }
    @Override public int hashCode() { return Objects.hash(cf); }
    @Override public String toString() { return "Cliente{cf='" + cf + "'}"; }
}
