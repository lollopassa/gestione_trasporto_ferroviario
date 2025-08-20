package utility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private DBConnection() {
    }

    public static Connection getConnection(String propertiesFileName) throws SQLException {
        Properties props = new Properties();

        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                throw new IOException("File properties non trovato: " + propertiesFileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new SQLException("Errore nella lettura del file properties: " + e.getMessage(), e);
        }

        String url = props.getProperty("URL");
        String user = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, user, password);
    }
}
