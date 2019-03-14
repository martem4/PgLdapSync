import model.AcsUser;
import org.junit.Test;
import services.db.AcsDbService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class AccessServiceTest {

    private final String driverClassName = "com.inet.jj.cli.JJDriver";
    private final String accessDbUrl = "jdbc:inetjj:172.172.173.230;DataRef";
    private final String connectionProperties = "charSet=windows-1251";
    private final String accessPass = "LocUnLoclLockE";
    private final String accessUser = "ServiceUnlocker";

    @Test
    public void testGetAccessUserList() throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        AcsDbService acsDbService = new AcsDbService();
        List<AcsUser> acsUserList = acsDbService.getAccessUserList(acsDbService.getAccessConnection(driverClassName,
                accessDbUrl, connectionProperties, accessUser, accessPass));

        for (AcsUser acsUser : acsUserList) {
            if (acsUser.getFio() != null) {
                System.out.println("Surname Name: " + acsUser.getSurname() + " " + acsUser.getName());
            }
        }
    }
}
