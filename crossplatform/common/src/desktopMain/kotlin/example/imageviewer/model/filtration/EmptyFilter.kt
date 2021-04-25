package example.imageviewer.model.filtration


import example.imageviewer.core.BitmapFilter
import java.awt.image.BufferedImage

class EmptyFilter : BitmapFilter {

    override fun apply(bitmap: BufferedImage): BufferedImage {
        return bitmap
    }
}