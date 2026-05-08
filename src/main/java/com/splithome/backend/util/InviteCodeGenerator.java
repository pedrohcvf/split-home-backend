package com.splithome.backend.util;

import java.security.SecureRandom;

public class InviteCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom random = new SecureRandom();

    public static String generate(){
        StringBuilder code = new StringBuilder("SPLIT-");

        for (int i = 0; i < 6; i++){
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

}
