package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class TaskCellController {
    @FXML
    private ListView<Task> listView;

    @FXML
    private void initialize() {
        listView.setCellFactory(new TaskCellFactory());
    }
}
