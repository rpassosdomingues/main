# Episódio 1
 - Abstração: Ocultar detalhes desnecessários para o software.
 - Classes: [Arquitetura] Modelo (Template) que incorpora características (Atributos/Campos/Variáveis de Instância) e funcionalidades (Métodos/Funções).
 - Objetos: Entidades criadas a partir de classes.
 - Classes são sementes, objetos são galhos.
 - A classe determina a estrutura que os objetos em nível de execução vão compartilhar. Dados específicos estão presentes em cada objeto.
 - Um classe é constituída de membros (campos, construtores e métodos).

## Por exemplo:

Classe Aluno
- Atributos: nome, curso.

Objeto 1
- nome: "Rafael"
- curso: "Ciência da Computação"

Objeto 2
- nome: "Nicolas"
- curso: "Gastronomia"

## Em código Java...

Aluno objeto1 = new Aluno ();
Aluno objeto2 = new Aluno ();