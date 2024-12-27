/**
 * ==================================================
 *       -- Criando e Usando o Objeto Caneta --
 * --------------------------------------------------
 * Last Update: Jun 11 Tue
 * By Rafael Passos Domingues
 * ==================================================
 * 
 * - A classe Main contém o método main onde criamos e utilizamos o objeto Caneta.
 * - Criamos um objeto canetaAzul usando o construtor e definimos suas propriedades.
 * - Chamamos o método escrever para fazer a caneta escrever um texto.
 * - Chamamos o método mostrarTintaRestante para verificar a quantidade de tinta restante.
 * - Modificamos a cor da caneta usando o setter setCor.
 * - Chamamos o método escrever novamente com outro texto.
 * - Verificamos a tinta restante mais uma vez.
 * 
 * Este exemplo demonstra os conceitos básicos de classes, propriedades, métodos e objetos em Java.
 * 
 * Na programação real, as classes podem ser bem mais complexas,
 * com diversas propriedades, métodos e hierarquias de herança.
 * 
 * É importante seguir boas práticas de programação,
 * como utilizar getters e setters para acessar e modificar propriedades,
 * e documentar o código para facilitar a compreensão.
*/
public class Main {
    public static void main (String[] args) {
        // Criando um objeto caneta azul com ponta 0.5 e 100 de tinta
        Caneta canetaAzul = new Caneta ("Azul", 0.5f, 100);

        // Fazendo a caneta escrever um texto
        canetaAzul.escrever("Hello World!");

        // Mostrando a tinta restante
        canetaAzul.mostrarTintaRestante();

        // Mudando a cor da caneta para verde
        canetaAzul.setCor("Verde");

        // Fazendo a caneta escrever outro texto
        canetaAzul.escrever("Escrevendo em verde!");

        // Mostrando a tinta restante
        canetaAzul.mostrarTintaRestante();
    }
}
