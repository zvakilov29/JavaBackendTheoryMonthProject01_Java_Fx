package rpg.fx;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class FxMain extends Application {

    @Override
    public void start(Stage stage) {

        // =============================
        // GAME SCREEN (HUD + log + actions)
        // =============================

        // Player HUD
        Label playerStatus = new Label("Player: -");
        ProgressBar playerHpBar = new ProgressBar(0);
        playerHpBar.setMaxWidth(Double.MAX_VALUE);

        Label xpLabel = new Label("XP: -");
        ProgressBar xpBar = new ProgressBar(0);
        xpBar.setMaxWidth(Double.MAX_VALUE);

        Label potionLabel = new Label("Potions: 0");

        Label playerTitle = new Label("PLAYER");
        playerTitle.getStyleClass().add("hud-title");

        playerStatus.getStyleClass().add("hud-value");
        xpLabel.getStyleClass().add("hud-value");
        potionLabel.getStyleClass().add("hud-value");

        VBox playerBox = new VBox(6,
                playerTitle,
                playerStatus,
                playerHpBar,
                xpLabel,
                xpBar,
                potionLabel
        );

        playerBox.getStyleClass().add("hud-card");

        // Enemy HUD
        Label enemyStatus = new Label("Enemy: -");
        ProgressBar enemyHpBar = new ProgressBar(0);
        enemyHpBar.setMaxWidth(Double.MAX_VALUE);

        Label enemyTitle = new Label("ENEMY");
        enemyTitle.getStyleClass().add("hud-title");

        enemyStatus.getStyleClass().add("hud-value");

        VBox enemyBox = new VBox(6,
                enemyTitle,
                enemyStatus,
                enemyHpBar
        );

        enemyBox.getStyleClass().add("hud-card");

        HBox topStatus = new HBox(20, playerBox, enemyBox);
        topStatus.setPadding(new Insets(12));
        HBox.setHgrow(playerBox, Priority.ALWAYS);
        HBox.setHgrow(enemyBox, Priority.ALWAYS);
        playerBox.setMaxWidth(Double.MAX_VALUE);
        enemyBox.setMaxWidth(Double.MAX_VALUE);

        // Log area
        TextArea log = new TextArea();
        log.setEditable(false);
        log.setWrapText(true);
        log.getStyleClass().add("log-area");

        // Prompt + actions
        Text prompt = new Text("Ready.");
        prompt.getStyleClass().add("prompt-text");

        VBox buttons = new VBox(8);

        // Inline input row (same window)
        TextField inputField = new TextField();
        inputField.setPromptText("Type here...");

        Button okBtn = new Button("OK");
        okBtn.setDefaultButton(true);

        HBox inputRow = new HBox(8, inputField, okBtn);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        inputRow.getStyleClass().add("input-row");

        // hidden by default
        inputRow.setVisible(false);
        inputRow.setManaged(false);

        VBox bottom = new VBox(10, prompt, inputRow, buttons);
        bottom.setPadding(new Insets(12));
        bottom.getStyleClass().add("bottom-panel");

        BorderPane gameRoot = new BorderPane();
        gameRoot.getStyleClass().add("game-root");
        topStatus.getStyleClass().add("top-hud");
        bottom.getStyleClass().add("bottom-panel");
        gameRoot.setTop(topStatus);
        gameRoot.setCenter(log);
        gameRoot.setBottom(bottom);
        gameRoot.getStyleClass().add("game-root"); // ✅ this makes .game-root work
        inputRow.getStyleClass().add("input-row");


        // JavaFX UI adapter (must match your JavaFxUI constructor)
        JavaFxUI ui = new JavaFxUI(
                log, prompt, buttons,
                inputRow, inputField, okBtn,
                playerStatus, playerHpBar,
                enemyStatus, enemyHpBar,
                xpLabel, xpBar, potionLabel
        );

        // =============================
        // INTRO SCREEN (RPG themed)
        // =============================

        ImageView bg = tryLoadImageView("/images/rpg_bg.jpg");
        if (bg != null) {
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(stage.widthProperty());
            bg.fitHeightProperty().bind(stage.heightProperty());
        }

        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        ImageView logo = tryLoadImageView("/images/logo.png");
        if (logo != null) {
            logo.setPreserveRatio(true);
            logo.setFitWidth(160);
            logo.setSmooth(true);
        }

        Text title = new Text("RPG Battle");
        title.setFont(Font.font(58));
        title.setStyle("-fx-fill: white;");

        Text subtitle = new Text("Java OOP engine • JavaFX UI");
        subtitle.setStyle("-fx-fill: rgba(255,255,255,0.85);");

        Button startBtn = new Button("Start Game");
        startBtn.setPrefWidth(240);
        startBtn.setPrefHeight(44);
        startBtn.getStyleClass().add("start-button");
        buttons.setFillWidth(true);

        VBox introBox = (logo != null)
                ? new VBox(16, logo, title, subtitle, startBtn)
                : new VBox(16, title, subtitle, startBtn);

        introBox.setAlignment(Pos.CENTER);
        introBox.setOpacity(0);
        introBox.setTranslateY(22);

        StackPane introRoot = new StackPane();
        if (bg != null) {
            introRoot.getChildren().add(bg);
        } else {
            introRoot.setStyle("-fx-background-color: linear-gradient(#1f1f1f, #0f0f0f);");
        }
        introRoot.getChildren().addAll(overlay, introBox);

        // =============================
        // ROOT STACK (swap intro <-> game)
        // =============================
        StackPane rootStack = new StackPane(introRoot);
        Scene scene = new Scene(rootStack, 900, 650);

        // attach CSS safely
        safeAddStylesheet(scene, "/styles/app.css");

        stage.setTitle("RPG Battle - JavaFX");
        stage.setScene(scene);
        stage.show();

        // =============================
        // Intro animation
        // =============================
        FadeTransition fade = new FadeTransition(Duration.millis(900), introBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        Timeline slide = new Timeline(
                new KeyFrame(Duration.millis(900),
                        new KeyValue(introBox.translateYProperty(), 0))
        );
        slide.play();

        // =============================
        // Wait for Start -> switch -> run game
        // =============================
        CountDownLatch startLatch = new CountDownLatch(1);
        startBtn.setOnAction(e -> startLatch.countDown());

        new Thread(() -> {
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Platform.runLater(() -> rootStack.getChildren().setAll(gameRoot));

            // Run your existing flow (menus, saves, battles, encounters)
            FxRunner.run(ui);

        }, "game-thread").start();
    }

    private void safeAddStylesheet(Scene scene, String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url != null) scene.getStylesheets().add(url.toExternalForm());
        } catch (Exception ignored) {
        }
    }

    private ImageView tryLoadImageView(String resourcePath) {
        try {
            var stream = getClass().getResourceAsStream(resourcePath);
            if (stream == null) return null;
            Image img = new Image(stream);
            return new ImageView(img);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
