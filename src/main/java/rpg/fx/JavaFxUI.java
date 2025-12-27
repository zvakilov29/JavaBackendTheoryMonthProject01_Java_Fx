package rpg.fx;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import rpg.ui.GameUI;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class JavaFxUI implements GameUI {

    private final ListView<FeedItem> feed;
    private final Text promptText;
    private final VBox buttonsBox;

    private final HBox inputRow;
    private final TextField inputField;
    private final Button okBtn;

    private final Label playerStatusLabel;
    private final ProgressBar playerHpBar;
    private final Label enemyStatusLabel;
    private final ProgressBar enemyHpBar;

    private final Label xpLabel;
    private final ProgressBar xpBar;
    private final Label potionLabel;

    public JavaFxUI(ListView<FeedItem> feed,
                    Text promptText,
                    VBox buttonsBox,
                    HBox inputRow,
                    TextField inputField,
                    Button okBtn,
                    Label playerStatusLabel,
                    ProgressBar playerHpBar,
                    Label enemyStatusLabel,
                    ProgressBar enemyHpBar,
                    Label xpLabel,
                    ProgressBar xpBar,
                    Label potionLabel) {

        this.feed = feed;
        this.promptText = promptText;
        this.buttonsBox = buttonsBox;

        this.inputRow = inputRow;
        this.inputField = inputField;
        this.okBtn = okBtn;

        this.playerStatusLabel = playerStatusLabel;
        this.playerHpBar = playerHpBar;
        this.enemyStatusLabel = enemyStatusLabel;
        this.enemyHpBar = enemyHpBar;

        this.xpLabel = xpLabel;
        this.xpBar = xpBar;
        this.potionLabel = potionLabel;
    }

    @Override
    public void println(String s) {
        FeedItem item = FeedItem.fromLine(s);
        Platform.runLater(() -> {
            feed.getItems().add(item);
            feed.scrollTo(feed.getItems().size() - 1);
        });
    }

    @Override
    public String readNonBlank(String prompt) {
        while (true) {
            String value = askTextInline(prompt);
            if (value != null && !value.isBlank()) return value.trim();
            println("Please enter a non-empty value.");
        }
    }

    private String askTextInline(String prompt) {
        var q = new ArrayBlockingQueue<String>(1);

        Platform.runLater(() -> {
            promptText.setText(prompt);

            buttonsBox.getChildren().clear();

            inputField.clear();
            inputRow.setVisible(true);
            inputRow.setManaged(true);
            inputField.requestFocus();

            okBtn.setOnAction(e -> {
                String text = inputField.getText();
                inputRow.setVisible(false);
                inputRow.setManaged(false);
                q.offer(text);
            });

            inputField.setOnAction(e -> okBtn.fire());
        });

        try {
            return q.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    @Override
    public int chooseOption(String prompt, List<String> options) {
        var q = new ArrayBlockingQueue<Integer>(1);

        Platform.runLater(() -> {
            promptText.setText(prompt);

            inputRow.setVisible(false);
            inputRow.setManaged(false);

            buttonsBox.getChildren().clear();

            for (int i = 0; i < options.size(); i++) {
                int idx = i;
                Button b = new Button(options.get(i));
                b.setMaxWidth(Double.MAX_VALUE);
                b.setOnAction(e -> q.offer(idx));
                buttonsBox.getChildren().add(b);
            }
        });

        try {
            return q.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        }
    }

    @Override
    public void updatePlayerStatus(String text, double hpPercent) {
        Platform.runLater(() -> {
            playerStatusLabel.setText(text);
            playerHpBar.setProgress(clamp01(hpPercent));
        });
    }

    @Override
    public void updateEnemyStatus(String text, double hpPercent) {
        Platform.runLater(() -> {
            enemyStatusLabel.setText(text);
            enemyHpBar.setProgress(clamp01(hpPercent));
        });
    }

    @Override
    public void updatePlayerProgress(String text, double xpPercent, int potions) {
        Platform.runLater(() -> {
            xpLabel.setText(text);
            xpBar.setProgress(clamp01(xpPercent));
            potionLabel.setText("Potions: " + potions);
        });
    }

    private double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
