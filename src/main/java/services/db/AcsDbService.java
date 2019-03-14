package services.db;

import model.AcsUser;
import org.apache.commons.dbcp.BasicDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcsDbService {
    //private final String driverClassName = "com.inet.jj.cli.JJDriver";
    //private final String accessDbUrl = "jdbc:inetjj:172.172.173.230;DataRef";
    //private final String connectionProperties = "charSet=windows-1251";
    //private final String accessPass = "LocUnLoclLockE";
    //private final String accessUser = "ServiceUnlocker";

    public Connection getAccessConnection(final String driverClassName, final String accessDbUrl,
                                         final String connectionProperties, final String accessUser,
                                         final String accessPass) throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(accessDbUrl);
        dataSource.setConnectionProperties(connectionProperties);
        dataSource.setUsername(accessUser);
        dataSource.setPassword(accessPass);
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public List<AcsUser> getAccessUserList(Connection connection) throws SQLException {
        ArrayList<AcsUser> acsUserList = new ArrayList<>();
        final String query = "SELECT DISTINCT id" +
                                        " ,ТабНом as tabnum" +
                                        " ,Pass" +
                                        " ,FIO" +
                                        " ,NameAccess " +
                                        " ,BlockedPass" +
                            " FROM tblUsers " +
                            " WHERE BlockedPass = FALSE " +
                            "   AND NOT NameAccess IS NULL " +
                            "   AND isGroup = FALSE " +
                            " ORDER BY NameAccess";

        ResultSet resultSet = connection.prepareStatement(query).executeQuery();
        if (resultSet != null) {
            while (resultSet.next()) {
                acsUserList.add(new AcsUser(resultSet.getInt("id"),
                                            resultSet.getInt("tabnum"),
                                            resultSet.getString("NameAccess").toLowerCase(),
                                            (resultSet.getString("FIO")==null)?"None":
                                                    resultSet.getString("FIO"),
                                            (resultSet.getString("Pass")==null)?"111111":
                                                    resultSet.getString("Pass"),
                                            true
                                            ));
            }
            return acsUserList;
        }
        return null;
    }
}
