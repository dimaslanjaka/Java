package driver

import org.openqa.selenium.*
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.events.EventFiringWebDriver
import org.openqa.selenium.support.events.WebDriverEventListener
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class WebDriverCookie {
    private var driver: WebDriver
    var cookiePath: File = File("build/Cookies.data")
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    var dateTimeFormatter: DateFormat = SimpleDateFormat(pattern)

    constructor(webDriver: WebDriver) {
        driver = webDriver
        loadCookie(this.cookiePath)
        listen()
    }

    constructor(webDriver: WebDriver, cookiePath: File) {
        driver = webDriver
        this.cookiePath = cookiePath
        listen()
    }

    fun formatDate(string: String): Date {
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
        val date = LocalDate.parse(string, formatter)
        return DateUtils.asDate(date)
    }

    fun listen() {
        val eventHandler = EventFiringWebDriver(driver)
        val eCapture = EventCapture()
        eventHandler.register(eCapture)
        driver = eventHandler
        val runnable = Runnable {
            while (!hasQuit(driver)) {
                Thread.sleep(5000)
                saveCookie(cookiePath)
                Thread.sleep(10000)
            }
        }
        Thread(runnable).start()
    }

    fun hasQuit(driver: WebDriver?): Boolean {
        if (driver == null) return false
        return (driver as RemoteWebDriver).sessionId == null
    }

    fun formatDate(date: Date?): String? {
        return dateTimeFormatter.format(date)
    }

    @Suppress("unused")
    fun saveCookie(file: File = File("build/Cookies.data")) {
        try {
            // Delete old file if exists
            file.delete()
            file.createNewFile()
            val fileWrite = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWrite)

            // loop for getting the cookie information
            for (ck in driver.manage().cookies) {
                if (ck.expiry != null) {
                    bufferedWriter.write(
                        ck.name.toString() + ";" + ck.value + ";" + ck.domain + ";" + ck.path + ";" + formatDate(
                            ck.expiry
                        ) + ";" + ck.isSecure
                    )
                    bufferedWriter.newLine()
                }
            }
            bufferedWriter.close()
            fileWrite.close()
        } catch (ex: java.lang.Exception) {
            driver.quit()
            ex.printStackTrace()
        }
    }

    @Suppress("unused")
    fun loadCookie(file: File = File("build/Cookies.data")) {
        try {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            var storyline: String?
            while (bufferedReader.readLine().also { storyline = it } != null) {
                val token = StringTokenizer(storyline, ";")
                while (token.hasMoreTokens()) {
                    val name = token.nextToken()
                    val value = token.nextToken()
                    val domain = token.nextToken()
                    val path = token.nextToken()
                    var expiry: Date? = null
                    var dateString: String?
                    if (token.nextToken().also { dateString = it } != "null") {
                        expiry = dateString?.let { formatDate(it) }
                    }
                    val isSecure = token.nextToken().toBoolean()
                    val ck = Cookie(name, value, domain, path, expiry, isSecure)
                    println(ck)
                    // add the stored cookie to your current session
                    driver.manage().addCookie(ck)
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}

class EventCapture : WebDriverEventListener {
    override fun beforeAlertAccept(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterAlertAccept(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterAlertDismiss(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeAlertDismiss(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeNavigateTo(url: String?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterNavigateTo(url: String?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeNavigateBack(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterNavigateBack(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeNavigateForward(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterNavigateForward(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeNavigateRefresh(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterNavigateRefresh(driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeFindBy(by: By?, element: WebElement?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterFindBy(by: By?, element: WebElement?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeClickOn(element: WebElement?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterClickOn(element: WebElement?, driver: WebDriver?) {
        if (element != null) {
            println("clicked on tag ${element.tagName}")
        } else if (driver != null) {
            println("clicked on page ${driver.title}")
        }
    }

    override fun beforeChangeValueOf(element: WebElement?, driver: WebDriver?, keysToSend: Array<out CharSequence>?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterChangeValueOf(element: WebElement?, driver: WebDriver?, keysToSend: Array<out CharSequence>?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeScript(script: String?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterScript(script: String?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeSwitchToWindow(windowName: String?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterSwitchToWindow(windowName: String?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun onException(throwable: Throwable?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun <X : Any?> beforeGetScreenshotAs(target: OutputType<X>?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun <X : Any?> afterGetScreenshotAs(target: OutputType<X>?, screenshot: X) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun beforeGetText(element: WebElement?, driver: WebDriver?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

    override fun afterGetText(element: WebElement?, driver: WebDriver?, text: String?) {
        println(Thread.currentThread().stackTrace[1].methodName)
    }

}