package driver.firefox

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.openqa.selenium.*
import org.openqa.selenium.support.events.WebDriverEventListener
import java.io.BufferedReader
import java.io.File
import java.net.URI


class EventHandler : WebDriverEventListener {
    override fun beforeAlertAccept(driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterAlertAccept(driver: WebDriver) {}
    override fun afterAlertDismiss(driver: WebDriver) {}
    override fun beforeAlertDismiss(driver: WebDriver) {}
    override fun beforeNavigateTo(url: String, driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterNavigateTo(url: String, driver: WebDriver) {
        println(getMethodName(2) + " $url")
    }

    override fun beforeNavigateBack(driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterNavigateBack(driver: WebDriver) {
        println("Inside the after navigateback to " + driver.currentUrl)
    }

    override fun beforeNavigateForward(driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterNavigateForward(driver: WebDriver) {
        println("Inside the afterNavigateForward to " + driver.currentUrl)
    }

    override fun beforeNavigateRefresh(driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterNavigateRefresh(driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun beforeFindBy(by: By, element: WebElement, driver: WebDriver) {}
    override fun afterFindBy(by: By, element: WebElement, driver: WebDriver) {}
    override fun beforeClickOn(element: WebElement, driver: WebDriver) {}
    override fun afterClickOn(element: WebElement, driver: WebDriver) {
        println("inside method afterClickOn on $element")
    }

    override fun beforeChangeValueOf(element: WebElement, driver: WebDriver, keysToSend: Array<CharSequence>) {}
    override fun afterChangeValueOf(element: WebElement, driver: WebDriver, keysToSend: Array<CharSequence>) {}
    override fun beforeScript(script: String, driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterScript(script: String, driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun beforeSwitchToWindow(windowName: String, driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun afterSwitchToWindow(windowName: String, driver: WebDriver) {
        val name = object : Any() {}.javaClass.enclosingMethod.name
        println(name)
    }

    override fun onException(throwable: Throwable, driver: WebDriver) {}
    override fun <X> beforeGetScreenshotAs(target: OutputType<X>) {}
    override fun <X> afterGetScreenshotAs(target: OutputType<X>, screenshot: X) {}
    override fun beforeGetText(element: WebElement, driver: WebDriver) {}
    override fun afterGetText(element: WebElement, driver: WebDriver, text: String) {}

    companion object {
        var cookieFile = File("build/cookie.json")
        val gson: Gson = com.google.gson.GsonBuilder().setPrettyPrinting().create()
        var cookies = mutableMapOf<String, Set<Cookie?>>()

        /**
         * Get the method name for a depth in call stack. <br></br>
         * Utility function
         *
         * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
         * @return method name
         */
        fun getMethodName(depth: Int): String {
            val ste = Thread.currentThread().stackTrace
            //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
            // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
            return ste[ste.size - 1 - depth].methodName //Thank you Tom Tresansky
        }

        fun saveCookies(driver: WebDriver) {
            try {
                resolveCookie()
                val allCookies: Set<Cookie> = driver.manage().cookies
                cookies[getDomainName(driver.currentUrl)] = allCookies
                val json: String = gson.toJson(cookies)
                cookieFile.writeText(json)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun loadCookie2(driver: WebDriver): Boolean {
            try {
                resolveCookie()
                val bufferedReader: BufferedReader = cookieFile.bufferedReader()
                val json = bufferedReader.use { it.readText() }
                val type = object : TypeToken<MutableMap<String, Set<Cookie?>>?>() {}.type
                cookies = gson.fromJson(json, type)

                val cookieCurrentUrl = cookies[getDomainName(driver.currentUrl)] ?: return false
                cookieCurrentUrl.forEach {
                    if (it is Cookie) driver.manage().addCookie(it)
                }

                val cookiesCollection: Collection<Cookie>
                val mapper = mutableListOf<Cookie>()

                cookies.forEach { (_, cookie) ->
                    cookie.forEach {
                        if (it is Cookie) {
                            mapper.add(it)
                        }
                    }
                }
                cookiesCollection = mapper
                driver.manage().cookies.addAll(cookiesCollection)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun loadCookie(driver: WebDriver) {
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

        fun getDomainName(url: String): String {
            return try {
                val uri = URI(url)
                val domain: String = uri.host
                if (domain.startsWith("www.")) {
                    domain.substring(4)
                } else {
                    domain
                }
            } catch (e: Exception) {
                ""
            }
        }

        private fun resolveCookie() {
            if (!cookieFile.exists()) {
                cookieFile.parentFile.mkdirs()
                cookieFile.writeText("[]")
            }
        }
    }
}