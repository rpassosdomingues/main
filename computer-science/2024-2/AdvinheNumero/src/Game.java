import java.util.Random;

public class Game {
    private final UserInput userInput = new UserInput();
    private final int targetNumber;

    public Game() {
        Random rand = new Random();
        targetNumber = rand.nextInt(100) + 1; // Gera número entre 1 e 100
    }

    public void start() {
        System.out.println("Bem-vindo ao jogo de adivinhação!");
        boolean isCorrect = false;

        while (!isCorrect) {
            System.out.print("Adivinhe o número (entre 1 e 100): ");
            int guess = userInput.getGuess();

            if (guess < targetNumber) {
                System.out.println("Muito baixo!");
            } else if (guess > targetNumber) {
                System.out.println("Muito alto!");
            } else {
                System.out.println("Parabéns! Você adivinhou o número!");
                isCorrect = true;
            }
        }
    }
}

