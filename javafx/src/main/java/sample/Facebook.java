package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Facebook extends Application {
    private WebView webView;
    private WebEngine webEngine;
    private StackPane stackPane;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        webView = new WebView();

        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.setUserAgent("Mozilla/5.0 (compatible; ABrowse 0.4; Syllable)");

        stackPane = new StackPane();
        stackPane.getChildren().add(webView);

        stage = new Stage();
        stage.setScene(new Scene(stackPane, 600, 600));
        stage.show();

        //loadCredentials();
        CookieUtils.loadCookies();
        //webEngine.getLoadWorker().stateProperty().addListener(changeListener);
        WebViewListener.SetListener(webView);
        webEngine.load("https://fb.com/");
    }
}
