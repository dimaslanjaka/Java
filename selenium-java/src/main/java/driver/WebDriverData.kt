package driver

import org.openqa.selenium.firefox.FirefoxDriverLogLevel
import java.io.File
import java.util.*

class WebDriverData {
    var proxy: String? = null
    var url = "https://www.whatismybrowser.com/"
    var cookieFile: File? = null
    var proxyFile: File? = null
    var userAgent: String? = null
    var firefoxProfile = "default"
    var background = false
    var firefoxLogLevel: FirefoxDriverLogLevel = FirefoxDriverLogLevel.FATAL

    companion object {
        val tempFolder = File(System.getProperty("java.io.tmpdir"), "Selenium")
        var chromeDriverPath = File("bin/chromedriver.exe")
        var firefoxDriverPath = File("bin/geckodriver.exe")
        var phantomDriverPath = File("bin/phantomjs.exe")
        fun setupDriver() {
            println("Setup start ${Date()}")
            if (!chromeDriverPath.exists()) {
                chromeDriverPath = File("selenium-java/bin/chromedriver.exe")
            }
            if (!firefoxDriverPath.exists()) {
                firefoxDriverPath = File("selenium-java/bin/geckodriver.exe")
            }
            if (!phantomDriverPath.exists()) {
                phantomDriverPath = File("selenium-java/bin/phantomjs.exe")
            }
            System.setProperty("webdriver.chrome.driver", chromeDriverPath.absolutePath)
            System.setProperty("webdriver.gecko.driver", firefoxDriverPath.absolutePath)
            System.setProperty("phantomjs.binary.path", phantomDriverPath.absolutePath)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            setupDriver()
            println(chromeDriverPath.absolutePath)
            println(firefoxDriverPath.absolutePath)
        }
    }
}
