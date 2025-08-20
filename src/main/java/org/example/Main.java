    package org.example;

    import utility.DBConnection;
    import utility.Scheduler;

    import java.sql.Connection;
    import java.sql.SQLException;

    public class Main {

        public static void main(String[] args) {
            Scheduler.attivaEventSchedulerComeRoot();

            try (Connection c = DBConnection.getConnection("loginuser.properties")) {
                if (c.isValid(1)) {
                    System.out.println("Connessione riuscita al database!");
                } else {
                    System.err.println("Connessione non valida");
                    return;
                }
            } catch (SQLException e) {
                System.err.println("Errore di connessione: " + e.getMessage());
                return;
            }

            view.MainView.start ();
        }
    }
