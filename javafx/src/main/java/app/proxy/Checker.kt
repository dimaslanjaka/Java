package app.proxy

import org.apache.http.HttpHost
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.params.ConnRoutePNames
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.CoreConnectionPNames
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.util.logging.Logger
import java.util.regex.Pattern
import kotlin.properties.Delegates

object Checker {
    var google by Delegates.notNull<Boolean>()

    val allProxies = mutableListOf<String>()
    val workingProxy = mutableListOf<String>()
    val deadProxy = mutableListOf<String>()
    val log = Logger.getLogger(Checker::class.java.name)
    var regex =
        "(\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b):?(\\d{2,5})"

    @Throws(IOException::class)
    @Suppress("unused")
    fun parseProxy(file: String): MutableList<String> {
        val proxyFile = File(file)
        if (!proxyFile.exists()) {
            throw RuntimeException("Unable to find proxy file: " + proxyFile.absolutePath)
        }
        val proxies = proxyFile.readText(Charset.defaultCharset())
        val pattern = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher = pattern.matcher(proxies)
        while (matcher.find()) {
            val fullMatch = matcher.group(0)
            val ip = matcher.group(1)
            val port = matcher.group(2)
            if (ip != null
                && port != null
                && !allProxies.contains(fullMatch)
            ) {
                allProxies.add(fullMatch)
            }
        }
        return allProxies
    }

    @Suppress("unused")
    @Throws(IOException::class)
    fun parseProxy(file: File) {
        parseProxy(file.absolutePath)
    }

    @Suppress("unused")
    @Throws(IOException::class)
    fun check(host: String, port: Int): Boolean {
        try {
            val addr = InetAddress.getByName(host)
            if (addr.isReachable(5000)) {
                if (google) {
                    return checkGoogle(host, port)
                }
            }
        } catch (e: UnknownHostException) {
            try {
                val socket = Socket(host, port)
                val addr = InetSocketAddress("http://www.google.com", 80)
                socket.connect(addr, 10000)
                if (socket.isConnected) {
                    return true
                }
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    @Suppress("unused")
    fun checkGoogle(host: String, port: Int): Boolean {
        var connected = false
        try {
            val client: HttpClient = DefaultHttpClient()
            val get = HttpGet("http://www.google.com/")
            val proxy = HttpHost(host, port)
            client.params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy)
            client.params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000)
            val response = client.execute(get)
            if (response != null) {
                connected = true
            }
        } catch (ignored: Exception) {
        }
        if (!connected) {
            try {
                val socket = Socket(host, port)
                val addr = InetSocketAddress("http://www.google.com", 80)
                socket.connect(addr, 10000)
                if (socket.isConnected) {
                    connected = true
                }
            } catch (ignored: IOException) {
            }
        }
        return connected
    }

    @JvmStatic
    fun main(args: Array<String>) {
        google = true
        val proxies = parseProxy(File("build/proxy.txt").absolutePath)
        check(proxies)
    }

    fun check(proxies: MutableList<String>) {
        proxies.forEach { proxy ->
            //log.info("checking $proxy")
            val split = proxy.split(":")
            if (split.size > 1) {
                if (check(split[0], split[1].toInt())) {
                    workingProxy.add(proxy)
                } else {
                    deadProxy.add(proxy)
                }
            }
        }
    }
}