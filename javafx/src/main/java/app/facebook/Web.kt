package app.facebook

import com.dimaslanjaka.helper.CookieHandling
import javafx.application.Application
import javafx.concurrent.Worker
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.web.WebView
import javafx.stage.Stage
import sample.WebViewComplex
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

class Web : Application() {
    @Throws(Exception::class)
    override fun start(stage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/WebView.fxml"))
        val scene = Scene(root, 600.0, 400.0)
        val webView = scene.lookup("#webView") as WebView
        val urlbar = scene.lookup("#urlbar") as TextField
        val webEngine = webView.engine
        WebViewComplex.handling = CookieHandling(webView, Config.cookie_location)

        // go cookie manager
        //webEngine.load("https://www.webmanajemen.com/p/online-cookie-manager.html")
        webEngine.load("https://free.facebook.com")

        // set default stage title
        stage.title = "Facebook"
        webEngine.loadWorker.stateProperty().addListener { _, _, newState ->
            if (newState == Worker.State.SUCCEEDED) {
                // Update the stage title when a new web page title is available
                stage.title = webEngine.title
            } else if (newState == Worker.State.READY) {
                // set stage icon
                try {
                    val domain = URL(webEngine.location).host
                    val faviconUrl = String.format(
                        "http://www.google.com/s2/favicons?domain_url=%s",
                        URLEncoder.encode(domain, "UTF-8")
                    )
                    val favicon = Image(faviconUrl, true)
                    stage.icons.add(favicon)
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
        }
        stage.scene = scene
        stage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Web::class.java)
        }
    }
}