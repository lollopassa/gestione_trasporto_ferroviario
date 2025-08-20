package dao;

import domain.Role;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class LoginDAO {

    private LoginDAO() {}

    public static Role login(String username, String password) throws SQLException {
        final String sql = "SELECT role FROM credentials WHERE username = ? AND password = ?";
        try (Connection c = DBConnection.getConnection("loginuser.properties");
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Role.valueOf(rs.getString("role"));
                }
                return null;
            }
        }
    }
}
