package com.tistory.dnjsrud.disney.global;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomPassword {

    public String makePassword(int length) {
        int index = 0;
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '$', '`', '~', '!', '@', '$', '!', '%', '*', '#', '^', '?', '&', '-', '_', '=', '+'
        };

        StringBuffer password = new StringBuffer();
        Random random = new Random();

        for(int i = 0; i < length; i++) {
            double rd = random.nextDouble();
            index = (int) (charSet.length * rd);

            password.append(charSet[index]);
        }

        return password.toString();
    }
}
