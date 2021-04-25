package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;

import static dimas.java.component.Image.loadImageFromUrl;

public class CardView extends Pane {
    CardController cardController;
    Node view;

    public CardView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXMLFiles/Card.fxml"));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return cardController = new CardController();
            }
        });
        try {
            view = (Node) fxmlLoader.load();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        getChildren().add(view);
        cardController.setNameAndDsc("Card", "This is A card", loadImageFromUrl("https://upload.wikimedia.org/wikipedia/commons/8/85/Logo-Test.png"));
    }

}