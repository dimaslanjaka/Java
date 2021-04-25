package example.imageviewer.model.filtration

import example.imageviewer.core.BitmapFilter
import example.imageviewer.utils.applyGrayScaleFilter
import java.awt.image.BufferedImage

class GrayScaleFilter : BitmapFilter {

    override fun apply(bitmap: BufferedImage): BufferedImage {
        return applyGrayScaleFilter(bitmap)
    }
}