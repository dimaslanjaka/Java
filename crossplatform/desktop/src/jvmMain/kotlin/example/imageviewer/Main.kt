package example.imageviewer

import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import example.imageviewer.model.ContentState
import example.imageviewer.style.icAppRounded
import example.imageviewer.utils.getPreferredWindowSize
import example.imageviewer.view.BuildAppUI

fun main() {
    Window(
        title = "Facebot",
        size = getPreferredWindowSize(800, 1000),
        icon = icAppRounded()
    ) {
        MaterialTheme {

        }
    }
}

fun mainDefault() {
    Window(
        title = "ImageViewer",
        size = getPreferredWindowSize(800, 1000),
        icon = icAppRounded()
    ) {
        val content = ContentState.applyContent(
            "https://spvessel.com/iv/images/fetching.list"
        )
        MaterialTheme {
            DesktopTheme {
                BuildAppUI(content)
            }
        }
    }
}
