import com.dimaslanjaka.library.helper.File
import org.junit.jupiter.api.Test
import java.net.HttpCookie
import java.net.URI

class CookieTest {
    val manager = java.net.CookieManager()

    @Test
    fun createHttpCookie() {
        val uri = URI("https://m.facebook.com/home.php")
        val httpCookie = HttpCookie("name", "value")
        httpCookie.domain = uri.host
        manager.cookieStore.add(uri, httpCookie)
        val json = gson().toJson(manager.cookieStore.cookies)

        val functionName = object {}.javaClass.enclosingMethod.name
        File.write("build/test-results/${javaClass.name}/${functionName}.json", json)
    }

    @Test
    fun parseHttpCookie() {
        val headers = StringBuilder()
        headers.append("set-cookie: __Secure-3PSIDCC=AJi4QfH4CNyLEOZtIHsfcKM8lWZ46dRMFZlhuZ-gJg4gT1LvmuATqzpHLD8257MgDTuRspYemg; expires=Thu, 28-Apr-2022 17:22:51 GMT; path=/; domain=.google.com; Secure; HttpOnly; priority=high; SameSite=none")
            .append("\n")
        val parsecookie = HttpCookie.parse(headers.toString())
        println(gson().toJson(parsecookie))
    }

    /**
     * Get the method name for a depth in call stack. <br></br>
     * Utility function
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    fun getMethodName(depth: Int = 0): String? {
        val ste = Thread.currentThread().stackTrace

        //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
        return ste[ste.size - 1 - depth].methodName //Thank you Tom Tresansky
    }
}