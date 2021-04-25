package driver

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.stream.Collectors

import java.io.BufferedReader
import java.io.IOException

import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList

object Utils {
    @JvmStatic
    fun killWebDriver() {
        kill("geckodriver.exe")
    }

    @JvmStatic
    fun kill(process: String) {
        Runtime.getRuntime().exec(
            arrayOf(
                "taskkill.exe",
                "/f",
                "/im",
                process
            )
        )
    }

    @JvmStatic
    fun MD5(md5: String): String? {
        return try {

            // Static getInstance method is called with hashing MD5
            val md = MessageDigest.getInstance("MD5")

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            val messageDigest = md.digest(md5.toByteArray())

            // Convert byte array into signum representation
            val no = BigInteger(1, messageDigest)

            // Convert message digest into hex value
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } // For specifying wrong message digest algorithms
        catch (e: NoSuchAlgorithmException) {
            return null
        }
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    @Throws(IOException::class)
    @JvmStatic
    fun getResourceFileAsString(fileName: String?): String? {
        val classLoader = ClassLoader.getSystemClassLoader()
        classLoader.getResourceAsStream(fileName).use { inputStream ->
            if (inputStream == null) return null
            InputStreamReader(inputStream).use { isr ->
                BufferedReader(isr).use { reader ->
                    return reader.lines().collect(Collectors.joining(System.lineSeparator()))
                }
            }
        }
    }

    @JvmStatic
    fun smartCombineList(first: Iterable<*>, second: Iterable<*>): MutableList<Any?> {
        val result = mutableListOf<Any?>()
        for (num in second) {      // iterate through the second list
            if (!first.contains(num)) {   // if first list doesn't contain current element
                result.add(num) // add it to the first list
            }
        }
        result.addAll(first)
        return result
    }

    @JvmStatic
    fun println(vararg args: Any?) {
        if (args.size > 1) kotlin.io.println(args.toList())
        else kotlin.io.println(args[0])
    }
}

fun main() {
    Utils.killWebDriver()
    Utils.kill("java.exe")
}