package lib

import com.dimaslanjaka.gradle.plugin.date.SimpleDateFormat
import com.dimaslanjaka.kotlin.ConsoleColors.Companion.println
import com.dimaslanjaka.kotlin.Date.isLessThanHourAgo
import com.dimaslanjaka.kotlin.Date.isLessThanMinuteAgo
import com.dimaslanjaka.kotlin.Date.isMoreThanHourAgo
import com.dimaslanjaka.kotlin.Date.isMoreThanMinuteAgo
import java.io.*
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributeView
import java.nio.file.attribute.FileTime
import java.text.DateFormat
import java.util.*


class File : java.io.File, Serializable {
    val serialVersionUID = 1 // Noncompliant; not static & int rather than long
    private var file: java.io.File? = null

    /**
     * Is file first creation
     *
     * @return true for firstly created
     */
    var isFirst = false
        private set

    constructor(pathname: String) : super(pathname) {
        resolve(pathname)
        file = java.io.File(pathname)
    }

    constructor(parent: String?, child: String) : super(parent, child) {
        file = java.io.File(parent, child)
    }

    constructor(parent: java.io.File?, child: String) : super(parent, child) {
        resolveDir(parent!!.absolutePath)
        resolve(java.io.File(parent, child).absolutePath)
        file = java.io.File(parent, child)
    }

    constructor(uri: URI) : super(uri) {}
    constructor(file: java.io.File?) : super(file.toString()) {
        this.file = file
    }

    constructor(temp: String, value: File) : super(temp, value.toString()) {
        file = java.io.File(temp, value.toString())
        resolveDir(file!!.parent)
    }

    @get:Throws(IOException::class)
    val attributes: Map<String, Date>
        get() {
            val p = Paths.get(file!!.absolutePath)
            val view = Files
                .getFileAttributeView(p, BasicFileAttributeView::class.java)
                .readAttributes()
            val creationTime = view.creationTime().toMillis()
            val modificationTime = view.lastModifiedTime().toMillis()
            val map: MutableMap<String, Date> = HashMap()
            map["create"] = Date(creationTime)
            map["modification"] = Date(modificationTime)
            return map
        }
    val creationTime: Date?
        get() = try {
            attributes["create"]
        } catch (e: IOException) {
            null
        }

    fun removeLineFromFile(lineToRemove: String): Boolean {
        if (file != null) {
            val tempFile = File("${getTempDir()}/java/build/myTempFile.txt")
            if (!tempFile.parentFile.exists()) {
                tempFile.parentFile.mkdirs()
            }
            val reader = BufferedReader(FileReader(this.file!!))
            val writer = BufferedWriter(FileWriter(tempFile))

            var currentLine: String

            while (reader.readLine().also { currentLine = it } != null) {
                // trim newline when comparing with lineToRemove
                val trimmedLine = currentLine.trim { it <= ' ' }
                if (trimmedLine == lineToRemove) continue
                writer.write(currentLine + System.getProperty("line.separator"))
            }
            writer.close()
            reader.close()
            return tempFile.renameTo(file)
        }
        return false
    }

    fun resolve(name: String) {
        val file = java.io.File(name)
        if (!file.exists()) {
            isFirst = true
            try {
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun read(): String {
        val data = StringBuilder()
        try {
            val reader = Scanner(file)
            while (reader.hasNextLine()) {
                //String data = myReader.nextLine();
                //System.out.println(data);
                data.append(reader.nextLine())
            }
            reader.close()
        } catch (e: FileNotFoundException) {
            //System.out.println("An error occurred.");
            e.printStackTrace()
        }
        return data.toString()
    }

    fun readAsList(): List<String> {
        val stringList: MutableList<String> = ArrayList()
        try {
            val reader = Scanner(file)
            while (reader.hasNextLine()) {
                //String data = myReader.nextLine();
                //System.out.println(data);
                stringList.add(reader.nextLine())
            }
            reader.close()
        } catch (e: FileNotFoundException) {
            //System.out.println("An error occurred.");
            e.printStackTrace()
        }
        return stringList
    }

    @JvmOverloads
    fun write(s: String?, append: Boolean = false) {
        try {
            val writer = FileWriter(file, append)
            writer.write(s)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun resolveDir(name: String?) {
        if (!java.io.File(name).exists()) {
            if (!java.io.File(name).mkdirs()) {
                println("cannot create parent folder")
            }
            isFirst = true
        }
    }

    @JvmOverloads
    fun file_in_dir(filename: String, autocreate: Boolean = false): File? {
        val parent = file
        file = File(file, filename)
        if (parent!!.isDirectory) {
            if (autocreate) {
                if (!parent.exists()) {
                    if (!parent.mkdirs()) {
                        println("Cannot create parent dir $parent")
                    }
                }
                try {
                    if (file?.exists() == true) {
                        if (file?.createNewFile()!!) {
                            println(
                                "cannot create file $filename"
                            )
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return File(file)
        }
        return null
    }

    //return dateFormat.format(fileTime.toMillis());
    val lastModified: Date?
        get() {
            val fileTime: FileTime
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            return try {
                fileTime = Files.getLastModifiedTime(file!!.toPath())
                //return dateFormat.format(fileTime.toMillis());
                Date(fileTime.toMillis())
            } catch (e: IOException) {
                System.err.println("Cannot get the last modified time - $e")
                null
            }
        }

    fun isModifiedMoreThanMinute(i: Int): Boolean {
        val lastModified = lastModified
        return isMoreThanMinuteAgo(lastModified!!, i)
    }

    fun isModifiedLessThanMinute(i: Int): Boolean {
        val lastModified = lastModified
        return isLessThanMinuteAgo(lastModified!!, i)
    }

    fun isModifiedMoreThanHour(i: Int): Boolean {
        val lastModified = lastModified
        return isMoreThanHourAgo(lastModified!!, i)
    }

    fun isModifiedLessThanHour(i: Int): Boolean {
        val lastModified = lastModified
        return isLessThanHourAgo(lastModified!!, i)
    }

    fun write(any: Any) {
        file?.writeText(any.toString())
    }

    companion object {
        @JvmStatic
        fun getTempDir(): String {
            val property = "java.io.tmpdir"
            val tempDir = System.getProperty(property)
            if (tempDir != null) return tempDir
            return "build"
        }
    }
}