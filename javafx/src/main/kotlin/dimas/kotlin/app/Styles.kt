package dimas.kotlin.app

import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {
    companion object {
        //val win7 by importStylesheet("/resources/css/win7.css")
        val defaultScreen by cssclass()
    }

    init {
        defaultScreen {
            padding = box(15.px)
            vgap = 7.px
            hgap = 10.px
        }
    }
}