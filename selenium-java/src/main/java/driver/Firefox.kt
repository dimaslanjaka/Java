package driver

import driver.proxy.ProxyUtils
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxDriverLogLevel
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger.getLogger


class Firefox {
    lateinit var drivercookie: WebDriverCookie
    lateinit var driver: WebDriver
    var firefoxOptions = FirefoxOptions()
    lateinit var firefoxProfile: FirefoxProfile
    lateinit var firefoxData: WebDriverData
    val capabilities = DesiredCapabilities()
    var proxyInit = Proxy()
    lateinit var cookieFile: File
    lateinit var proxyFile: File
    var isProxySet = false

    init {
        //println("Initializing new firefox instance ${Date()}")
        WebDriverData.setupDriver()
        //firefoxOptions.addArguments("start-maximized")
        //firefoxOptions.profile = firefoxProfile

        firefoxOptions.setAcceptInsecureCerts(true)
        //firefoxOptions.setHeadless(true)
    }

    @Suppress("unused")
    constructor(proxy: Proxy) {
        this.proxyInit = proxy
        setProxy2(proxy)
    }

    @Suppress("unused")
    constructor(url: String) {
        driver = FirefoxDriver(firefoxOptions)
        load(url)
    }

    @Suppress("unused")
    constructor(firefoxData: WebDriverData) {
        this.firefoxData = firefoxData
        firefoxOptions.setHeadless(firefoxData.background)
        firefoxOptions.setLogLevel(firefoxData.firefoxLogLevel)
        if (firefoxData.proxyFile != null) {
            proxyFile = firefoxData.proxyFile!!
            if (proxyFile.exists()) {
                val listReadedProxies = ProxyUtils.readProxy(proxyFile.absolutePath)
                listReadedProxies.shuffle()
                val proxyFromList = listReadedProxies.random()
                val proxy = Proxy()
                proxy.sslProxy = "${proxyFromList.host}:${proxyFromList.port}"
                proxy.httpProxy = "${proxyFromList.host}:${proxyFromList.port}"
                setProxy2(proxy)
                println("Proxy $proxy randomly from ${firefoxData.proxyFile}")
            }
        } else if (firefoxData.proxy != null) {
            val proxy = firefoxData.proxy!!
            setProxy(proxy)
            println("Proxy $proxy")
        }
        setProfile(firefoxData.firefoxProfile)
        firefoxData.cookieFile?.let {
            cookieFile = it;
            driver.let { it1 ->
                drivercookie = WebDriverCookie(it1, it)
            }
        }
        load(firefoxData.url)
    }

    constructor() {

    }

    constructor(options: FirefoxOptions) {
        firefoxOptions = options
        driver = FirefoxDriver(firefoxOptions)
    }

    fun silent() {
        System.setProperty("webdriver.chrome.silentOutput", "true")
        getLogger("org.openqa.selenium").level = Level.OFF
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.FATAL)
    }

    @Suppress("unused")
    fun load(url: String) {
        try {
            driver.get(url)
            /*
            val runnable = Runnable {
                while (true) {
                    Thread.sleep(1000)
                    if (this::cookieFile.isInitialized) {
                        drivercookie.saveCookie(cookieFile)
                    }
                }
            }
            Thread(runnable).start()
             */
        } catch (e: Exception) {
            driver.quit()
            if (isProxySet) {
                Firefox(this.firefoxData)
            }
        }
    }

    fun setProfile(profileName: String?) {
        val locationProfile = File(WebDriverData.tempFolder, "Selenium Profile/$profileName")
        if (!locationProfile.exists()) locationProfile.mkdirs()

        firefoxProfile = FirefoxProfile(locationProfile)
        /*
        firefoxProfile = if (profileName == null) {
            ProfilesIni().getProfile("default")
        } else {
            ProfilesIni().getProfile(profileName)
        }
         */
        firefoxProfile.setAcceptUntrustedCertificates(true)
        firefoxProfile.setAssumeUntrustedCertificateIssuer(true)
        firefoxProfile.setPreference("browser.cache.disk.enable", false)
        firefoxProfile.setPreference("browser.cache.memory.enable", false)
        firefoxProfile.setPreference("browser.cache.offline.enable", false)
        firefoxProfile.setPreference("network.http.use-cache", false)
        firefoxOptions.profile = firefoxProfile
        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile)
        if (this::driver.isInitialized) driver.quit()
        driver = FirefoxDriver(firefoxOptions)
    }

    @Suppress("unused")
    fun setProxy(ip: String, port: String) {
        setProxy("$ip:$port")
    }

    fun setProxy2(proxy: Proxy) {
        firefoxOptions.setProxy(proxy)
        driver = FirefoxDriver(firefoxOptions)
        isProxySet = true
    }

    @Suppress("unused")
    fun setProxy(proxy: String) {
        isProxySet = true
        proxyInit.httpProxy = proxy
        proxyInit.sslProxy = proxy
        capabilities.setCapability("proxy", proxyInit)
        capabilities.setCapability(ChromeOptions.CAPABILITY, firefoxOptions)
        driver = FirefoxDriver(capabilities)
    }

    fun setLogLevel(type: FirefoxDriverLogLevel) {
        firefoxOptions.setLogLevel(type)
    }

    fun js(): JavascriptExecutor? {
        return if (driver is JavascriptExecutor) {
            driver as JavascriptExecutor
        } else {
            null
        }
    }

    @Suppress("unused")
    fun setProxy2(proxy: String) {
        proxyInit.httpProxy = proxy
        proxyInit.sslProxy = proxy
        firefoxOptions.setProxy(proxyInit)
        driver = FirefoxDriver(firefoxOptions)
    }

    @Suppress("unused")
    fun findElements(by: By?): MutableList<WebElement> {
        return driver.findElements(by)
    }

    fun findElement(by: By?): WebElement {
        return driver.findElement(by)
    }

    fun get(url: String?) {
        return driver.get(url)
    }

    fun getCurrentUrl(): String {
        return driver.currentUrl
    }

    fun getTitle(): String {
        return driver.title
    }

    fun getPageSource(): String {
        return driver.pageSource
    }

    fun close() {
        return driver.close()
    }

    fun quit() {
        val quit = driver.quit()
        //driver = null
        return quit
    }

    fun quitAll() {
        val quit = driver.quit()
        Utils.killWebDriver()
        return quit
    }

    fun getWindowHandles(): MutableSet<String> {
        return driver.windowHandles
    }

    fun getWindowHandle(): String {
        return driver.windowHandle
    }

    fun switchTo(): WebDriver.TargetLocator {
        return driver.switchTo()
    }

    fun navigate(): WebDriver.Navigation {
        return driver.navigate()
    }

    fun manage(): WebDriver.Options {
        return driver.manage()
    }
}