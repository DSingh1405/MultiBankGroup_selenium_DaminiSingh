package Assignment02DS;

/**
 * Assignment 2 – Character Frequency Counter
 *
 * Problem:
 * --------
 * Write a program to count how many times each character appears in a string.
 * The output must maintain the same order in which characters first appear
 * in the input string.
 *
 * Additional Requirements : - Added thinking if user input was never given then program should work with default values
 * ------------------------
 * 1. Ask the user to enter a string.
 * 2. If the user does not enter anything (empty input),
 *    then run the program on a list of default sample strings.
 *
 * Assumptions:
 * ------------
 * • The program is case-sensitive. ('A' and 'a' are treated separately.)
 * • Whitespace characters (spaces) are counted as valid characters.
 * • Output order must follow the order of first appearance of characters.
 * • Program accepts user input from console.
 *
 * Approach:
 * ---------
 * • Use LinkedHashMap<Character, Integer> to preserve insertion order.
 * • Iterate through the characters in the string and count frequencies.
 * • Print the results for the user input or the default sample strings.
 *
 */

import java.util.*;

public class CharacterCountInAString {

    /**
     * Counts characters in a string and prints them in order of first appearance.
     *
     * @param input The string for which character frequencies should be counted.
     */
    private static void countCharacters(String input) {

        // LinkedHashMap maintains the order in which characters first appear.
        Map<Character, Integer> charCount = new LinkedHashMap<>();

        // Count occurrences of each character.
        for (char ch : input.toCharArray()) {
            charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);
        }

        // Display results clearly.
        System.out.println("Input: \"" + input + "\"");
        charCount.forEach((character, frequency) ->
                System.out.println(character + " → " + frequency)
        );

        System.out.println("-----------------------------------");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Step 1: Accept input from the user.
        System.out.print("Enter a string (press Enter to use default test strings): ");
        String userInput = scanner.nextLine();

        // Default list of sample strings when user input is not provided.
        List<String> defaultStrings = Arrays.asList(
                "aabbccaa d",
                "Hello World",
                "Java Programming",
                "1122334455"
        );

        // Step 2: Check if the user provided an input.
        if (!userInput.trim().isEmpty()) {
            // User gave their own string → process only that.
            countCharacters(userInput);
        } else {
            // Empty input → use default strings.
            System.out.println("\nNo input provided. Running default test cases...\n");

            for (String sample : defaultStrings) {
                countCharacters(sample);
            }
        }

        scanner.close();
    }
}
