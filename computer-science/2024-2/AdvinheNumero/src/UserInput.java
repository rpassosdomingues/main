import java.util.Scanner;

public class UserInput {
    private final Scanner scanner = new Scanner(System.in);

    public int getGuess() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
            }
        }
    }
}

