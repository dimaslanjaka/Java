package dimas.kotlin.app

import com.dimaslanjaka.gradle.plugin.Utils
import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet
import tornadofx.launch
import dimas.kotlin.component.mergeArrays2

class App : App(SingleWebView::class) {
    //class App : App() {
    override fun start(stage: Stage) {
        importStylesheet(resources["/css/win7.css"])
        val params = parameters
        if (params != null) {
            val list = params.raw
            if (list.size > 0) {
                WebViewData.homepage = list[0]
                WebViewData.cookiename = list[1]
                for (each in list) {
                    if (Utils.webUrlValid(each)) {
                        WebViewData.homepage = each
                    }
                }
            }
        }
        super.start(stage)
    }
}
//class App : App(SingleWebView::class, Styles::class)

/*
class App : App() {
    override fun start(stage: Stage) {
        //super.start(stage)
        SingleWebView()
    }
}
 */

fun main(args: Array<String>) {
    val newargs: Array<String> = arrayOf("http://0.facebook.com", "default")
    val merge: Array<String> = mergeArrays2(args, newargs)
    launch<dimas.kotlin.app.App>(merge)
}

