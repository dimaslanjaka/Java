package driver.proxy

import java.util.regex.Pattern

class Parser {
    var result = mutableListOf<String>()

    constructor(page: String) {
        standard(page)
    }

    constructor() {}

    fun parseIp(page: String): String? {
        val matcher = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})").matcher(page)
        var res: String? = null
        while (matcher.find()) {
            try {
                res = matcher.group(1)
                break
            } catch (e: IllegalArgumentException) {
                /* Ignore invalid ip */
            }
        }
        return res
    }

    fun standard(page: String) {
        val matcher = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]{1,7})").matcher(page)

        while (matcher.find()) {
            try {
                val ip = matcher.group(1)
                val port = matcher.group(2)
                println("found $ip:$port")
                result.add("$ip:$port")
            } catch (e: IllegalArgumentException) {
                /* Ignore invalid proxies */
            }
        }
    }

    override fun toString(): String {
        return result.joinToString("\n")
    }
}