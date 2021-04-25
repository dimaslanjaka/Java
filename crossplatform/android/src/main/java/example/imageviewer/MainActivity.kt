package example.imageviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import example.imageviewer.model.ContentState
import example.imageviewer.view.BuildAppUI
import example.imageviewer.view2.Greeting

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Greeting("Android")
        }
    }

    fun defaultPreview(){
        val content = ContentState.applyContent(
            this@MainActivity,
            "https://spvessel.com/iv/images/fetching.list"
        )

        setContent {
            BuildAppUI(content)
        }
    }
}