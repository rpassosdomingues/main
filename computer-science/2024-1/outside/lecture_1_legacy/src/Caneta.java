/**
 * ==================================================
 *             -- Exemplo Prático --
 * --------------------------------------------------
 * Classes, Propriedades, Métodos e Objetos em Java
 * --------------------------------------------------
 * Last Update: Jun 11 Tue
 * By Rafael Passos Domingues
 * ==================================================
 * 
 * A classe caneta define um modelo para representar canetas.
 * 
 * - As propriedades (atributos) representam as características
 * de uma caneta, como cor, ponta e tinta restante.
 * 
 * - O construtor é um método especial que inicializa os valores
 * das propriedades quando um objeto da classe é criado.
 * 
 * - Os métodos definem as ações que a caneta pode realizar,
 * como escrever e mostrar tinta restante.
 *  
 */

// Criando a Classe Caneta
public class Caneta {
    // Propriedades (Atributos)
    private String cor;
    private float ponta;
    private int tintaRestante;

    // Construtor (Método especial para inicializar objetos)
    public Caneta (String cor, float ponta, int tintaRestante) {
        this.cor = cor;
        this.ponta = ponta;
        this.tintaRestante = tintaRestante;
    }

    // Métodos (Ações que a caneta pode realizar)

    // Método -- ESCREVER --
    public void escrever (String texto) {
        if (tintaRestante > 0) {
            System.out.println("Escrevendo em " + cor + " com ponta " + ponta + ": " + texto);
            tintaRestante -= texto.length();
        } else {
            System.out.println("Caneta sem tinta! Tinta restante: " + tintaRestante);
        }
    }

    // Método -- MOSTRAR TINTA RESTANTE --
    public void mostrarTintaRestante () {
        System.out.println("Tinta restante: " + tintaRestante);
    }

    // Getters and Setters (Métodos para acessar e modificar propriedades)
    public String getCor () {
        return cor;
    }

    public void setCor (String cor) {
        this.cor = cor;
    }

    public float getPonta () {
        return ponta;
    }

    public void setPonta (float ponta) {
        this.ponta = ponta;
    }

    public int getTintaRestante () {
        return tintaRestante;
    }

    public void setTintaRestante (int tintaRestante) {
        this.tintaRestante = tintaRestante;
    }
}
