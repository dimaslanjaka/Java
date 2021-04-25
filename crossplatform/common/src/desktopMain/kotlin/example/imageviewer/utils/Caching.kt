package example.imageviewer.utils

import example.imageviewer.model.Picture
import java.io.*
import java.nio.charset.StandardCharsets
import javax.imageio.ImageIO

val cacheImagePostfix = "info"
val cacheImagePath = System.getProperty("user.home")!! +
        File.separator + "Pictures/imageviewer" + File.separator

fun cacheImage(path: String, picture: Picture) {
    try {
        ImageIO.write(picture.image, "png", File(path))

        val bw =
            BufferedWriter(
                OutputStreamWriter(
                    FileOutputStream(path + cacheImagePostfix),
                    StandardCharsets.UTF_8
                )
            )

        bw.write(picture.source)
        bw.write("\r\n${picture.width}")
        bw.write("\r\n${picture.height}")
        bw.close()

    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun clearCache() {

    val directory = File(cacheImagePath)

    val files: Array<File>? = directory.listFiles()

    if (files != null) {
        for (file in files) {
            if (file.isDirectory)
                continue

            file.delete()
        }
    }
}