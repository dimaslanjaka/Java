package app.facebook

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.paddingAll
import java.util.*

class SelectProfiles : Application() {
    //lateinit var root : Region
    val rng = Random()

    override fun start(primaryStage: Stage) {
        val root = BorderPane()
        //root.paddingAll = 5

        val vbox1 = VBox()
        vbox1.spacing = 5.0 //Set vbox spacing
        // set width full
        vbox1.prefWidthProperty().bind(root.widthProperty())
        val scroller = ScrollPane(vbox1)
        scroller.isFitToWidth = true
        scroller.paddingAll = 5.0

        //Handles the number of row to be added to the vbox
        for (i in 0..3) {
            val cardPane = fbcard(i, "http://signpost.mywebermedia.com/wp-content/uploads/sites/2/2018/03/Facebook-no-profile-picture-icon-620x389.jpg", "Fb")
            // set width full
            cardPane.prefWidthProperty().bind(vbox1.widthProperty())
            vbox1.children.add(cardPane)
        }

        //root.children.add(vbox1)
        root.center = scroller
        val scene = Scene(root, 400.0, 400.0)
        //val scene = Scene(root, 600.0, 300.0)

        primaryStage.title = "Hello World!"
        primaryStage.scene = scene
        primaryStage.show()
    }

    @Throws(Exception::class)
    fun fbcard(current_index: Int, imageUrl: String, profileName: String): HBox {
        // setup profile image view
        val photo = ImageView()
        photo.image = Image(imageUrl)
        photo.fitHeight = 100.0
        photo.fitWidth = 100.0
        //photo.style = "-fx-border-radius: 10px;-fx-background-radius: 10px;"

        // setup profile name
        val name = Label()
        name.text = profileName
        name.style = "-fx-font-weight: bold; -fx-font-family: \"Courier New\""

        // setup card pane
        val cardPane = HBox(5.0)
        //cardPane.style = "-fx-background-color:#FFF; -fx-border-radius: 10px; -fx-background-radius: 10px;"
        cardPane.style = "-fx-background-color:#FFF;"

        // set id each created element
        cardPane.id = "card-$current_index"
        photo.id = "card-photo-$current_index"
        name.id = "card-name-$current_index"

        val dropShadow = DropShadow()
        dropShadow.height = 3.0
        dropShadow.width = 3.0
        dropShadow.blurType = BlurType.TWO_PASS_BOX
        cardPane.effect = dropShadow

        // add photo, name to card pane
        cardPane.children.addAll(photo, name)
        return cardPane
    }

    /**
     * @url https://stackoverflow.com/a/26776520
     */
    fun interface LoopCallBack<currentIterator, totalIterator, Out> {
        // (In1,In2) -> Out
        fun apply(in1: currentIterator, in2: totalIterator): Out
    }

    fun loop(x: Int, callback: LoopCallBack<Int, Int, Any>) {
        for (i in 0..x) {
            callback.apply(i, x)
        }
    }
}