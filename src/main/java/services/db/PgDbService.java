package services.db;

import lombok.Data;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Data
public class PgDbService {

    final String url;
    final String user;
    final String pass;

    public Statement getPgStatement() {
        try {
            return DriverManager.getConnection(url, user, pass).createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
