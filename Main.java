package bullscows;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int numberOfChar;
    static int numberOfSymbols;
    static char[] symbolsWhatNeed;
    static String questionNumber;
    static String answerNumber = "";
    static int countTurns = 1;
    static int cows = 0;
    static int bulls = 0;

    public static void main(String[] args) {
        questionNumber = "";
        start();
        random();
        readVariants();
    }

    private static void start() {
        System.out.println("Input the length of the secret code:");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine(); // ввели не цифру
        try {
            numberOfChar = Integer.parseInt(str);
            if (numberOfChar < 1) {
                System.out.printf("Error: it's not possible to generate a code with a length of %d", numberOfChar);
                System.exit(0);
            }
            System.out.println("Input the number of possible symbols in the code:");
            numberOfSymbols = scanner.nextInt(); // больше 37 или меньше numberOfChar
            if (numberOfChar < numberOfSymbols) {
                if (numberOfSymbols < 37) {
                    countRules();
                } else {
                    System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                    System.exit(0);
                }
            } else {
                System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", numberOfChar, numberOfSymbols);
                System.exit(0);
            }
        }
        catch (Exception e){
            System.out.printf("Error: " + str + " isn't a valid number.", numberOfChar, numberOfSymbols);
            System.exit(0);
        }
    }

    private static void countRules() {
        StringBuilder lineOut = new StringBuilder("The secret is prepared: ");
        lineOut.append("*".repeat(Math.max(0, numberOfChar)));
        symbolsWhatNeed = new char[numberOfSymbols];
        for (int i = 0; i < numberOfSymbols; i++) {
            if (i < 10) {
                symbolsWhatNeed[i] = (char) i;
            }
            else {
                symbolsWhatNeed[i] = (char) ('a' + (i - 10));
            }
        }
        showRules(lineOut);
    }

    private static void showRules(StringBuilder lineOut) {
        if (numberOfSymbols <=  10) {
            System.out.println(lineOut + " (0-" + (numberOfSymbols - 1) + ").");
        }
        else if (numberOfSymbols ==  11) {
            System.out.println(lineOut + " (0-9, a).");
        }
        else {
            System.out.println(lineOut + " (0-9, a-" + (char)('a' + (numberOfSymbols - 11)) + ").");
        }
        System.out.println("Okay, let's start a game!");
    }

    private static void random() {
        Boolean[] question = new Boolean[numberOfSymbols];
        Arrays.fill(question, Boolean.FALSE);
        if (numberOfChar < 5) {
            for (int i = 0; i < numberOfChar; ) {
                int randomIndex = new Random().nextInt(symbolsWhatNeed.length);
                if (!question[randomIndex]) {
                    question[randomIndex] = true;
                    if (randomIndex < 10) {
                        questionNumber += randomIndex;
                    } else {
                        questionNumber += ((char)('a' + (randomIndex - 10)));
                    }
                    i++;
                }
            }
        }
        else {
            for (int i = 0; i < numberOfChar; i++) {
                questionNumber += i;
            }
        }
    }

    private static void readVariants() {
        System.out.printf("Turn %d:\n", countTurns);
        countTurns++;
        Scanner sc = new Scanner(System.in);
        answerNumber = sc.nextLine();
        cows();
    }

    private static void cows() {
        cows = 0;
        Boolean[] answer = countCows(answerNumber);
        Boolean[] question = countCows(questionNumber);
        for (int i = 0; i < answer.length; i++) {
            cows = question[i] == answer[i] && question[i] == true ? cows + 1 : cows;
        }
        logic();
    }

    private static Boolean[] countCows(String str) {
        Boolean[] bool = new Boolean[numberOfSymbols];
        Arrays.fill(bool, Boolean.FALSE);
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < numberOfSymbols; j++) {
                bool[j] = j < 10 ? ((char)(j + '0') == str.charAt(i)? true : bool[j]) : ((char)('a' + (j - 10)) == str.charAt(i) ? true : bool[j]);
            }
        }
        return bool;
    }

    private static void logic(){
        bulls = 0;
        for (int i = 0; i < answerNumber.length(); i++){
            if (questionNumber.charAt(i) == answerNumber.charAt(i)){
                bulls++;
            }
        }
        cows -= bulls;
        write();
    }

    private static void write() {
        if (bulls == 0 && cows == 0)
            System.out.print("Grade: None.\n");
        else if (bulls == 0 && cows != 0)
            System.out.printf("Grade: %d cow(s).\n",cows);
        else if (bulls != 0 && cows != 0)
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n",bulls, cows);
        else if (bulls != 0 && cows == 0)
            System.out.printf("Grade: %d bull(s).\n",bulls);
        if (bulls == numberOfChar)
            System.out.println("Congratulations! You guessed the secret code.");
        else
            readVariants();
    }
}