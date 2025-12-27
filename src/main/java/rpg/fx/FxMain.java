package rpg.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FxMain extends Application {

    @Override
    public void start(Stage stage) {
        // -----------------------------
        // Top status panel (NEW)
        // -----------------------------
        Label playerStatus = new Label("Player: -");
        ProgressBar playerHpBar = new ProgressBar(0);
        playerHpBar.setMaxWidth(Double.MAX_VALUE);

        Label enemyStatus = new Label("Enemy: -");
        ProgressBar enemyHpBar = new ProgressBar(0);
        enemyHpBar.setMaxWidth(Double.MAX_VALUE);

        VBox playerBox = new VBox(6, new Label("PLAYER"), playerStatus, playerHpBar);
        VBox enemyBox = new VBox(6, new Label("ENEMY"), enemyStatus, enemyHpBar);

        HBox topStatus = new HBox(20, playerBox, enemyBox);
        topStatus.setPadding(new Insets(10));
        HBox.setHgrow(playerBox, Priority.ALWAYS);
        HBox.setHgrow(enemyBox, Priority.ALWAYS);
        playerBox.setMaxWidth(Double.MAX_VALUE);
        enemyBox.setMaxWidth(Double.MAX_VALUE);

        // -----------------------------
        // Main log area
        // -----------------------------
        TextArea log = new TextArea();
        log.setEditable(false);
        log.setWrapText(true);

        // Prompt text (what the game asks right now)
        Text prompt = new Text("Ready.");

        // Buttons area (options like Attack/Defend/etc.)
        VBox buttons = new VBox(8);
        buttons.setPadding(new Insets(0, 10, 10, 10));

        // Inline input row (for name or any text input)
        TextField inputField = new TextField();
        inputField.setPromptText("Type here...");
        Button okBtn = new Button("OK");
        HBox inputRow = new HBox(8, inputField, okBtn);
        inputRow.setPadding(new Insets(0, 10, 0, 10));

        // Hidden by default; shown only when asking for text
        inputRow.setVisible(false);
        inputRow.setManaged(false);

        VBox bottom = new VBox(10, prompt, inputRow, buttons);
        bottom.setPadding(new Insets(10));

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(topStatus);          // NEW
        root.setCenter(log);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("RPG Battle - JavaFX");
        stage.setScene(scene);
        stage.show();

        // Create UI adapter (NEW params added at the end)
        JavaFxUI ui = new JavaFxUI(
                log, prompt, buttons, inputRow, inputField, okBtn,
                playerStatus, playerHpBar,
                enemyStatus, enemyHpBar
        );

        // Run game logic on background thread
        new Thread(() -> FxRunner.run(ui), "game-thread").start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
