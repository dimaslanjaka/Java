package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.concurrent.Callable;

public class WebViewListener {
    private final static ChangeListener<Worker.State> changeListener = new ChangeListener<Worker.State>() {
        @Override
        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
            if (newValue == Worker.State.SUCCEEDED) {
                CookieUtils.saveCookies();
            }
        }
    };
    static WebEngine webEngine;

    public static void SetListener(WebView w) {
        webEngine = w.getEngine();
        CookieUtils.loadCookies();
        addListener();
    }

    public static void SetListener(WebView w, Callable<Object> callback) {
        SetListener(w);
        try {
            callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addListener() {
        webEngine.getLoadWorker().stateProperty().addListener(changeListener);
    }

    private static void removeListener() {
        webEngine.getLoadWorker().stateProperty().removeListener(changeListener);
    }

    public static void changeProfile(String name) {
        changeProfile(name, false);
    }

    public static void changeProfile(String name, boolean debug) {
        CookieUtils.setFileCookie(name, debug);
        removeListener();
        addListener();
    }
}
