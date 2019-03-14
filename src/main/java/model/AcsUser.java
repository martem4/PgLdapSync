package model;

import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Data
public class AcsUser {
    private final int id;
    private final int tabNum;
    private final String nameAccess;
    private final String fio;
    private final String pass;
    private final boolean blockedPass;

    public String getSurname() {
        return getFioPart(1);
    }
    public String getName() {
        return getFioPart(2);
    }
    public String getPatronymic() {
        return getFioPart(3);
    }
    public String getBase64Md5Pass() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        if (pass != null) {
            md.update(pass.getBytes("UTF-8"));
            return "{MD5}" + Base64.getEncoder().encodeToString(md.digest());
        }
        return null;
    }
    private String getFioPart(int num) {
        String[] fioArr = fio.split(" ");
        return (fioArr.length >= num) ? fioArr[num - 1] : "None";
    }
}
