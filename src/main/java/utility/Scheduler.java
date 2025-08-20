package utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Scheduler {

    private Scheduler() {
    }

    public static void attivaEventSchedulerComeRoot() {
        try (Connection conn = DBConnection.getConnection("root.properties");
             Statement stmt = conn.createStatement()) {
            stmt.execute("SET GLOBAL event_scheduler = ON");
            System.out.println("Event Scheduler attivato");
        } catch (SQLException e) {
            System.err.println("Errore attivazione event scheduler: " + e.getMessage());
        }
    }
}
