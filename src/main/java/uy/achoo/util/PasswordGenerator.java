package uy.achoo.util;

/**
 * Created by Mathias on 10/5/15.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase encargada de generar un password
 **/

public class PasswordGenerator {

    /**
     * Metodo que genera un password aleatorio de largo n
     **/
    public static String generatePassword(int n) {
        Random rd = new Random();

        char lowerChars[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char upperChars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char numbers[] = "0123456789".toCharArray();
        char specialChars[] = "!$%?*.,:;#&\\\"\'@".toCharArray();
        List<Character> pwd = new ArrayList<>();
        for (int g = 0; g < 4; g++) {
            for (int z = 0; z < 1; z++) {
                if (g == 0) {
                    pwd.add(numbers[rd.nextInt(numbers.length)]);
                } else if (g == 1) {
                    pwd.add(lowerChars[rd.nextInt(lowerChars.length)]);
                } else if (g == 2) {
                    pwd.add(upperChars[rd.nextInt(upperChars.length)]);
                } else if (g == 3) {
                    pwd.add(specialChars[rd.nextInt(specialChars.length)]);
                }
            }
            if (pwd.size() == n) {
                break;
            }
            if (g + 1 == 4) {
                g = (int) Math.random() * 5;

            }
        }
        StringBuilder password = new StringBuilder();
        Collections.shuffle(pwd);
        for (int c = 0; c < pwd.size(); c++) {
            password.append(pwd.get(c));
        }
        return password.toString();
    }

}
