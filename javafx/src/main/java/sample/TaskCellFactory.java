package sample;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.util.Callback;

public class TaskCellFactory implements Callback<ListView<Task>, ListCell<Task>> {

    @Override
    public ListCell<Task> call(ListView<Task> param) {
        return new TaskCell();
    }
}