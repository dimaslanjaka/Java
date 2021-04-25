package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class TaskCell extends ListCell<Task> {

    @FXML
    private Label titleLabel;

    @FXML
    private Label commentLabel;

    @FXML
    private Label descriptionLabel;

    public TaskCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/task_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            titleLabel.setText(item.getTitle());
            commentLabel.setText(item.getTitle());
            descriptionLabel.setText(item.getMessage());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}