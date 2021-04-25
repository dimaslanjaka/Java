package app.facebook

import dimas.java.component.Image.loadImageViewFromUrl
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.io.InputStream
import java.net.URL

class Test : Application() {
    override fun start(primaryStage: Stage?) {
        val url = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Test-Logo.svg/783px-Test-Logo.svg.png"

        primaryStage!!.title = "GridPane Experiment"

        val button1 = Button("Button 1")
        val button2 = Button("Button 2")
        val button3 = Button("Button 3")
        val button4 = Button("Button 4")
        val button5 = Button("Button 5")
        val button6 = Button("Button 6")

        val gridPane = GridPane()

        gridPane.add(loadImageViewFromUrl(url), 0, 0, 1, 1)
        gridPane.add(button2, 1, 0, 1, 1)
        gridPane.add(button3, 2, 0, 1, 1)
        gridPane.add(button4, 0, 1, 1, 1)
        gridPane.add(button5, 1, 1, 1, 1)
        gridPane.add(button6, 2, 1, 1, 1)

        val scene = Scene(gridPane, 600.0, 600.0)
        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Test::class.java)
        }
    }

    fun startxx(stage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/Profile.fxml"))
        val scene = Scene(root, 600.0, 400.0)
        stage.scene = scene
        stage.show()
    }
}