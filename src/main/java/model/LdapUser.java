package model;

import lombok.Data;

@Data
public class LdapUser {
    private final String displayName;
    private final String uid;
    private final String mail;
    private final String sn;
    private final String cn;
    private final String givenName;
    private final String userPassword;
    private final String employeeNumber;
    private final String dn;
}
