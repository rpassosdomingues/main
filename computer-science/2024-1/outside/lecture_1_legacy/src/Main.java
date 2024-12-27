/**
 * ==================================================
 *       -- Aplicando Herança no Main --
 * --------------------------------------------------
 * Last Update: Jun 13 Thu
 * By Rafael Passos Domingues
 * ==================================================
 * 
 * Explicação:
 * 
 * > Criamos um objeto canetaTinteiro da classe CanetaTinteiro.
 * > Tentamos escrever inicialmente, mas a tampa está fechada (vide saída do console).
 * > Abrimos a tampa usando canetaTinteiro.abrirTampa().
 * > Agora a escrita é permitida e a tinta específica da caneta tinteiro é mencionada na saída.
 * > Verificamos a tinta restante usando o método herdado da classe base (mostrarTintaRestante). 
 * 
 * Benefícios da Herança:
 * 
 * - Reutilização de Código: A classe CanetaTinteiro reaproveita as propriedades e métodos da classe base
 * Caneta, evitando duplicação de código.
 * 
 * - Extensibilidade: O conceito de herança permite criar facilmente novas classes especializadas.
 * como (canetaTinteiro) que herdam características de classes genéricas (Caneta).
 * 
 * - Relação "é um": A classe CanetaTinteiro é um tipo de Caneta, demonstrando a relação hierárquica entre
 * as classes.
*/
public class Main {
    public static void main (String[] args) {
        // Criando um objeto caneta tinteiro azul com ponta 0.7 e tinta preta.
        CanetaTinteiro canetaTinteiro = new CanetaTinteiro("Azul", 0.7f, 50, "Preta");

        // Tentando escrever (tampa fechada)
        canetaTinteiro.escrever("Hello, World!");

        // Abrindo a tampa
        canetaTinteiro.abrirTampa();

        // Escrevendo novamente
        canetaTinteiro.escrever("Escrevendo com caneta tinteiro!");

        // Mostrando a tinta restante
        canetaTinteiro.mostrarTintaRestante();
    }
}
