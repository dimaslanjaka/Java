@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package driver

import com.dimaslanjaka.library.helper.Resources
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Proxy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.UnreachableBrowserException
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File


object TestChrome {
    lateinit var driver: WebDriver
    var chromeOptions: ChromeOptions = ChromeOptions()
    var capabilities: DesiredCapabilities = DesiredCapabilities()
    val resource = Resources("config.properties").toProperties()

    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        setChromeVersion(90)
        setChromeBinary("D:\\ProgramData\\GoogleChromePortable64\\App\\Chrome-bin\\chrome.exe")
        setProxy("150.238.75.122:3128")
        driver = ChromeDriver(chromeOptions)
        detect()
    }

    /**
     * Load url, when error will be quit driver
     */
    fun get(url: String) {
        try {
            driver.get(url)
            waitForPageLoaded(driver)
        } catch (e: Exception) {
            driver.quit()
        }
    }

    fun waitForPageLoaded(driver: WebDriver, callback: Callback? = null) {
        val expectation: ExpectedCondition<Boolean> =
            ExpectedCondition { driverLoaded ->
                (driverLoaded as JavascriptExecutor?)!!.executeScript("return document.readyState") == "complete"
            }
        val wait = WebDriverWait(driver, 30)
        try {
            wait.until(expectation)
        } catch (error: Throwable) {
            //Assert.assertFalse(true, "Timeout waiting for Page Load Request to complete.")
            println("Connection Timeout")
        }
    }

    /**
     * Sync capabilities with chrome options
     * @see <a href="https://stackoverflow.com/a/45514063">Stack Overflow</a>
     */
    fun prepare(): TestChrome {
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions)
        return this
    }

    /**
     * Set chrome driver which compatible with your chrome version.
     * Based on first number of version.
     */
    fun setChromeVersion(prefix: Int) {
        var path = ""
        path = when {
            File("bin/chromedriver-$prefix.exe").exists() -> {
                File("bin/chromedriver-$prefix.exe").absolutePath
            }
            File("selenium-java/bin/chromedriver-$prefix.exe").exists() -> {
                File("selenium-java/bin/chromedriver-$prefix.exe").absolutePath
            }
            else -> {
                throw Exception("Chrome Driver $prefix Not Found")
            }
        }
        System.setProperty(
            "webdriver.chrome.driver",
            //"D:\\Workspaces\\Kotlin\\.Kotlin Workspace\\selenium-java\\bin\\chromedriver-$prefix.exe"
            path
        )
    }

    /**
     * Maximize browser window
     */
    fun maximize(): TestChrome {
        (driver as ChromeDriver).manage().window().maximize()
        return this
    }

    fun setProxy(proxyStr: String): TestChrome {
        val proxy = Proxy()
        proxy.httpProxy = proxyStr.trim()
        proxy.sslProxy = proxyStr.trim()
        capabilities.setCapability("proxy", proxy)
        chromeOptions.setCapability("proxy", proxy)
        return this
    }

    /**
     * Set socks proxy
     * @param version socks version. default 5
     */
    fun setSocksProxy(
        proxyStr: String,
        version: Int = 5,
        username: String? = null,
        password: String? = null
    ): TestChrome {
        val proxy = Proxy()
        proxy.socksProxy = proxyStr
        if (username != null) {
            proxy.socksUsername = username
        }
        if (password != null) {
            proxy.socksPassword = password
        }
        proxy.socksVersion = version
        capabilities.setCapability("proxy", proxy)
        chromeOptions.setCapability("proxy", proxy)
        return this
    }

    /**
     * @see setSocksProxy
     */
    fun setSocksProxy(
        proxyStr: String,
        version: String = "5",
        username: String? = null,
        password: String? = null
    ): TestChrome {
        return setSocksProxy(proxyStr, version.toInt(), username, password)
    }

    fun detect() {
        get("http://git.webmanajemen.com/page/bot-detect.html")
    }

    fun isBrowserClosed(driver: WebDriver): Boolean {
        val wait = WebDriverWait(driver, 5)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("title")))

        var isClosed = false
        try {
            driver.title
        } catch (er: UnreachableBrowserException) {
            isClosed = true
        }
        return isClosed
    }

    @Throws(InterruptedException::class)
    fun google() {
        driver["http://www.google.com/"]
        Thread.sleep(5000) // Let the user actually see something!
        val searchBox = driver.findElement(By.name("q"))
        searchBox.sendKeys("ChromeDriver")
        searchBox.submit()
        Thread.sleep(5000) // Let the user actually see something!
        driver.quit()
    }

    fun setChromeBinary(bin: String): TestChrome {
        chromeOptions.setBinary(bin)
        return this
    }


    init {
        //val chromeBin = resource.getProp<String>("webdriver.chrome.binary")
        //val chromeDriver = resource.getProp<String>("webdriver.chrome.binary")
    }
}