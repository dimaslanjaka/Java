package example.imageviewer.view2

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview

@Composable
fun NewsStory() {
    Text("A day in Shark Fin Cove")
    Text("Davenport, California")
    Text("December 2018")
}

@Preview
@Composable
fun DefaultPreview() {
    NewsStory()
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
