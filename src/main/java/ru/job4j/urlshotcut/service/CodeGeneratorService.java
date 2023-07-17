package ru.job4j.urlshotcut.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGeneratorService {
    private static final String CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = CHARS.length();
    private static final int NUMBER = 12;

    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int num = NUMBER;
        while (num > 0) {
            stringBuilder.append(CHARS.charAt(random.nextInt(LENGTH)));
            num--;
        }
        return stringBuilder.toString();
    }
}
