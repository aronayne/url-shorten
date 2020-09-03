package com.shorten.api.model;

import com.shorten.api.system.Constants;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Map the characters 0..9, a..z , A..Z to their ascii equivalent, then randomly select
 * 7 of these characters.
 */
@Component
public class URLShorten {

    private static final int MAX_CHARACTER_POSITION = 62;
    private final Random random = new Random(); // Random object used to generate random integers
    private char[] characters = new char[62]; // This array is used for character to number
    private int keyLength;

    public URLShorten() {

        this.initialiseCharacterArray();
        this.keyLength = Constants.SHORT_URL_LENGTH;

    }

    /**
     * Initialise the character array ascii value mappings.
     *
     * Ascii Characters from  '0' to '9' , add 48
     * Uppercase characters begin at 65 in ascii table (55+10), end at 90
     * Lowercase characters begin at 97 in ascii table (61+36), end at postion 122
     */
    private void initialiseCharacterArray() {

        for (int i = 0; i < 10; i++) {
            characters[i] = (char) (i + 48);
        }
        for (int i = 10; i < 36; i++) {
            characters[i] = (char) (i + 55);
        }
        for (int i = 36; i < MAX_CHARACTER_POSITION; i++) {
            characters[i] = (char) (i + 61);
        }
    }

    /**
     * Convert a long URL to a shortened String
     *
     * @param longURL
     * @return
     */
    public String shortenURL(String longURL) {
        StringBuilder shortUrl = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            shortUrl.append(characters[random.nextInt(MAX_CHARACTER_POSITION)]);
        }

        return shortUrl.toString();
    }

}