package fxcardview;

import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;

public class CustomerCard extends Pane {

    protected final ImageView photo;
    protected final Label name;
    protected final Label mobile;
    protected final Label label;
    protected final Label date;
    protected final Label visites;
    protected final Label label0;
    private int ID;

    public CustomerCard(int Id, String Name, String Mobile, String Date, String Vistes) {
        photo = new ImageView();
        name = new Label();
        mobile = new Label();
        label = new Label();
        date = new Label();
        visites = new Label();
        label0 = new Label();

        setId(Id + "");
        setPrefHeight(233.0);
        setPrefWidth(245.0);
        setStyle("-fx-background-color:#FFF; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(3);
        dropShadow.setWidth(3);
        dropShadow.setBlurType(BlurType.TWO_PASS_BOX);
        setEffect(dropShadow);

        photo.setImage(new Image("https://www.irreverentgent.com/wp-content/uploads/2018/03/Awesome-Profile-Pictures-for-Guys-look-away2.jpg"));
        photo.setLayoutX(123.0);
        photo.setLayoutY(56.0);
        photo.setFitHeight(100);
        photo.setFitWidth(100);
//        photo.setRadius(45.0);
//        photo.setStroke(javafx.scene.paint.Color.valueOf("#d7d7d7"));
//        photo.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        name.setAlignment(javafx.geometry.Pos.CENTER);
        name.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        name.setLayoutX(14.0);
        name.setLayoutY(109.0);
        name.setPrefHeight(26.0);
        name.setPrefWidth(215.0);
        name.setText(Name);
        name.setFont(new Font(17.0));

        mobile.setAlignment(javafx.geometry.Pos.CENTER);
        mobile.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        mobile.setLayoutX(16.0);
        mobile.setLayoutY(143.0);
        mobile.setPrefHeight(19.0);
        mobile.setPrefWidth(215.0);
        mobile.setText(Mobile);
        mobile.setFont(new Font(13.0));

        label.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label.setLayoutX(39.0);
        label.setLayoutY(169.0);
        label.setPrefHeight(19.0);
        label.setPrefWidth(62.0);
        label.setText("Date :");
        label.setFont(new Font(13.0));

        date.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        date.setLayoutX(107.0);
        date.setLayoutY(169.0);
        date.setPrefHeight(19.0);
        date.setPrefWidth(115.0);
        date.setText(Date);
        date.setFont(new Font(13.0));

        visites.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        visites.setLayoutX(107.0);
        visites.setLayoutY(194.0);
        visites.setPrefHeight(19.0);
        visites.setPrefWidth(115.0);
        visites.setText(Vistes);
        visites.setFont(new Font(13.0));

        label0.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        label0.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label0.setLayoutX(39.0);
        label0.setLayoutY(194.0);
        label0.setPrefHeight(19.0);
        label0.setPrefWidth(62.0);
        label0.setText("Visites :");
        label0.setFont(new Font(13.0));

        setOnMouseClicked(e -> {
            // Action you want to do
            Alert alert = new Alert(AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Sample Alert");
            alert.showAndWait();
        });

        getStylesheets().add("/css/CardDesign.css");
        getChildren().addAll(photo, name, mobile, label, date, visites, label0);
    }
}