// Imagine a puzzle where words represent numbers, and we need to find a unique digit mapping for
// each letter to satisfy a given equation. The rule is that no two letters can have the same digit, and
// numbers must not have leading zeros.
// Scenario 1: True Case (Valid Equation)
// Equation:
// "STAR" + "MOON" = "NIGHT"
// "STAR"+"MOON"="NIGHT"
// Step 1: Assign Unique Digits to Letters
// S = 8
// T = 4
// A = 2
// R = 5
// M = 7
// O = 1
// N = 9
// I = 6
// G = 3
// H = 0
// Step 2: Convert Words into Numbers
// "STAR" → 8425
// "MOON" → 7119
// "NIGHT" → 96350
// Step 3: Verify the Sum
// 8425 + 7119 = 15544
// Equation: "CODE" + "BUG" = "DEBUG"
// "CODE"+"BUG"="DEBUG"
// Now, let's try to assign unique digits.
// Step 1: Assign Unique Digits to Letters
// C = 1
// O = 0
// D = 5
// E = 7
// B = 3
// U = 9
// G = 2
// Step 2: Convert Words into Numbers
// "CODE" → 1057
// "BUG" → 392
// "DEBUG" → 57392
// Step 3: Verify the Sum
// 1057+392=1449
// Since 1449 ≠ 57392, this mapping is invalid, and no possible digit assignment satisfies the equation.



package Question2;
import java.util.*;

public class Question2b{

    static String[] words = {"STAR", "MOON"};
    static String result = "NIGHT";

    static Map<Character, Integer> charToDigit = new HashMap<>();
    static boolean[] usedDigits = new boolean[10];
    static List<Character> letters = new ArrayList<>();

    public static void main(String[] args) {
        // Collect unique letters
        Set<Character> uniqueLetters = new HashSet<>();
        for (String w : words) {
            for (char c : w.toCharArray()) uniqueLetters.add(c);
        }
        for (char c : result.toCharArray()) uniqueLetters.add(c);

        letters.addAll(uniqueLetters);

        if (letters.size() > 10) {
            System.out.println("Too many unique letters (>10), no solution possible.");
            return;
        }

        if (solve(0)) {
            printSolution();
        } else {
            System.out.println("No solution found.");
        }
    }

    // Backtracking function
    static boolean solve(int index) {
        if (index == letters.size()) {
            return checkSolution();
        }

        char c = letters.get(index);
        for (int d = 0; d <= 9; d++) {
            if (!usedDigits[d]) {
                // Leading letter cannot be zero
                if (d == 0 && isLeadingLetter(c)) continue;

                usedDigits[d] = true;
                charToDigit.put(c, d);

                if (solve(index + 1)) return true;

                // Backtrack
                usedDigits[d] = false;
                charToDigit.remove(c);
            }
        }
        return false;
    }

    static boolean isLeadingLetter(char c) {
        // Leading letters are first letters of any word or result
        for (String w : words) {
            if (w.charAt(0) == c) return true;
        }
        if (result.charAt(0) == c) return true;
        return false;
    }

    static int wordToNumber(String w) {
        int num = 0;
        for (char c : w.toCharArray()) {
            num = num * 10 + charToDigit.get(c);
        }
        return num;
    }

    static boolean checkSolution() {
        int sum = 0;
        for (String w : words) {
            sum += wordToNumber(w);
        }
        return sum == wordToNumber(result);
    }

    static void printSolution() {
        System.out.println("Solution found:");
        for (Map.Entry<Character, Integer> entry : charToDigit.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println();

        for (String w : words) {
            System.out.println(w + " = " + wordToNumber(w));
        }
        System.out.println(result + " = " + wordToNumber(result));
       System.out.println("Check: sum of words = " + Arrays.stream(words).mapToInt(Question2b::wordToNumber).sum());

    }
}
