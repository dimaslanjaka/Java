package dimas.kotlin.app

import com.dimaslanjaka.kotlin.Helper.println
import com.dimaslanjaka.helper.CookieHandling
import dimas.kotlin.app.WebViewData.cookiename
import dimas.kotlin.app.WebViewData.homepage
import dimas.kotlin.component.ImageHelper.downloadImage
import dimas.kotlin.component.StageHelper
import javafx.scene.control.ProgressBar
import javafx.scene.layout.VBox
import javafx.scene.web.WebView
import javafx.stage.Stage
import tornadofx.View
import tornadofx.setStageIcon

class SingleWebView : View {
    override val root: VBox by fxml("/SingleWebView.fxml")
    val web: WebView by fxid("web")
    private val progress: ProgressBar by fxid("progress")

    constructor(title: String, imageUrl: String) {
        println("single webview with custom title")
        this.title = title;
        val img =
            downloadImage(imageUrl)
        if (img != null) {
            setStageIcon(img)
        }

        initialize()
    }

    constructor() {
        this.title = WebViewData.window_title
        StageHelper.setImageIcon(this, WebViewData.image_path)

        initialize()
    }

    private fun initialize() {
        //root.addClass(Styles.defaultScreen)
        web.engine.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0"

        CookieHandling(web, cookiename)

        // full width height of parent
        web.prefWidthProperty().bind(root.prefWidthProperty())
        web.prefHeightProperty().bind(root.prefHeightProperty())

        // load url
        web.engine.load(homepage)

        // updating progress bar using binding
        progress.progressProperty().bind(web.engine.loadWorker.progressProperty())

        web.engine.loadWorker.stateProperty().addListener { _, _, newValue ->
            if (newValue === javafx.concurrent.Worker.State.SUCCEEDED) {
                // hide progress bar then page is ready
                progress.isVisible = false
            } else if (!progress.isVisible){
                progress.isVisible = true
            }
        }
    }

    fun start(stage: Stage) {
        stage.width = 800.0
        stage.height = 600.0
        //stage.icons.add(Image(inputStream))
    }
}
