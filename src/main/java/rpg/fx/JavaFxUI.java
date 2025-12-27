package rpg.fx;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import rpg.ui.GameUI;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class JavaFxUI implements GameUI {

    private final TextArea logArea;
    private final Text promptText;
    private final VBox buttonsBox;

    // Inline input controls (inside the same window)
    private final HBox inputRow;
    private final TextField inputField;
    private final Button okBtn;

    // NEW: status UI
    private final Label playerStatusLabel;
    private final ProgressBar playerHpBar;
    private final Label enemyStatusLabel;
    private final ProgressBar enemyHpBar;

    public JavaFxUI(TextArea logArea,
                    Text promptText,
                    VBox buttonsBox,
                    HBox inputRow,
                    TextField inputField,
                    Button okBtn,
                    Label playerStatusLabel,
                    ProgressBar playerHpBar,
                    Label enemyStatusLabel,
                    ProgressBar enemyHpBar) {

        this.logArea = logArea;
        this.promptText = promptText;
        this.buttonsBox = buttonsBox;

        this.inputRow = inputRow;
        this.inputField = inputField;
        this.okBtn = okBtn;

        this.playerStatusLabel = playerStatusLabel;
        this.playerHpBar = playerHpBar;
        this.enemyStatusLabel = enemyStatusLabel;
        this.enemyHpBar = enemyHpBar;
    }

    @Override
    public void println(String s) {
        Platform.runLater(() -> logArea.appendText(s + "\n"));
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

            // When asking text: remove option buttons
            buttonsBox.getChildren().clear();

            // Show input row
            inputField.clear();
            inputRow.setVisible(true);
            inputRow.setManaged(true);
            inputField.requestFocus();

            // OK submits
            okBtn.setOnAction(e -> {
                String text = inputField.getText();

                // Hide input row after submit
                inputRow.setVisible(false);
                inputRow.setManaged(false);

                q.offer(text);
            });

            // Enter key submits too
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

            // When choosing options: hide input row
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

    // -----------------------------
    // NEW status updates
    // -----------------------------
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

    private double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
