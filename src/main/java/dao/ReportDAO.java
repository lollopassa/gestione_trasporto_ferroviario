package dao;

import exception.DAOException;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {

    private static final String PROPS = "personaleuser.properties";

    public String getReportSettimanalePersonale(String cf) throws DAOException {

        String sql = "SELECT settimana, ore_lavorate " +
                "FROM   report_settimanale_personale " +
                "WHERE  cf_personale = ?";

        StringBuilder sb = new StringBuilder();

        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cf);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return "Nessun report disponibile.";
                }
                do {
                    sb.append("Settimana ")
                            .append(rs.getInt("settimana"))
                            .append(" – ore lavorate: ")
                            .append(rs.getInt("ore_lavorate"))
                            .append(System.lineSeparator());
                } while (rs.next());
            }

        } catch (SQLException e) {
            throw new DAOException("Errore generazione report: " + e.getMessage());
        }
        return sb.toString();
    }
}
