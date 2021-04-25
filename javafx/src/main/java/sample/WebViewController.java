package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.dimaslanjaka.kotlin.ConsoleColors.Companion.println;

public class WebViewController {
    @FXML //  fx:id="fruitCombo"
    private ComboBox<String> fruitCombo;

    @FXML
    private TextField urlbar;

    @FXML
    private WebView webView;
    @FXML
    private BorderPane borderPane;

    @FXML
    private void initialize() {
        WebEngine engine = webView.getEngine();
        //WebViewListener.SetListener(webView);
        //engine.load("http://free.facebook.com");
        engine.load("http://git.io/");

        //borderPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // listen for changes to the fruit combo box selection and update the displayed fruit image accordingly.
        fruitCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
                if (oldFruit != null) {
                    if ("Apple".equals(oldFruit)) {
                        println("apple");
                    }
                }
                if (newFruit != null) {
                    if ("Apple".equals(newFruit)) {
                        WebViewListener.changeProfile("apple", true);
                    } else {
                        WebViewListener.changeProfile(newFruit);
                    }
                }
            }
        });
    }
}