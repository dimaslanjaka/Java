package com.neptune.domino

import android.os.Build
import androidx.annotation.RequiresApi
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Collectors
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object XML {
    @JvmStatic
    fun main(argv: Array<String>) {
        val filepath = "Cocos2dxPrefsFile.xml"
    }

    @JvmStatic
    @Throws(java.lang.Exception::class)
    fun loadXMLFromString(xml: String?): Document? {
        val factory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val `is` = InputSource(StringReader(xml))
        return builder.parse(`is`)
    }

    @JvmStatic
    fun loadXMLFromFile(filepath: String): Document? {
        val docFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = docFactory.newDocumentBuilder()
        return docBuilder.parse(getResourceAsStream(filepath))
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun modifySharedXml(xml: String, outputFile: File) {
        //Log.d(outputFile.absolutePath, xml)
        try {
            val doc = loadXMLFromString(xml)
            if (doc != null) {
                val xPath: XPath = XPathFactory.newInstance().newXPath()

                val mapper = doc.getElementsByTagName("map").item(0)

                val strings = doc.getElementsByTagName("string")
                for (node in object : Iterable<Node?> {
                    override fun iterator(): MutableIterator<Node?> {
                        return object : MutableIterator<Node?> {
                            var index = 0
                            override fun hasNext(): Boolean {
                                return index < strings.length
                            }

                            override fun next(): Node {
                                return if (hasNext()) {
                                    strings.item(index++)
                                } else {
                                    throw NoSuchElementException()
                                }
                            }

                            override fun remove() {
                                throw UnsupportedOperationException()
                            }
                        }
                    }
                }) {
                    if (node != null) {
                        val attrname = getAttribute(node, "name")
                        if (attrname != null) {
                            when (attrname.nodeValue) {
                                "slot_total_play_money" -> {
                                    var inner = node.textContent.toLong()
                                    inner += randLong(9000000000, 90000000000)
                                    node.textContent = inner.toString()
                                }
                                "score" -> {
                                    var inner = node.textContent.toLong()
                                    inner += randInt(2000, 100000).toLong()
                                    node.textContent = inner.toString();
                                }
                                "firstGame" -> {
                                    setText(node, "1")
                                }
                            }
                        }
                    }
                }

                val ints = doc.getElementsByTagName("int")
                for (node in object : Iterable<Node?> {
                    override fun iterator(): MutableIterator<Node?> {
                        return object : MutableIterator<Node?> {
                            var index = 0
                            override fun hasNext(): Boolean {
                                return index < ints.length
                            }

                            override fun next(): Node {
                                return if (hasNext()) {
                                    ints.item(index++)
                                } else {
                                    throw NoSuchElementException()
                                }
                            }

                            override fun remove() {
                                throw UnsupportedOperationException()
                            }
                        }
                    }
                }) {
                    if (node != null) {
                        val attrname = getAttribute(node, "name")
                        when (attrname?.nodeValue) {
                            "lastIntoGameID" -> {
                                mapper.removeChild(node)
                            }
                            "slot_total_play_game_num" -> {
                                val vall = node.attributes.getNamedItem("value")
                                val rep = vall.nodeValue.toLong() + randLong(10000, 100000)
                                vall.nodeValue = rep.toString()
                            }
                            "lastGame" -> {
                                setAttribute(node, "value", "0")
                            }
                        }
                    }
                }

                for (i in 0..100) {
                    val attrname = "jackpot_$i"
                    val match =
                        xPath.compile("//map/string[@name='$attrname']/text()")
                            .evaluate(doc, XPathConstants.NODESET) as NodeList

                    if (match.length == 0) {
                        val create = doc.createElement("string")
                        create.textContent = randLong(9000000000, 900000000000).toString()
                        create.setAttribute("name", attrname)
                        mapper.appendChild(create)
                    }
                }
                saveResult(doc, outputFile)
            };
        } catch (pce: Exception) {
            pce.printStackTrace()
        }
    }

    fun saveResult(doc: Document, filepath: File) {
        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        val source = DOMSource(doc)
        val result = StreamResult(filepath)
        transformer.transform(source, result)
    }

    fun setupStorage() {

    }

    fun dumpResult(doc: Document) {
        val tf: TransformerFactory = TransformerFactory.newInstance()
        val transformer: Transformer = tf.newTransformer()
        val source = DOMSource(doc)
        val result = StreamResult(System.out)
        transformer.transform(source, result)
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * `Integer.MAX_VALUE - 1`.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random.nextInt
     */
    fun randInt(min: Int, max: Int): Int {
        val rand = Random()
        return rand.nextInt(max - min + 1) + min
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun randLong(min: Long, max: Long): Long {
        return ThreadLocalRandom.current().nextLong(min, max)
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    @JvmStatic
    @Suppress("unused")
    fun getResourceFileAsString(fileName: String?): String? {
        val classLoader = ClassLoader.getSystemClassLoader()
        classLoader.getResourceAsStream(fileName).use { `is` ->
            if (`is` == null) return null
            InputStreamReader(`is`).use { isr ->
                BufferedReader(isr).use { reader ->
                    return reader.lines().collect(
                        Collectors.joining(System.lineSeparator())
                    )
                }
            }
        }
    }

    fun getResourceAsStream(fileName: String?): InputStream? {
        val classLoader = ClassLoader.getSystemClassLoader()
        return classLoader.getResourceAsStream(fileName)
    }

    fun setAttribute(node: Node, key: String, value: String) {
        val keynode = node.attributes.getNamedItem(key)
        keynode?.nodeValue = value
    }

    fun getAttribute(node: Node, key: String): Node? {
        return node.attributes.getNamedItem(key)
    }

    fun setText(node: Node, content: String) {
        node.textContent = content
    }

    @Throws(java.lang.Exception::class)
    fun convertStreamToString(`is`: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

    @Throws(java.lang.Exception::class)
    fun getStringFromFile(filePath: String): String? {
        try {
            val fl = File(filePath)
            return if (fl.exists()) {
                val fin = FileInputStream(fl)
                val ret = convertStreamToString(fin)
                //Make sure you close all streams.
                fin.close()
                ret
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun validateXml(fXmlFile: File): Boolean {
        try {
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(fXmlFile)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}