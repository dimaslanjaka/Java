package sample;

import com.dimaslanjaka.library.helper.CookieHandling;
import dimas.java.component.TextFieldController;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import static com.dimaslanjaka.gradle.plugin.Utils.webUrlValid;

public class WebViewComplex extends Thread {
    /**
     * Change this to save cookies
     */
    public static CookieHandling handling = new CookieHandling();
    @FXML
    private ProgressBar progBar;
    @FXML
    private TextField urlbar;
    @FXML
    private WebView webView;
    private EventHandler<? super KeyEvent> event = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent ke) {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                goEnter(urlbar.getText().trim());
            }
        }
    };

    @FXML
    public void onEnter(ActionEvent ae) {
        enterUrl();
    }

    private boolean onEnterListened = false;

    public void enterUrl() {
        String url = urlbar.getText().trim();
        goEnter(url);

        // start listener
        if (!onEnterListened) {
            onEnterListened = true;
            urlbar.setOnKeyPressed(event);
        }
    }

    private void goEnter(String url) {
        if (webUrlValid(url)) {
            webView.getEngine().load(url);
        } else {
            webView.getEngine().load("https://google.com/?search=" + url);
        }
    }

    @SuppressWarnings("SetJavaScriptEnabled")
    @FXML
    private void initialize() {
        WebEngine engine = webView.getEngine();
        engine.setJavaScriptEnabled(true);
        engine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
        handling.loadCookie();

        // smart text field
        //new TextFieldController(urlbar, this.getClass().getName());

        // listen webview
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                urlbar.setText(webView.getEngine().getLocation());
                handling.saveCookie();
            }
        });

        // load default url
        engine.load("http://free.facebook.com");

        /*
        urlbar.textProperty().addListener((observable, oldValue, newValue) -> {
            new Thread(() -> {
                if (webUrlValid(newValue)) {
                    engine.load(newValue);
                }
            });
        });
         */

        //progress bar
        progBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
        /*
        progBar.visibleProperty().bind(
                Bindings.when(progBar.progressProperty().lessThan(0).or(progBar.progressProperty().isEqualTo(1)))
                        .then(false)
                        .otherwise(true)
        );
         */
        //progBar.managedProperty().bind(progBar.visibleProperty());
        progBar.setMaxWidth(Double.MAX_VALUE);

        //borderPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // listen for changes to the fruit combo box selection and update the displayed fruit image accordingly.
        /*
        fruitCombo.getSelectionModel().selectedItemProperty().addListener((selected, oldFruit, newFruit) -> {
            if (newFruit != null) {
                handling.setFileCookie(CookieHandling.Companion.getAppdir() + "/build/cookies/" + newFruit + ".json");
                engine.reload();
            }
        });
         */
    }
}
