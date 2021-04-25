package example.imageviewer.model.filtration

import example.imageviewer.core.BitmapFilter
import example.imageviewer.utils.applyPixelFilter
import java.awt.image.BufferedImage

class PixelFilter : BitmapFilter {

    override fun apply(bitmap: BufferedImage): BufferedImage {
        return applyPixelFilter(bitmap)
    }
}