package driver.proxy

import driver.Firefox
import driver.Utils
import driver.WebDriverData
import java.util.concurrent.TimeUnit

class SslProxiesOrg {
    private val result = mutableListOf<String>()
    fun scrape(): MutableList<String> {
        val data = WebDriverData()
        data.url = "https://www.sslproxies.org/"
        data.background = true
        val firefox = Firefox(data)
        firefox.get(data.url)
        firefox.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
        firefox.js()?.let { javascriptExecutor ->
            val jsfile = Utils.getResourceFileAsString("sslproxiesorg.js")
            val proxies = javascriptExecutor.executeScript(jsfile)
            firefox.quit()
            //Utils.killWebDriver()

            val split = proxies.toString().split("\n")
            split.forEach { sproxy ->
                if (sproxy.isNotEmpty()) result.add(sproxy.trim())
            }
        }
        println("SslProxies ${result.size}")
        return result
    }
}

fun main() {
    SslProxiesOrg().scrape()
}