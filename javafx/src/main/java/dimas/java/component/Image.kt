package dimas.java.component

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.InputStream
import java.net.URL

/**
 * JavaFX image helper
 */
object Image {
    /**
     * Load javafx.scene.image.ImageView from url
     */
    @JvmStatic
    fun loadImageViewFromUrl(imageUrl: String): ImageView {
        return ImageView(loadImageFromUrl(imageUrl))
    }

    /**
     * Load javafx.scene.image.Image from url
     */
    @JvmStatic
    fun loadImageFromUrl(imageUrl: String): Image {
        val url = URL(imageUrl)
        val img = url.openStream() as InputStream
        return Image(img)
    }
}