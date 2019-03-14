import com.novell.ldap.LDAPException;
import services.ldap.LdapService;
import model.LdapUser;
import org.junit.Test;

import java.util.List;

public class LdapServiceTest {

    private final int ldapPort = 389;
    private final int ldapVersion = 2;
    private final String ldapHost = "172.172.173.18";
    private final String ldapUser = "cn=Manager,dc=century,dc=local";
    private final String ldapUserPass = "panasonic";
    private final String ldapBase = "ou=people,dc=century,dc=local";
    private final String entryFilterLogin = "uid=";

    @Test
    public void testGetLdapUserList() throws LDAPException {
        LdapService ldapService = new LdapService();
        LdapUser ldapUserTest = ldapService.getLdapUserByLogin(ldapService.getLdapConnection(ldapHost, ldapPort,
                ldapVersion, ldapUser, ldapUserPass), ldapBase, entryFilterLogin, "yayakubovskij");
        System.out.println(ldapUserTest.getDisplayName() + " " + ldapUserTest.getUid() + " " +
                ldapUserTest.getUserPassword() );
    }
}
