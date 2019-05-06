package Utilities;

import java.util.Scanner;

public class CheckInput {
    static Scanner scanner = new Scanner(System.in);
    public static int getInt() {
        boolean isInt = false;
        int inputToInt = -1;
        while (!isInt) {
            String input = scanner.nextLine();
            try {
                inputToInt = Integer.parseInt(input);
                isInt = true;
            } catch (NumberFormatException e) {
                System.out.println("Given input is not an integer. Try again:");
            }
        }
        return inputToInt;
    }

    public static int checkRange(int lower, int upper) {
        boolean isInt = false;
        int inputToInt = -1;
        while (!isInt) {
            String input = scanner.nextLine();
            try {
                inputToInt = Integer.parseInt(input);
                if (inputToInt >= lower && inputToInt <= upper) {
                    isInt = true;
                } else {
                    System.out.println("The input must be between " + lower + " and " + upper + ". Try again:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Given input is not an integer. Try again:");
            }
        }
        return inputToInt;
    }

    public static Boolean getBoolean() {
        Boolean isBoolean = false;
        Boolean simple = true;
        while (!isBoolean) {
            String input = scanner.nextLine();
            if (input.equals("y")) {
                isBoolean = true;
            } else {
                if (input.equals("n")) {
                    simple = false;
                    isBoolean = true;
                } else {
                    System.out.println("Given input is neither y or n. Try again: ");
                }
            }
        }
        return simple;
    }
}
