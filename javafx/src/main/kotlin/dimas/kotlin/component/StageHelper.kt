package dimas.kotlin.component

import dimas.kotlin.component.ImageHelper
import javafx.scene.image.Image
import tornadofx.View
import tornadofx.setStageIcon
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL
import javax.imageio.ImageIO

object StageHelper {
    @JvmStatic
    fun setIcon(view: View, urlImage: String) {
        val url = URL(urlImage)
        val c: BufferedImage = ImageIO.read(url)
        val os = ByteArrayOutputStream()
        ImageIO.write(c, "png", os)
        val inputStream: InputStream = ByteArrayInputStream(os.toByteArray())
        setStageIcon(Image(inputStream))
    }

    @JvmStatic
    fun setImageIcon(view: View, imageUrl: String){
        val img =
            ImageHelper.downloadImage(imageUrl)
        if (img != null) {
            view.primaryStage.icons.add(img)
        }
    }
}