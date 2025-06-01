/**
 * ==================================================
 *             -- Aplicando Herança --
 * --------------------------------------------------
 * Extensão da Classe Caneta
 * --------------------------------------------------
 * Last Update: Jun 12 Tue
 * By Rafael Passos Domingues
 * ==================================================
 *  
 * Vamos dividir nosso modelo de caneta em duas classes:
 * 
 * 1. Classe Base Caneta: Define as características
 * gerais de uma caneta como cor, ponta e tinta restante.
 * 
 * 2. Classe Filha Caneta Tinteiro: Herda da classe Caneta
 * e possui características específicas de caneta tinteiro,
 * como tipo e mecanismo de recarga.
 * 
 * Explicação das modificações
 * 
 * Classe CanetaTinteiro:
 * - Herança: Ela herda da classe Caneta através da palavra-chave extends.
 * 
 * O construtor da classe CanetaTinteiro chama explicitamente o
 * construtor da classe base (Caneta) usando super(cor, ponta, tintaRestante).
 * Isso garante que as propriedades comuns (cor, ponta e tintaRestante)
 * sejam inicializadas corretamente.
 * 
 * Métodos:
 * 
 * - abrirTampa: Método específico da caneta tinteiro, utilizado para abrir a tampa.
 * - escrever (sobrescrito):
 * > Anotação @Override indica que estamos sobrescrevendo o método escrever
 * herdado da classe base.
 * > Verifica se a tampa está aberta (tampaFechada == false) antes de permitir a escrita.
 * > Caso a tampa esteja fechada, exibe uma mensagem para abrir.
 * 
 * Getters e Setters:
 * 
 * - A classe CanetaTinteiro possui getters e setters específicos
 * para suas propriedades (tipoTinta e tampaFechada).
 * 
 */

// Classe Base Caneta (sem alterações)

// Criando a Classe Filha
public class CanetaTinteiro extends Caneta {
    // Propriedades específicas de caneta tinteiro
    private String tipoTinta;
    private boolean tampaFechada;

    // Construtor da classe filha
    public CanetaTinteiro (String cor, float ponta, int tintaRestante, String tipoTinta) {
        // Chamada de construtor da classe base
        super(cor, ponta, tintaRestante);
        this.tipoTinta = tipoTinta;
        this.tampaFechada = true;
    }

    // Métodos (Ações que a caneta pode realizar)

    // Método específico de caneta tinteiro
    public void abrirTampa () {
        tampaFechada = false;
        System.out.println("Tampa da caneta tinteiro aberto!");
    }

    // Método reescrito (sobrescrito) da classe base
    @Override
    public void escrever (String texto) {
        if (tintaRestante > 0 && tampaFechada == false) {
            System.out.println("Escrevendo em " + cor + " com ponta " + ponta + " com tinta " + tipoTinta + ": " + texto);
            tintaRestante -= texto.length();
        } else if (tampaFechada) {
            System.out.println("Abra tampa da caneta tinteiro para escrever!");
        } else {
            System.out.println("Caneta sem tinta! Tinta restante: " + tintaRestante);
        }
    }

    // Getters and Setters específicos da caneta tinteiro
    public String getTipoTinta () {
        return tipoTinta;
    }

    public void setTipoTinta (String tipoTinta) {
        this.tipoTinta = tipoTinta;
    }

    public boolean isTampaFechada () {
        return tampaFechada;
    }

    public void setTampaFechada (boolean tampaFechada) {
        this.tampaFechada = tampaFechada;
    }
}
