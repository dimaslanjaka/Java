package example.imageviewer.model.filtration

import example.imageviewer.core.BitmapFilter
import example.imageviewer.utils.applyBlurFilter
import java.awt.image.BufferedImage

class BlurFilter : BitmapFilter {

    override fun apply(bitmap: BufferedImage): BufferedImage {
        return applyBlurFilter(bitmap)
    }
}