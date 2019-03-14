import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import lombok.extern.slf4j.Slf4j;
import model.AcsUser;
import model.LdapUser;
import services.db.AcsDbService;
import services.ldap.LdapService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Slf4j
public class PgLdapSync {

    private final static String appPropertiesFilePath = "app.properties";

        public static void main (String[] args) throws SQLException, IOException,
            NoSuchAlgorithmException, LDAPException {
        Properties appProps = readAppProperties();

            final String ldapPort = appProps.getProperty("ldap.port");
            final String ldapVersion = appProps.getProperty("ldap.version");
            final String ldapHost = appProps.getProperty("ldap.host");
            final String ldapUserLogin = appProps.getProperty("ldap.user");
            final String ldapUserPass = appProps.getProperty("ldap.userpass");
            final String ldapBase = appProps.getProperty("ldap.base");
            final String entryFilterLogin = appProps.getProperty("ldap.entryfilterlogin");
            final String accessDriverClassName = appProps.getProperty("access.driverclassname");
            final String accessDbUrl = appProps.getProperty("access.dburl");
            final String accessConnectionProperties = appProps.getProperty("access.connectionproperties");
            final String accessUser = appProps.getProperty("access.user");
            final String accessPass = appProps.getProperty("access.pass");


        AcsDbService acsDbService = new AcsDbService();
        LdapService ldapService = new LdapService();

        //connect to access database
        Connection acsCon = acsDbService.getAccessConnection(accessDriverClassName, accessDbUrl,
                accessConnectionProperties, accessUser, accessPass);

        //connect to ldap service
        LDAPConnection ldapConnection = ldapService.getLdapConnection(ldapHost, Integer.parseInt(ldapPort),
                Integer.parseInt(ldapVersion), ldapUserLogin, ldapUserPass);

        List<AcsUser> acsUserList = acsDbService.getAccessUserList(acsCon);
        for (AcsUser acsUser : acsUserList) {
            LdapUser ldapUser = ldapService.getLdapUserByLogin(ldapConnection, ldapBase, entryFilterLogin,
                    acsUser.getNameAccess().toLowerCase());
            if (ldapUser != null) {
                if (acsUser.getPass() != null) {
                    if (!acsUser.getBase64Md5Pass().trim().equals(ldapUser.getUserPassword().trim())) {
                        ldapService.updateLdapUserPasswordByUid(ldapConnection, ldapBase, entryFilterLogin,
                                acsUser.getNameAccess().toLowerCase(), acsUser);
                        log.info("updated password for user: " + acsUser.getNameAccess().toLowerCase());
                    }
                }
            }
            else {
                ldapService.createLdapUser(ldapConnection, ldapBase, entryFilterLogin, acsUser);
                log.info("created new user: " + acsUser.getNameAccess().toLowerCase());
            }
        }
        acsDbService.closeConnection(acsCon);
        ldapService.closeLdapConnection(ldapConnection);
    }

    private static Properties readAppProperties() throws IOException {
        InputStream inputStream = PgLdapSync.class.getClassLoader().getResourceAsStream(appPropertiesFilePath);
        Properties properties = new Properties ();
        properties.load(inputStream);
        return properties;
    }
}
