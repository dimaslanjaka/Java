package com.dimaslanjaka.library.helper

import com.dimaslanjaka.kotlin.ConsoleColors.Companion.println
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

class File : com.dimaslanjaka.gradle.plugin.File {
    lateinit var file: File

    constructor(pathname: String) : super(pathname) {
        this.file = File(pathname)
    }

    constructor(parent: String?, child: String) : super(parent, child) {
        this.file = File(parent, child)
    }

    constructor(parent: File?, child: String) : super(parent, child) {
        this.file = File(parent, child)
    }

    constructor(uri: URI) : super(uri) {}
    constructor(file: File) : super(file) {
        this.file = file
    }

    @Suppress("unused")
    fun isEmpty(): Boolean {
        return file.readText(Charset.defaultCharset()).isEmpty()
    }

    fun readText(charset: Charset = Charset.defaultCharset()): String {
        return file.readText(charset)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val opt: Optional<*> = getFileExtension(URL("http://www.example.com/stuff.zip"))
                if (opt.isPresent) {
                    println(opt.get())
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun getFileExtension(url: URL): Optional<String> {
            Objects.requireNonNull(url, "url is null")
            val file = url.file
            if (file.contains(".")) {
                val sub = file.substring(file.lastIndexOf('.') + 1)
                if (sub.isEmpty()) {
                    return Optional.empty()
                }
                return if (sub.contains("?")) {
                    Optional.of(sub.substring(0, sub.indexOf('?')))
                } else Optional.of(sub)
            }
            return Optional.empty()
        }

        /**
         * Format file path to default System file separated
         * EG: windows (\\) or Linux (/)
         */
        @JvmStatic
        @Suppress("unused")
        fun separatorsToSystem(res: Any): String {
            val target = when (res) {
                is File -> {
                    res.absolutePath
                }
                is String -> {
                    res
                }
                else -> {
                    res.toString()
                }
            }
            val windows = System.getProperty("os.name").toLowerCase().contains("win")
            return if (windows) {
                // From Windows to Linux/Mac
                target.replace('/', File.separatorChar)
            } else {
                // From Linux/Mac to Windows
                target.replace('\\', File.separatorChar)
            }
        }

        @JvmStatic
        fun getFileExtension(urlImage: String): Optional<String>? {
            return try {
                getFileExtension(URL(urlImage))
            } catch (e: MalformedURLException) {
                null
            }
        }

        @JvmStatic
        @JvmName("write_file_helper")
        fun write(absolutePath: File, fileContent: MutableMap<String, Any>): Boolean {
            return write(absolutePath.absolutePath, fileContent)
        }

        @JvmStatic
        @JvmName("write_file_helper")
        fun write(absolutePath: String, fileContent: String): Boolean {
            resolve(absolutePath)
            // create file if not exist
            if (!File(absolutePath).exists()) create(absolutePath)
            // Write the content in file
            try {
                val fileWriter = FileWriter(absolutePath)
                fileWriter.write(fileContent)
                fileWriter.close()
                return true
            } catch (e: IOException) {
                // Cxception handling
            }
            return false
        }

        @JvmStatic
        @JvmName("write_object_file_helper")
        fun write(absolutePath: String, fileContent: Any?): Boolean {
            resolve(absolutePath)
            if (fileContent is String || fileContent is Boolean ||
                fileContent is Number || fileContent == null
            ) {
                return write(absolutePath, fileContent.toString())
            }
            return writeJSON(absolutePath, fileContent)
        }

        @JvmStatic
        @JvmName("create_file_helper")
        fun create(absolutePath: String): Boolean {
            resolve(absolutePath)
            try {
                val myObj = File(absolutePath)
                if (myObj.createNewFile()) {
                    return true
                }
            } catch (e: IOException) {
                println("An error occurred.")
                e.printStackTrace()
            }
            return false
        }

        @JvmStatic
        fun read(absolutePath: File): String {
            return read(absolutePath.absolutePath)
        }

        @JvmStatic
        @JvmName("read_file_helper")
        fun read(absolutePath: String): String {
            resolve(absolutePath)
            return File(absolutePath).readText(Charset.defaultCharset())
        }

        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

        /*
        @JvmStatic
        @Throws(Exception::class)
        fun readJSON(absolutePath: String, clazz: Class): Any? {
            resolve(absolutePath)
            return gson.fromJson(read(absolutePath), clazz)
        }
         */

        @JvmStatic
        fun writeJSON(absolutePath: String, fileContent: Any): Boolean {
            val gson = Var.gson
            return write(absolutePath, gson.toJson(fileContent))
        }

        private fun resolve(absolutePath: String) {
            val file = File(absolutePath)
            if (!file.parentFile.exists()) file.parentFile.mkdirs();
        }

        /**
         * Is file modified in Milliseconds
         */
        @JvmStatic
        fun modifiedInMilliseconds(file: File, i: Int): Boolean {
            val now = Date()
            return TimeUnit.MILLISECONDS.toMillis(now.time - file.lastModified()) > i
        }

        /**
         * Is file modified in Seconds
         */
        @JvmStatic
        fun modifiedInSeconds(file: File, i: Int): Boolean {
            val now = Date()
            return TimeUnit.MILLISECONDS.toSeconds(now.time - file.lastModified()) > i
        }

        /**
         * Is file modified in Minutes
         */
        @JvmStatic
        fun modifiedInMinutes(file: File, i: Int): Boolean {
            val now = Date()
            return TimeUnit.MILLISECONDS.toMinutes(now.time - file.lastModified()) > i
        }

        /**
         * Is file modified in Hours
         */
        @JvmStatic
        fun modifiedInHours(file: File, i: Int): Boolean {
            val now = Date()
            return TimeUnit.MILLISECONDS.toHours(now.time - file.lastModified()) > i
        }

        /**
         * Is file modified in Days
         */
        @JvmStatic
        fun modifiedInDays(file: File, i: Int): Boolean {
            val now = Date()
            return TimeUnit.MILLISECONDS.toDays(now.time - file.lastModified()) > i
        }
    }
}