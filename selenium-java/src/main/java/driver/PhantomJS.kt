package driver

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait


class PhantomJS {
    init {
        WebDriverData.setupDriver()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val driver: WebDriver = PhantomJSDriver()
                // And now use this to visit Google
                driver.get("http://www.google.com")
                // Alternatively the same thing can be done like this
                // driver.navigate().to("http://www.google.com");

                // Find the text input element by its name
                val element: WebElement = driver.findElement(By.name("q"))

                // Enter something to search for
                element.sendKeys("Cheese!")

                // Now submit the form. WebDriver will find the form for us from the element
                element.submit()

                // Check the title of the page
                println("Page title is: " + driver.title)

                // Google's search is rendered dynamically with JavaScript.
                // Wait for the page to load, timeout after 10 seconds
                WebDriverWait(driver, 10).until(object : ExpectedCondition<Boolean?> {
                    override fun apply(d: WebDriver?): Boolean {
                        if (d != null) {
                            return d.title.toLowerCase().startsWith("cheese!")
                        }
                        return false
                    }
                })

                // Should see: "cheese! - Google Search"
                println("Page title is: " + driver.title)

                //Close the browser
                driver.quit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}