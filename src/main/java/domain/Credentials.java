package domain;


public class Credentials {

    private String username;
    private String password;   // hash (preferibile) o plain-text per test
    private Role   role;

    public Credentials() { }

    public Credentials(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    /* ---------- getter / setter ---------- */

    public String getUsername()        { return username; }
    public void   setUsername(String u){ this.username = u; }

    public String getPassword()        { return password; }
    public void   setPassword(String p){ this.password = p; }

    public Role   getRole()            { return role; }
    public void   setRole(Role r)      { this.role = r; }

    @Override
    public String toString() {
        return username + " [" + role + "]";
    }
}
