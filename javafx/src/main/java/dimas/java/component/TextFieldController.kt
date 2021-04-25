package dimas.java.component

import com.dimaslanjaka.gradle.plugin.MD5
import com.dimaslanjaka.kotlin.Temp
import javafx.beans.value.ChangeListener
import javafx.scene.control.TextField
import java.io.File

class TextFieldController(textField: TextField, signature: Any) {
    private val tmpClass = Temp(TextField::class.java.simpleName)
    private val tempFile: File = tmpClass.getTempFile(MD5.get(signature.toString()))
    private val textField: TextField
    private var listener: ChangeListener<String>? = null

    /**
     * Attach listener to text field
     */
    fun attachListener() {
        listener = ChangeListener { _, oldValue, newValue ->
            if (newValue != oldValue) Thread {
                com.dimaslanjaka.helper.File.write(
                    tempFile.absolutePath,
                    newValue
                )
            }
        }
        textField.textProperty().addListener(listener)
    }

    fun detachListener() {
        textField.textProperty().removeListener(listener)
        listener = null
    }

    private fun restoreText() {
        if (tempFile.exists()) {
            if (com.dimaslanjaka.helper.File.read(tempFile.absolutePath).isNotEmpty()) {
                textField.text = com.dimaslanjaka.helper.File.read(tempFile.absolutePath)
            }
        }
    }

    init {
        this.textField = textField
        restoreText()
        attachListener()
    }
}