/***
* Fig. 2.7: Addition.java
* Programa de adição que exibe a soma de dois números.
*
* @author Rafael P. Domingues
* Last Update: 2024 Apr 1, Mon
*
*/
import java.util.Scanner; // programa utiliza a classe Scanner.

public class Addition {
   // Método principal inicia a execução do aplicativo Java
   public static void main (String args[]) {
      // Cria Scanner para obter entrada a partir da janela de comando.
      Scanner input = new Scanner(System.in);
      
      int number1; // primeiro número a somar
      int number2; // segundo número a somar
      int sum;     // soma de number1 e number2
      
      System.out.print("Enter first integer: "); // prompt
      number1 = input.nextInt(); // Lê o primeiro número fornecido pelo usuário.
      
      System.out.print("Enter second integer: "); // prompt
      number2 = input.nextInt(); // Lê o segundo número fornecido pelo usuário.
      
      sum = number1 + number2; // Soma os números
      
      System.out.printf("Sum is %d\n", sum); // exibe a soma
   } // Fim do método principal.
} // Fim da classe Addition.
