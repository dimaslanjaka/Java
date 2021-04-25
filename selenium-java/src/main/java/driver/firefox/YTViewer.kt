package driver.firefox

import driver.RandomUserAgent
import driver.Utils
import driver.Utils.println
import driver.WebDriverData
import driver.firefox.EventHandler.Companion.cookieFile
import driver.proxy.ProxyUtils.getRandomProxyFromFile
import lib.File
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxDriverLogLevel
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.events.EventFiringWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.function.Function
import java.util.logging.Level

class YTViewer {
    init {
        WebDriverData.setupDriver();
    }

    companion object {
        private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        lateinit var options: FirefoxOptions
        lateinit var profile: FirefoxProfile
        lateinit var driver: WebDriver
        lateinit var eventDriver: EventFiringWebDriver

        @JvmStatic
        fun main(args: Array<String>) {
            val randomString = (1..10)
                .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");
            cookieFile = File("build/cookies/${randomString}.json")
            options = FirefoxOptions()
            profile = FirefoxProfile()

            // initialize capability
            val capabilities = DesiredCapabilities()

            // set user agent
            val userAgent = RandomUserAgent.randomUserAgent
            options.addPreference("general.useragent.override", userAgent)

            // set proxy
            val proxyFile = File("build/proxy.txt")
            val proxyStr = getRandomProxyFromFile(proxyFile)
            val proxy = org.openqa.selenium.Proxy()
            proxy.httpProxy = proxyStr
            proxy.sslProxy = proxyStr
            capabilities.setCapability("proxy", proxy)

            // background / headless
            //options.addArguments("--headless");

            // other
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1400,800")

            // disable logs
            options.addArguments("--log-level=3");
            options.addArguments("--silent");

            // disable gps
            profile.setPreference("geo.enabled", false);
            profile.setPreference("geo.provider.use_corelocation", false);
            profile.setPreference("geo.prompt.testing", false);
            profile.setPreference("geo.prompt.testing.allow", false);

            // merging capabilities to options
            options.merge(capabilities)
            options.setLogLevel(FirefoxDriverLogLevel.fromLevel(Level.OFF))
            //options.addPreference("browser.link.open_newwindow", 3)
            options.addPreference("browser.link.open_newwindow.restriction", 0)
            capabilities.setCapability(FirefoxDriver.PROFILE, profile)

            // start driver
            driver = FirefoxDriver(options)
            eventDriver = EventFiringWebDriver(driver)
            val handler = EventHandler()
            eventDriver.register(handler)
            try {
                eventDriver.get("https://youtu.be/bgn8zqvchQw")
                //.get("https://youtu.be/stFcDti365U")
                //.get("https://dimaslanjaka.github.io/page/cookies.html")
                //.get("https://bit.ly/37r8klT")

                var counter = 0;
                val mapCookie = mutableMapOf<String, Boolean?>()
                var played = false
                while (counter < 200) {
                    val ct = eventDriver.currentUrl.trim()
                    val domain = EventHandler.getDomainName(ct)
                    var viewCount = ""
                    if (mapCookie[domain] == true) {
                        // TODO: save cookie each url
                        EventHandler.saveCookies(eventDriver)
                    } else if (!StringUtils.startsWithAny(
                            ct,
                            "https://accounts.google.com/ServiceLogin",
                            "https://accounts.google.com/signin/v2",
                            "https://youtube.com",
                            "https://www.google.com/sorry/index"
                        )
                        // if mapcookie of domain is null. it excluded
                        && mapCookie[domain] != null
                    ) {
                        // TODO: load cookie each url
                        println("load cookie $ct")
                        EventHandler.loadCookie2(eventDriver)
                        mapCookie[domain] = true
                        eventDriver.navigate().refresh()
                    }

                    wait(driver)

                    println(driver.title)
                    if (!played && ct.startsWith("https://www.youtube.com/watch")) {
                        if (driver.title.trim().toLowerCase() != "youtube") {
                            val wait = WebDriverWait(driver, 20)
                            val playerIframe: WebElement =
                                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ytd-player")))
                            playerIframe.click()

                            val playerViewCount = eventDriver.findElement(By.cssSelector(".view-count"))
                            println("Views: ${playerViewCount.text}")
                            played = true
                        } else {
                            quit()
                            proxyFile.removeLineFromFile(proxyStr)
                            break;
                        }
                    }

                    val captchaPage = driver.findElements(By.id("captcha-page-content")).size != 0
                    if (driver.title.trim().startsWith("https://www.google.com/sorry/index") || captchaPage) {
                        quit()
                        proxyFile.removeLineFromFile(proxyStr)
                        break;
                    }
                    Thread.sleep(1000)
                    counter++
                }
            } catch (e: Exception) {
                //e.printStackTrace()
                println(e.message)
                quit()
            }
        }

        fun quit() {
            eventDriver.quit()
            driver.quit()
            Utils.killWebDriver()
        }

        fun wait(driver: WebDriver) {
            WebDriverWait(driver, 5).until<Boolean>(
                Function { webDriver: WebDriver ->
                    (webDriver as JavascriptExecutor).executeScript(
                        "return document.readyState"
                    ) == "complete"
                })
        }
    }
}