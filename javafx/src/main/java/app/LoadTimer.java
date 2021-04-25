package app;

import com.dimaslanjaka.library.helper.CookieHandling;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import dimas.kotlin.app.WebViewData;

/**
 * Reports load times for pages loaded in a WebView
 */
public class LoadTimer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        final WebView webview = new WebView();
        webview.getEngine().setJavaScriptEnabled(true);
        webview.getEngine().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
        new CookieHandling(webview, WebViewData.getCookiename());

        VBox layout = new VBox();
        layout.getChildren().setAll(
                createProgressReport(webview.getEngine()),
                webview
        );

        stage.setScene(new Scene(layout));
        stage.show();

        webview.getEngine().load("http://free.facebook.com");
    }

    /**
     * @return a HBox containing a ProgressBar bound to engine load progress and a Label showing load times
     */
    private HBox createProgressReport(WebEngine engine) {
        final LongProperty startTime = new SimpleLongProperty();
        final LongProperty endTime = new SimpleLongProperty();
        final LongProperty elapsedTime = new SimpleLongProperty();

        final ProgressBar loadProgress = new ProgressBar();
        loadProgress.progressProperty().bind(engine.getLoadWorker().progressProperty());

        final Label loadTimeLabel = new Label();
        loadTimeLabel.textProperty().bind(
                Bindings.when(
                        elapsedTime.greaterThan(0))
                        .then(
                                Bindings.concat("Loaded page in ", elapsedTime.divide(1_000_000), "ms")
                        )
                        .otherwise(
                                "Loading..."
                        )
        );

        elapsedTime.bind(Bindings.subtract(endTime, startTime));

        engine.getLoadWorker().stateProperty().addListener((observableValue, oldState, state) -> {
            switch (state) {
                case RUNNING:
                    startTime.set(System.nanoTime());
                    break;

                case SUCCEEDED:
                    endTime.set(System.nanoTime());
                    break;
            }
        });

        HBox progressReport = new HBox(10);
        progressReport.getChildren().setAll(
                loadProgress,
                loadTimeLabel
        );
        progressReport.setPadding(new Insets(5));
        progressReport.setAlignment(Pos.CENTER_LEFT);

        return progressReport;
    }
}