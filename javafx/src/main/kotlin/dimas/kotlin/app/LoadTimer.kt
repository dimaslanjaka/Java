package dimas.kotlin.app

import com.dimaslanjaka.helper.CookieHandling
import dimas.kotlin.app.WebViewData.cookiename
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.Stage
import tornadofx.launch

/**
 * Reports load times for pages loaded in a WebView
 */
class LoadTimer : Application() {
    override fun start(stage: Stage) {
        val webview = WebView()
        val layout = VBox(5.0)

        webview.engine.isJavaScriptEnabled = true
        webview.engine.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0"
        CookieHandling(webview, cookiename)

        layout.children.setAll(
            createProgressReport(webview.engine),
            webview
        )

        stage.scene = Scene(layout)
        stage.isFullScreen = true

        // full width height of parent
        //webview.prefWidthProperty().bind(layout.prefWidthProperty())
        //webview.prefHeightProperty().bind(layout.prefHeightProperty())

        stage.show()
        webview.engine.load("http://free.facebook.com")
    }

    /**
     * @return a HBox containing a ProgressBar bound to engine load progress and a Label showing load times
     */
    private fun createProgressReport(engine: WebEngine): HBox {
        val startTime: LongProperty = SimpleLongProperty()
        val endTime: LongProperty = SimpleLongProperty()
        val elapsedTime: LongProperty = SimpleLongProperty()
        val loadProgress = ProgressBar()
        loadProgress.progressProperty().bind(engine.loadWorker.progressProperty())
        val loadTimeLabel = Label()
        loadTimeLabel.textProperty().bind(
            Bindings.`when`(
                elapsedTime.greaterThan(0)
            )
                .then(
                    Bindings.concat("Loaded page in ", elapsedTime.divide(1000000), "ms")
                )
                .otherwise(
                    "Loading..."
                )
        )
        elapsedTime.bind(Bindings.subtract(endTime, startTime))
        engine.loadWorker.stateProperty()
            .addListener { _: ObservableValue<out Worker.State?>?, _: Worker.State?, state: Worker.State? ->
                when (state) {
                    Worker.State.RUNNING -> startTime.set(System.nanoTime())
                    Worker.State.SUCCEEDED -> endTime.set(System.nanoTime())
                    else -> {

                    }
                }
            }
        val progressReport = HBox(10.0)
        progressReport.children.setAll(
            loadProgress,
            loadTimeLabel
        )
        progressReport.padding = Insets(5.0)
        progressReport.alignment = Pos.CENTER_LEFT
        return progressReport
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<LoadTimer>(*args)
        }
    }
}
