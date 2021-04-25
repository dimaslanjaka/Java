package driver.proxy

import driver.RandomUserAgent
import driver.Utils
import driver.WebDriverData
import driver.firefox.EventHandler
import lib.File
import org.jsoup.Jsoup
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxDriverLogLevel
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.WebDriverWait
import string.RandomString
import java.io.*
import java.net.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Function
import java.util.logging.Level


object ProxyUtils {
    init {
        WebDriverData.setupDriver();
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val proxyFile = File("build/proxy.txt")
        //scrape(proxyFile)
        //println(getRandomProxyFromFile(proxyFile))
        //checkFromFile("build/proxy.txt")
        checkWebDriver(proxyFile)
    }

    @Suppress("unused")
    fun scrape(f: File) {
        val proxies = Utils.smartCombineList(SpysOne().scrape(), SslProxiesOrg().scrape())
        if (proxies.size > 0) {
            if (!f.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            }
            f.appendText("\n" + proxies.joinToString("\n"))
            uniqueProxyFromFile(f)
            //saveFile("build/proxy.txt", proxies.joinToString("\n"))
            //println(proxies)
        }
    }

    fun randomUnboundString(): String {
        return RandomString(8, ThreadLocalRandom.current()).toString()
    }

    fun checkWebDriver(filePath: File) {
        val proxies = uniqueProxyFromFile(filePath)
            .readText(Charset.defaultCharset())
            .split("\n") as MutableList<String>
        if (proxies.isEmpty()){
            scrape(filePath)
            return checkWebDriver(filePath)
        }
        proxies.forEach { proxy: String? ->
            if (proxy.isNullOrEmpty()) return@forEach
            val randomString = randomUnboundString()
            //println("p: $proxy, c: $randomString")
            EventHandler.cookieFile = File("build/cookies/${randomString}.json")
            val options = FirefoxOptions()
            val profile = FirefoxProfile()

            // initialize capability
            val capabilities = DesiredCapabilities()

            // set user agent
            val userAgent = RandomUserAgent.randomUserAgent
            options.addPreference("general.useragent.override", userAgent)

            // set proxy
            val proxyHandler = org.openqa.selenium.Proxy()
            proxyHandler.httpProxy = proxy
            proxyHandler.sslProxy = proxy
            capabilities.setCapability("proxy", proxyHandler)

            // other
            options.addArguments("--log-level=3");
            options.addArguments("--silent");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1400,800");
            System.setProperty("webdriver.chrome.silentOutput", "true");
            java.util.logging.Logger.getLogger("org.openqa.selenium").level = Level.OFF;

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
            var driver: WebDriver? = null
            try {
                driver = FirefoxDriver(options)
                driver.get("https://google.com")
                WebDriverWait(driver, 5).until { webDriver: WebDriver ->
                    (webDriver as JavascriptExecutor).executeScript(
                        "return document.readyState"
                    ) == "complete"
                }
                val title = driver.title.trim().toLowerCase()
                if (title != "google") {
                    proxies.remove(proxy)
                    println("$proxy not working ($title)")
                } else {
                    println("$proxy working ($title)")
                }
                driver.quit()
            } catch (e: Exception) {
                proxies.remove(proxy)
                driver?.quit()
            }
        }
        println("Working proxies ${proxies.size}")
        filePath.writeText(proxies.joinToString("\n"))
    }

    fun uniqueProxyFromFile(filePath: File): File {
        val textToTransfer = mutableSetOf<String>()
        var arrayStr = filePath
            .readText(Charset.defaultCharset())
            .split("\n")
            .toTypedArray()
        arrayStr = removeEmptyNullFromArray(arrayStr)
        arrayStr.forEach {
            textToTransfer.add(it)
        }
        filePath.writeText(textToTransfer.joinToString("\n"))
        return filePath
    }

    @JvmStatic
    fun checkFromFile(filePath: String) {
        val workingProxy = mutableListOf<String>()
        val proxies = readProxy(filePath)
        proxies.shuffle()
        proxies.forEachIndexed { _, proxy ->
            if (check(proxy.toString().trim())) {
                workingProxy.add(proxy.toString())
            }
        }
        saveProxy(filePath, workingProxy)
    }

    fun getRandomProxyFromFile(filePath: java.io.File): String {
        val txt = filePath.readText(Charset.defaultCharset()).split("\n")
        return (getRandom(txt) as String)
    }

    fun removeEmptyNullFromArray(stringArray: Array<String>): Array<String> {
        return Arrays.stream(stringArray)
            .filter { value: String? -> value != null && value.isNotEmpty() }
            .toArray { size: Int ->
                arrayOfNulls(
                    size
                )
            }
    }

    fun getRandom(array: IntArray): Int {
        val rnd = Random().nextInt(array.size)
        return array[rnd]
    }

    fun getRandom(array: Array<Any>): Any {
        val rnd = Random().nextInt(array.size)
        return array[rnd]
    }

    fun getRandom(array: List<Any>): Any {
        val rnd = Random().nextInt(array.size)
        return array[rnd]
    }

    @JvmStatic
    fun check(proxy: String): Boolean {
        try {
            val url = "http://www.google.com/"
            val ip = proxy.split(":")[0].trim()
            val port = proxy.split(":")[1].trim()
            println("Checking $ip:$port")
            val server = URL(url)
            val systemProperties = System.getProperties()
            systemProperties.setProperty("http.proxyHost", ip)
            systemProperties.setProperty("http.proxyPort", port)
            val proxy2test = Proxy(Proxy.Type.HTTP, InetSocketAddress(ip, port.toInt()))
            val connection = server.openConnection(proxy2test)
            connection.addRequestProperty("Client-Platform", "android")
            connection.addRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:28.0) Gecko/20100101 Firefox/28.0"
            )
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.useCaches = false
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val textBuilder = StringBuilder()
            BufferedReader(
                InputStreamReader(
                    inputStream,
                    Charset.forName(StandardCharsets.UTF_8.name())
                )
            ).use { reader ->
                var c: Int
                while (reader.read().also { c = it } != -1) {
                    textBuilder.append(c.toChar())
                }
            }
            val document = Jsoup.parse(textBuilder.toString())
            val title = document.title()
            return if (title != null && title.toString().trim().toLowerCase() == "google") {
                println("$proxy $title OK")
                true
            } else {
                println("$proxy $title Invalid")
                false
            }
        } catch (e: Exception) {
            println("$proxy ${e.message}")
        }
        return false
    }

    /**
     * Get my ip
     */
    @JvmStatic
    @Suppress("unused")
    fun myIp(): String? {
        val whatismyip = URL("http://checkip.amazonaws.com")
        val `in` = BufferedReader(
            InputStreamReader(
                whatismyip.openStream()
            )
        )
        return `in`.readLine() //you get the IP as a String
    }

    /**
     * Read proxy from file
     */
    @JvmStatic
    fun readProxy(filePath: String): MutableList<JProxy> {
        try {
            val f = File(filePath)
            val proxies: MutableList<JProxy> = ArrayList()
            val reader = BufferedReader(
                FileReader(f)
            )
            var proxy: String
            val iterator = reader.lines().iterator()
            while (iterator.hasNext()) {
                proxy = iterator.next()
                proxies.add(
                    JProxy(
                        proxy.split(":".toRegex()).toTypedArray()[0],
                        proxy.split(":".toRegex()).toTypedArray()[1]
                    )
                )
            }

            reader.close()
            return proxies
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    @JvmName("saveProxyFromListProxy")
    @JvmStatic
    fun saveProxy(filePath: String, proxies: MutableList<String>) {
        try {
            val joined: String = java.lang.String.join("\n", proxies)
            val f = File(filePath)
            val f2 = FileWriter(f, false)
            f2.write(joined)
            f2.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun saveFile(filePath: String, data: String) {
        try {
            val f = File(filePath)
            if (!f.parentFile.exists()) {
                f.parentFile.mkdirs()
            }
            val f2 = FileWriter(f, false)
            f2.write(data)
            f2.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    @JvmName("saveProxyFromListJProxy")
    fun saveProxy(filePath: String, proxies: MutableList<JProxy>) {
        try {
            val extract = mutableListOf<String>()
            proxies.forEach {
                extract.add("${it.host}:${it.port}")
            }
            val joined: String = java.lang.String.join("\n", extract)
            val f = File(filePath)
            val f2 = FileWriter(f, false)
            f2.write(joined)
            f2.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    class JProxy {
        val host: String
        val port: String

        constructor(h: String, p: String) {
            host = h
            port = p
        }

        constructor(proxy: String) {
            val split = proxy.split(":".toRegex()).toTypedArray()
            host = split[0].trim { it <= ' ' }
            port = split[1].trim { it <= ' ' }
        }

        override fun toString(): String {
            return "$host:$port"
        }
    }
}