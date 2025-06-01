package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Hero hero; // Referência ao herói selecionado
    private List<Enemy> enemies; // Lista de inimigos
    private Enemy.Difficulty selectedDifficulty; // Armazenar a dificuldade selecionada para o Inimigo
    private Pane gameArea; // Área do jogo
    private BorderPane stageArea; // Declarando o layout do stage aqui
    private double heroX = 50; // Posição inicial do herói
    private double heroY = 300; // Posição inicial do herói
    private double speedMultiplier = 2; // Fator de multiplicação da velocidade
    private VBox controlPanel; // Painel de controle
    private AnimationTimer gameLoop; // Timer de animação

    @Override
    public void start(Stage primaryStage) {
        enemies = new ArrayList<>(); // Inicializa a lista de inimigos

        // Cria a estrutura do layout
        stageArea = new BorderPane();
        stageArea.setPrefSize(600, 600);
        gameArea = new Pane();
        gameArea.setPrefSize(600, 600);
    
        // Define a cor de fundo
        gameArea.setStyle("-fx-background-color: lightblue;"); // Cor de fundo da área do jogo

        stageArea.setCenter(gameArea);
        stageArea.setRight(createControlPanel()); // Painel de controle à direita

        Scene scene = new Scene(stageArea, 800, 600);
        primaryStage.setTitle("Back to the Valhalla");
        primaryStage.setScene(scene);
        primaryStage.show(); // Mostra a janela principal
    }

    private VBox createControlPanel() {
        controlPanel = new VBox(10); // Espaçamento entre os botões
        controlPanel.setPrefWidth(200);
        controlPanel.setStyle("-fx-padding: 10; -fx-background-color: #eee;");

        // Botões de controle
        Button newGameButton = new Button("New Game");
        Button optionsButton = new Button("Options");
        Button exitButton = new Button("Exit");

        // Ações para os botões
        newGameButton.setOnAction(e -> openNewGameWindow());
        optionsButton.setOnAction(e -> showOptionsMenu());
        exitButton.setOnAction(e -> exitGame());

        // Adiciona os botões ao painel de controle
        controlPanel.getChildren().addAll(newGameButton, optionsButton, exitButton);
        return controlPanel;
    }

    private void openNewGameWindow() {
        // Clear existing options from the control panel
        controlPanel.getChildren().clear();

        // Create the new game layout
        VBox newGameLayout = new VBox(10);
        ComboBox<String> stageSelection = createStageSelection();
        ComboBox<String> heroSelection = createHeroSelection();
        selectHero("Thor"); // Set Thor as the default hero

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> startGame());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainMenu()); // Return to main menu

        heroSelection.setOnAction(e -> selectHero(heroSelection.getValue())); // Update hero on selection

        // Updated stage selection listener
        stageSelection.setOnAction(e -> {
            String selectedStage = stageSelection.getValue();
            int stageNumber = Integer.parseInt(selectedStage.split(" ")[1]); // Extracts the stage number from the string
            selectStage(stageNumber); // Calls the method with the integer stage number
        });

        newGameLayout.getChildren().addAll(new Label("Select Stage:"), stageSelection,
                                            new Label("Select Hero:"), heroSelection,
                                            startButton, backButton);

        controlPanel.getChildren().addAll(new Label("New Game Options"), newGameLayout); // Add new game layout to control panel
    }

    private ComboBox<String> createStageSelection() {
        // Cria um novo ComboBox para seleção de estágios
        ComboBox<String> stageSelection = new ComboBox<>();

        // Define a posição do ComboBox no stageArea
        stageArea.setTop(stageSelection);

        // Adiciona os itens (nomes dos estágios) no ComboBox
        stageSelection.getItems().addAll("Stage 1", "Stage 2", "Stage 3");
        stageSelection.setValue("Stage 1"); // Define o valor padrão (Stage 1)

        // Adiciona um listener para detectar mudanças na seleção
        stageSelection.setOnAction(event -> {
            // Obtém a string selecionada
            String selectedStage = stageSelection.getValue();

            // Converte a string para o número do estágio
            int stageNumber = Integer.parseInt(selectedStage.split(" ")[1]); // Extrai o número do estágio
            selectStage(stageNumber); // Chama o método com o número do estágio
        });

        // Retorna o ComboBox criado
        return stageSelection;
    }

    private ComboBox<String> createHeroSelection() {
        ComboBox<String> heroSelection = new ComboBox<>();
        heroSelection.getItems().addAll("Thor", "Loki", "Heimdall", "Feiticeira Negra", "Odin");
        heroSelection.setValue("Thor"); // Valor padrão
        return heroSelection;
    }

    private Scenario currentScenario; // Mantenha uma referência ao cenário atual

    private void selectStage(int stageNumber) {
        // Remove o cenário atual, se existir
        if (currentScenario != null) {
            gameArea.getChildren().remove(currentScenario.getRoot());
            currentScenario = null;
        }

        // Define a dificuldade
        Enemy.Difficulty difficulty = Enemy.Difficulty.EASY; // Altere conforme necessário

        // Cria um novo cenário usando StageFactory
        currentScenario = StageFactory.createStage(stageNumber, difficulty);

        // Log para depuração
        System.out.println("Carregando estágio " + stageNumber);

        // Configura e adiciona o novo cenário
        currentScenario.setup();
        gameArea.getChildren().add(currentScenario.getRoot());
    }

    private void selectHero(String heroName) {
        // Remove o herói atual da área do jogo, se existir
        if (hero != null) {
            gameArea.getChildren().remove(hero.getShape());
        }

        // Cria um novo herói com base na seleção
        hero = HeroFactory.createHero(heroName);
        if (hero != null) {
            positionHero(); // Posiciona o herói na área do jogo
            gameArea.getChildren().add(hero.getShape()); // Adiciona o herói à área do jogo
        }
    }

    private void positionHero() {
        // Define a posição do herói na área do jogo
        hero.getShape().setTranslateX(heroX);
        hero.getShape().setTranslateY(heroY);
    }

    public void startGame() {
        spawnEnemies(); // Cria inimigos
        startAnimation(); // Inicia a animação dos inimigos e do herói
    }

    private void startAnimation() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame(); // Atualiza o estado do jogo a cada frame
            }
        };
        gameLoop.start(); // Inicia o loop de animação
    }

    private void restartGame() {
        gameArea.getChildren().clear(); // Limpa a área de jogo
        enemies.clear(); // Limpa a lista de inimigos
        heroX = 50; // Reseta a posição do herói
        positionHero(); // Reposiciona o herói
        spawnEnemies(); // Recria inimigos
        if (gameLoop != null) {
            gameLoop.stop(); // Para o loop de animação
        }
        startAnimation(); // Reinicia a animação
    }

    private void spawnEnemies() {
        enemies.clear(); // Limpa a lista de inimigos
        for (int i = 0; i < 5; i++) {
            // Cria inimigos com dificuldade média
            Enemy enemy = new Enemy("Inimigo" + (i + 1), Enemy.Difficulty.MEDIUM, Color.RED, 750, 100 + (i * 50));
            enemies.add(enemy); // Adiciona o inimigo à lista
            gameArea.getChildren().add(enemy.getShape()); // Adiciona o inimigo à área do jogo
        }
    }

    private void updateGame() {
        // Atualiza a posição do herói
        if (hero != null) {
            heroX += speedMultiplier; // Move o herói
            hero.getShape().setTranslateX(heroX);
        }

        // Atualiza a posição dos inimigos
        for (Enemy enemy : enemies) {
            enemy.moveTowards(heroX, heroY); // Move os inimigos em direção ao herói
            checkCollisions(hero, enemy); // Verifica colisões entre o herói e os inimigos
        }
    }

    private void checkCollisions(Hero hero, Enemy enemy) {
        if (isColliding(hero, enemy)) {
            resolveCollision(hero, enemy); // Resolve a colisão se houver
        }
    }

    private boolean isColliding(Hero hero, Enemy enemy) {
        double heroRadius = 15; // Raio do herói (triângulo)
        double enemyRadius = 15; // Raio do inimigo (círculo)

        // Calcula a distância entre o herói e o inimigo
        double distance = Math.sqrt(Math.pow(hero.getX() - enemy.getX(), 2) + Math.pow(hero.getY() - enemy.getY(), 2));
        return distance < (heroRadius + enemyRadius); // Retorna true se houver colisão
    }

    private void resolveCollision(Hero hero, Enemy enemy) {
        // O herói recebe dano do inimigo
        hero.takeDamage(enemy.getHealth());

        // Verifica se o herói está vivo
        if (hero.getHealth() <= 0) {
            System.out.println(hero.getName() + " foi derrotado!"); // Mensagem se o herói for derrotado
            gameLoop.stop(); // Para o loop de animação
        }

        // O inimigo recebe dano do herói
        enemy.takeDamage(hero.getAttackPower());

        // Verifica se o inimigo está vivo
        if (!enemy.isAlive()) {
            gameArea.getChildren().remove(enemy.getShape()); // Remove o inimigo da área do jogo
        }
    }

    private void showOptionsMenu() {
        // Limpa a área de jogo e a lista de inimigos
        gameArea.getChildren().clear();
        enemies.clear();

        // Título
        Label titleLabel = new Label("Select Difficulty");
        controlPanel.getChildren().clear(); // Limpa o painel de controle

        // Botões para selecionar a dificuldade
        Button easyButton = new Button("EASY");
        easyButton.setOnAction(e -> {
            selectedDifficulty = Enemy.Difficulty.EASY; // Define a dificuldade selecionada
            System.out.println("Dificuldade selecionada: " + selectedDifficulty);
            backToMainMenu(); // Retorna ao menu principal
        });

        Button mediumButton = new Button("MEDIUM");
        mediumButton.setOnAction(e -> {
            selectedDifficulty = Enemy.Difficulty.MEDIUM; // Define a dificuldade selecionada
            System.out.println("Dificuldade selecionada: " + selectedDifficulty);
            backToMainMenu(); // Retorna ao menu principal
        });

        Button hardButton = new Button("HARD");
        hardButton.setOnAction(e -> {
            selectedDifficulty = Enemy.Difficulty.HARD; // Define a dificuldade selecionada
            System.out.println("Dificuldade selecionada: " + selectedDifficulty);
            backToMainMenu(); // Retorna ao menu principal
        });

        // Botão de voltar
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainMenu());

        // Adiciona os componentes ao painel de controle
        controlPanel.getChildren().addAll(titleLabel, easyButton, mediumButton, hardButton, backButton);
    }

    private void backToMainMenu() {
        controlPanel.getChildren().clear(); // Limpa o painel de controle
    
        // Recria os botões do menu principal
        Button newGameButton = new Button("New Game");
        Button optionsButton = new Button("Options");
        Button exitButton = new Button("Exit");
    
        newGameButton.setOnAction(e -> openNewGameWindow()); // Abre o submenu de "New Game"
        optionsButton.setOnAction(e -> showOptionsMenu()); // Abre o menu de opções
        exitButton.setOnAction(e -> exitGame()); // Sai do jogo
    
        controlPanel.getChildren().addAll(newGameButton, optionsButton, exitButton); // Adiciona os botões ao painel de controle
    }    

    private void exitGame() {
        // Fecha a janela do jogo
        Stage stage = (Stage) controlPanel.getScene().getWindow();
        stage.close();
    }

    public static void main(String[] args) {
        launch(args); // Inicia a aplicação JavaFX
    }
}
