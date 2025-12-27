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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        // GAME SCREEN (HUD + feed + actions)
        // =============================

        Label playerTitle = new Label("PLAYER");
        playerTitle.getStyleClass().add("hud-title");

        Label enemyTitle = new Label("ENEMY");
        enemyTitle.getStyleClass().add("hud-title");

        // Player HUD
        Label playerStatus = new Label("Player: -");
        playerStatus.getStyleClass().add("hud-value");

        ProgressBar playerHpBar = new ProgressBar(0);
        playerHpBar.setMaxWidth(Double.MAX_VALUE);

        Label xpLabel = new Label("XP: -");
        xpLabel.getStyleClass().add("hud-value");

        ProgressBar xpBar = new ProgressBar(0);
        xpBar.setMaxWidth(Double.MAX_VALUE);

        Label potionLabel = new Label("Potions: 0");
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
        enemyStatus.getStyleClass().add("hud-value");

        ProgressBar enemyHpBar = new ProgressBar(0);
        enemyHpBar.setMaxWidth(Double.MAX_VALUE);

        VBox enemyBox = new VBox(6,
                enemyTitle,
                enemyStatus,
                enemyHpBar
        );
        enemyBox.getStyleClass().add("hud-card");

        HBox topStatus = new HBox(20, playerBox, enemyBox);
        topStatus.setPadding(new Insets(12));
        topStatus.getStyleClass().add("top-hud");

        HBox.setHgrow(playerBox, Priority.ALWAYS);
        HBox.setHgrow(enemyBox, Priority.ALWAYS);
        playerBox.setMaxWidth(Double.MAX_VALUE);
        enemyBox.setMaxWidth(Double.MAX_VALUE);

        // ✅ Combat feed with custom cell renderer
        ListView<FeedItem> feed = new ListView<>();
        feed.getStyleClass().add("combat-feed");
        feed.setCellFactory(lv -> new FeedCell());

        // Prompt + actions
        Text prompt = new Text("Ready.");
        prompt.getStyleClass().add("prompt-text");

        VBox buttons = new VBox(8);
        buttons.getStyleClass().add("actions-box");
        buttons.setFillWidth(true);

        // Inline input row
        TextField inputField = new TextField();
        inputField.setPromptText("Type here...");

        Button okBtn = new Button("OK");
        okBtn.setDefaultButton(true);

        HBox inputRow = new HBox(8, inputField, okBtn);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        inputRow.getStyleClass().add("input-row");
        inputRow.setVisible(false);
        inputRow.setManaged(false);

        VBox bottom = new VBox(10, prompt, inputRow, buttons);
        bottom.setPadding(new Insets(12));
        bottom.getStyleClass().add("bottom-panel");

        BorderPane gameRoot = new BorderPane();
        gameRoot.getStyleClass().add("game-root");
        gameRoot.setTop(topStatus);
        gameRoot.setCenter(feed);
        gameRoot.setBottom(bottom);

        JavaFxUI ui = new JavaFxUI(
                feed, prompt, buttons,
                inputRow, inputField, okBtn,
                playerStatus, playerHpBar,
                enemyStatus, enemyHpBar,
                xpLabel, xpBar, potionLabel
        );

        // =============================
        // INTRO SCREEN
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

        VBox introBox = (logo != null)
                ? new VBox(16, logo, title, subtitle, startBtn)
                : new VBox(16, title, subtitle, startBtn);

        introBox.setAlignment(Pos.CENTER);
        introBox.setOpacity(0);
        introBox.setTranslateY(22);

        StackPane introRoot = new StackPane();
        if (bg != null) introRoot.getChildren().add(bg);
        else introRoot.setStyle("-fx-background-color: linear-gradient(#1f1f1f, #0f0f0f);");
        introRoot.getChildren().addAll(overlay, introBox);

        StackPane rootStack = new StackPane(introRoot);
        Scene scene = new Scene(rootStack, 900, 650);

        safeAddStylesheet(scene, "/styles/app.css");

        stage.setTitle("RPG Battle - JavaFX");
        stage.setScene(scene);
        stage.show();

        FadeTransition fade = new FadeTransition(Duration.millis(900), introBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        Timeline slide = new Timeline(
                new KeyFrame(Duration.millis(900), new KeyValue(introBox.translateYProperty(), 0))
        );
        slide.play();

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
            FxRunner.run(ui);

        }, "game-thread").start();
    }

    // Custom cell: tag + colored text
    private static class FeedCell extends ListCell<FeedItem> {
        private final Label tag = new Label();
        private final Label msg = new Label();
        private final HBox row = new HBox(10, tag, msg);

        FeedCell() {
            row.setAlignment(Pos.CENTER_LEFT);
            tag.getStyleClass().add("feed-tag");
            msg.getStyleClass().add("feed-text");
            setGraphic(row);
        }

        @Override
        protected void updateItem(FeedItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                getStyleClass().removeAll("feed-dmg", "feed-heal", "feed-xp", "feed-system", "feed-info");
                return;
            }

            setGraphic(row);
            msg.setText(item.getText());

            // tag text
            String t = switch (item.getKind()) {
                case DMG -> "DMG";
                case HEAL -> "HEAL";
                case XP -> "XP";
                case SYSTEM -> "SYS";
                default -> "INFO";
            };
            tag.setText(t);

            // apply a style class to the cell
            getStyleClass().removeAll("feed-dmg", "feed-heal", "feed-xp", "feed-system", "feed-info");
            switch (item.getKind()) {
                case DMG -> getStyleClass().add("feed-dmg");
                case HEAL -> getStyleClass().add("feed-heal");
                case XP -> getStyleClass().add("feed-xp");
                case SYSTEM -> getStyleClass().add("feed-system");
                default -> getStyleClass().add("feed-info");
            }
        }
    }

    private void safeAddStylesheet(Scene scene, String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url != null) scene.getStylesheets().add(url.toExternalForm());
        } catch (Exception ignored) { }
    }

    private ImageView tryLoadImageView(String resourcePath) {
        try {
            var stream = getClass().getResourceAsStream(resourcePath);
            if (stream == null) return null;
            return new ImageView(new Image(stream));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
