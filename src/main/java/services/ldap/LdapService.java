package services.ldap;

import com.novell.ldap.*;
import model.AcsUser;
import model.LdapUser;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class LdapService {

    //private final String ldapBase = "ou=people,dc=century,dc=local";
    //private final String entryFilter = "employeeNumber=";
    //private final String entryFilterLogin = "uid=";
    private String[] objectClass = new String[] {"top", "inetOrgPerson", "posixAccount"};
    public LDAPConnection getLdapConnection(String ldapHost, int ldapPort, int ldapVersion, String ldapUser,
                                            String ldapPass) throws LDAPException {
        LDAPConnection ldapCon = new LDAPConnection();
        ldapCon.connect(ldapHost, ldapPort);
        ldapCon.bind(ldapVersion, ldapUser, ldapPass.getBytes());
        return ldapCon;
    }

    public void closeLdapConnection(LDAPConnection ldapConnection) throws LDAPException {
        ldapConnection.disconnect();
    }

    public LdapUser getLdapUserByLogin(LDAPConnection ldapCon, final String ldapBase, final String entryFilter,
                                           final String login) throws LDAPException {
        LDAPSearchResults ldapSearchResult = ldapCon.search(ldapBase, LDAPConnection.SCOPE_ONE, entryFilter+login,
                null, false);

                while(ldapSearchResult.hasMore()) {
                    LDAPEntry ldapEntry = ldapSearchResult.next();
                        return new LdapUser((ldapEntry.getAttribute("displayName") == null)?"0":
                                (ldapEntry.getAttribute("displayName")==null)? "" :
                                        ldapEntry.getAttribute("displayName").getStringValue(),
                                ldapEntry.getAttribute("uid").getStringValue(),
                                (ldapEntry.getAttribute("mail") == null) ? "0" :
                                        ldapEntry.getAttribute("mail").getStringValue(),
                                (ldapEntry.getAttribute("sn")==null)? "" :
                                        ldapEntry.getAttribute("sn").getStringValue(),
                                (ldapEntry.getAttribute("cn")==null)? "" :
                                        ldapEntry.getAttribute("cn").getStringValue(),
                                ldapEntry.getAttribute("givenName").getStringValue(),
                                ldapEntry.getAttribute("userPassword").getStringValue(),
                                (ldapEntry.getAttribute("employeeNumber") == null) ? "0" :
                                        ldapEntry.getAttribute("employeeNumber").getStringValue(),
                                ldapEntry.getDN());
                }
        return null;
    }

    public void createLdapUser(LDAPConnection ldapCon, final String ldapBase, final String entryFilter,
                               AcsUser acsUser) throws UnsupportedEncodingException, NoSuchAlgorithmException,
            LDAPException {
        String dn = "uid=" + acsUser.getNameAccess() + "," + ldapBase;

        LDAPAttributeSet ldapAttributeSet = new LDAPAttributeSet();
        ldapAttributeSet.add(new LDAPAttribute("objectclass", objectClass));
        ldapAttributeSet.add(new LDAPAttribute("sn", acsUser.getSurname()));
        ldapAttributeSet.add(new LDAPAttribute("givenName", acsUser.getName().toLowerCase()));
        ldapAttributeSet.add(new LDAPAttribute("displayName", acsUser.getFio()));
        ldapAttributeSet.add(new LDAPAttribute("cn", acsUser.getName().toLowerCase()));
        ldapAttributeSet.add(new LDAPAttribute("uid", acsUser.getNameAccess().toLowerCase()));
        ldapAttributeSet.add(new LDAPAttribute("userPassword", acsUser.getBase64Md5Pass()));
        ldapAttributeSet.add(new LDAPAttribute("mail", acsUser.getNameAccess().toLowerCase() + "@gk21.ru"));
        ldapAttributeSet.add(new LDAPAttribute("uidNumber", new Integer(acsUser.getTabNum()).toString()));
        ldapAttributeSet.add(new LDAPAttribute("employeeNumber", new Integer(acsUser.getTabNum()).toString()));
        ldapAttributeSet.add(new LDAPAttribute("gidNumber", "0"));
        ldapAttributeSet.add(new LDAPAttribute("homeDirectory", "/home/" +
                acsUser.getNameAccess().toLowerCase()));
        ldapAttributeSet.add(new LDAPAttribute("initials", acsUser.getPatronymic()));
        ldapCon.add(new LDAPEntry(dn, ldapAttributeSet));
    }

    public void updateLdapUserPasswordByUid(final LDAPConnection ldapConnection, final String ldapBase,
                                            final String entryFilter, String uid, AcsUser acsUser)
            throws LDAPException, UnsupportedEncodingException, NoSuchAlgorithmException {
        LdapUser ldapUser = getLdapUserByLogin(ldapConnection, ldapBase, entryFilter, uid);
        if (ldapUser != null) {
            LDAPAttribute ldapAttribute = new LDAPAttribute("userPassword", acsUser.getBase64Md5Pass());
            LDAPModification ldapModification = new LDAPModification(LDAPModification.REPLACE, ldapAttribute);
            ldapConnection.modify(ldapUser.getDn(), ldapModification);
        }
    }
}
