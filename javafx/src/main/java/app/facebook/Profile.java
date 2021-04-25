package app.facebook;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import static dimas.java.component.Image.loadImageViewFromUrl;

public class Profile extends Thread {
    @FXML
    GridPane gridPane;

    @FXML
    private void initialize() {
        String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Test-Logo.svg/783px-Test-Logo.svg.png";
        //gridPane.add(loadImageViewFromUrl(url), 0, 0, 1, 1);

        Button button1 = new Button("Button 1");
        gridPane.add(button1, 1, 0, 1, 1);
    }
}
