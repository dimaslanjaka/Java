package driver

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import driver.TestChrome.driver
import org.openqa.selenium.Cookie
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxDriverLogLevel
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.BufferedReader
import java.io.File
import java.util.*
import java.util.logging.Level


object FirefoxTest {
    val webip = "https://whatismyipaddress.com/"
    val webinfo = "https://www.whatismybrowser.com/"

    init {
        WebDriverData.setupDriver()
    }

    @kotlin.jvm.JvmStatic
    fun main(args: Array<String>) {
        val profileDirectory = File("build/profile/firefox")
        if (!profileDirectory.exists()) profileDirectory.mkdirs()
        val profile = FirefoxProfile(profileDirectory)
        profile.setPreference("network.proxy.no_proxies_on", "localhost");
        profile.setPreference("javascript.enabled", true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        profile.setAcceptUntrustedCertificates(false);
        profile.setPreference("app.update.enabled", false);
        profile.setPreference("network.negotiate-auth.allow-insecure-ntlm-v1", true);

        val capabilities = DesiredCapabilities()
        capabilities.setCapability(FirefoxDriver.PROFILE, profile)

        val options = FirefoxOptions()
        options.addPreference("general.useragent.override", RandomUserAgent.randomUserAgent)

        val proxy = org.openqa.selenium.Proxy()
        proxy.httpProxy = "187.243.240.54:8080"
        proxy.sslProxy = "187.243.240.54:8080"
        //capabilities.setCapability("proxy", proxy)
        //capabilities.setCapability(ChromeOptions.CAPABILITY, options)

        options.merge(capabilities)
        options.setLogLevel(FirefoxDriverLogLevel.fromLevel(Level.FINEST))
        //options.addPreference("browser.link.open_newwindow", 3)
        options.addPreference("browser.link.open_newwindow.restriction", 0)

        loadUrl(options, "https://dimaslanjaka.github.io/page/cookies.html?${Date()}")
        while (true) {
            Thread.sleep(5000)
            saveCookies()
        }
    }

    fun waitForPageLoad() {
        val wait: Wait<WebDriver> = WebDriverWait(driver, 30)
        wait.until(object : ExpectedCondition<Boolean?> {
            override fun apply(wdriver: WebDriver?): Boolean {
                return (driver as JavascriptExecutor).executeScript(
                    "return document.readyState"
                ) == "complete"
            }
        })


        /*
        val allCookies: Set<Cookie> = driver.manage().cookies
        for (cookie in allCookies) {
            val domain = cookie.domain
            var expiry = cookie.expiry
            if (expiry == null) {
                expiry = Date()
            }
            var path = cookie.path
            val name = cookie.name
            val value = cookie.value
            val httpOnly = cookie.isHttpOnly
            val secure = cookie.isSecure
            //println(cookie.toJson())
        }
        //restore all cookies from previous session
        for (cookie in allCookies) {
            driver.manage().addCookie(cookie)
        }
         */
    }

    val gson: Gson = com.google.gson.GsonBuilder().setPrettyPrinting().create()
    var cookieFile = File("build/cookie.json")
    fun saveCookies() {
        try {
            val allCookies: Set<Cookie> = driver.manage().cookies
            val json: String = gson.toJson(allCookies)
            cookieFile.writeText(json)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun loadCookie() {
        try {
            val bufferedReader: BufferedReader = cookieFile.bufferedReader()
            val json = bufferedReader.use { it.readText() }
            //println(json)
            val httpCookies: List<Cookie?>
            val type = object : TypeToken<List<Cookie?>?>() {}.type
            httpCookies = gson.fromJson(json, type) // convert json string to list
            for (cookie in httpCookies) {
                if (cookie is Cookie) {
                    //driver.manage().cookies.add(cookie)
                    //driver.manage().addCookie(cookie)
                    driver.manage().addCookie(Cookie(cookie.name, cookie.value))
                }
                //println("C: " + cookie.toString())
            }
            driver.navigate().refresh()
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }
    }

    fun loadUrl(options: FirefoxOptions, url: String) {
        driver = FirefoxDriver(options)
        ///WebDriverCookie(driver)
        try {
            driver.get(url)
            //waitForPageLoad()
            loadCookie()
        } catch (e: Exception) {
            val currentUrl = driver.currentUrl
            driver.quit()
            //loadUrl(options, currentUrl)
            Utils.killWebDriver()
            e.printStackTrace()
        }
    }

    fun wData() {
        val data = WebDriverData()
        data.url = webinfo
        data.proxyFile = File("build/proxy.txt")
        data.cookieFile = File("build/Cookies.data")
        data.firefoxProfile = "dimas"
        val init = Firefox(data)
    }
}