package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

import java.util.concurrent.Callable;

public class ComboBoxController {
    private ComboBox<String> box;
    public ComboBoxController(ComboBox<String> comboBox){
        box = comboBox;
    }

    public void control(final Callable<Object> oldValueCallback){
        box.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    oldValueCallback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
