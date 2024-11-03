package com._a.backend.utils;

import java.util.Random;

public class OtpGenerator {
  public static String generate() {
    int otpLength = 6;
    String numbers = "0241739865";
    Random rand = new Random();
    char[] otpChar = new char[otpLength];
    for (int i = 0; i < otpLength; i++) {
      otpChar[i] = numbers.charAt(
          rand.nextInt(numbers.length()));
    }

    return new String(otpChar);
  }
}
