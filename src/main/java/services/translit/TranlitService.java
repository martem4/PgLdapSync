package services.translit;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TranlitService {
    private final char[] rus = new char[] {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
            'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
            'х', 'ц', 'ч', 'ш', 'щ', 'ы', 'э', 'ю', 'я'};

    private final String[] lat = new String[] {"a", "b", "v", "g", "d", "e", "jo", "zh", "z",
            "i", "j", "k", "l", "m", "n", "o", "p", "r", "s",
            "t", "u", "f", "h", "c", "ch", "sh", "w", "y", "e",
            "ju", "ja"};

    public String translitChar(char c) {
        for (int i = 0; i < rus.length; i++) {
            if (c == rus[i]) {
                return lat[i];
            }
        }
        return null;
    }

    public String translitString(String word) {
        StringBuffer sb = new StringBuffer();
        if (word.length() > 0) {
            char[] chars = word.toCharArray();
            for (char c : chars) {
                String res = translitChar(c);
                if (res != null) {
                    sb.append(res);
                }
            }
        }
        return sb.toString();
    }

}
