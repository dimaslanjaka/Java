package dimas.kotlin.component

import com.dimaslanjaka.helper.File
import javafx.scene.image.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI
import java.net.URL
import javax.imageio.ImageIO

object ImageHelper {
    @JvmStatic
    fun downloadImage(urlImage: String): Image? {
        val extension = File.getFileExtension(urlImage)
        if (extension?.isPresent == true) {
            val url = URL(urlImage)
            val c: BufferedImage = ImageIO.read(url)
            val os = ByteArrayOutputStream()
            ImageIO.write(c, extension.get(), os)
            val inputStream: InputStream = ByteArrayInputStream(os.toByteArray())
            return Image(inputStream)
        }
        return null;
    }

    @JvmStatic
    fun downloadImage(urlImage: URL): Image? {
        return downloadImage(urlImage.toString())
    }

    @JvmStatic
    fun downloadImage(urlImage: URI): Image? {
        return downloadImage(urlImage.toString())
    }
}