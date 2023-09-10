package com.project.passwordmanager.helper;

import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PasswordHelper {
    public static String generateRandomStrongPassword() {
        Stream<Character> passwordStream =
                Stream.concat(getRandomSpecialCharacters(2),
                Stream.concat(getRandomNumbers(2),
                        Stream.concat(getRandomAlphabets(3, true),
                                getRandomAlphabets(3, false))));

        List<Character> characterList = passwordStream.collect(Collectors.toList());
        Collections.shuffle(characterList);

        StringBuilder stringBuilder = new StringBuilder();
        characterList.forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    public static byte[] generateRandomBytes() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static IvParameterSpec generateIV(byte[] iv) {
        return new IvParameterSpec(iv);
    }

    //==================================PRIVATE METHOD=================================================


    private static Stream<Character> getRandomSpecialCharacters(int count) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 45);
        return specialChars.mapToObj(data-> (char)data);
    }

    private static Stream<Character> getRandomAlphabets(int count, boolean isSmall) {
        Random random = new SecureRandom();
        IntStream specialChars = isSmall ?
                random.ints(count, 97, 122):
                random.ints(count, 65, 90);
        return specialChars.mapToObj(data-> (char)data);
    }

    private static Stream<Character> getRandomNumbers(int count) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 48, 57);
        return specialChars.mapToObj(data-> (char)data);
    }

}
