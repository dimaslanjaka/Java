package fxcardview;

import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class FacebookCard extends Pane {
    protected final ImageView photo;
    protected final Label name;
    private static int index = 0;

    public FacebookCard(String url, String card_name) {
        index++;
        photo = new ImageView();
        name = new Label();

        setId(index + "");
        setPrefHeight(233.0);
        setPrefWidth(245.0);
        setStyle("-fx-background-color:#FFF; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(3);
        dropShadow.setWidth(3);
        dropShadow.setBlurType(BlurType.TWO_PASS_BOX);
        setEffect(dropShadow);

        photo.setImage(new Image(url));
        photo.setLayoutX(123.0);
        photo.setLayoutY(56.0);
        photo.setFitHeight(100);
        photo.setFitWidth(100);

        name.setAlignment(javafx.geometry.Pos.CENTER);
        name.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        name.setLayoutX(14.0);
        name.setLayoutY(109.0);
        name.setPrefHeight(26.0);
        name.setPrefWidth(215.0);
        name.setText(card_name);
        name.setFont(new Font(17.0));

        getStylesheets().add("/css/CardDesign.css");
        getChildren().addAll(photo, name);
    }
}
