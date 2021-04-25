package com.neptune.domino

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.dimaslanjaka.root.Utils
import com.dimaslanjaka.root.Utils.isPackageInstalled
import com.neptune.domino.XML.validateXml
import java.io.*


class Init {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SdCardPath")
    constructor(context: Context) {
        val pm = context.packageManager
        val isInstalled = isPackageInstalled("com.neptune.domino", pm)
        if (isInstalled) {
            setupPermission()
        }

        val appFiles = File(context.filesDir, "xml")
        val workingDir = File(appFiles, "domino")
        if (!workingDir.exists()) workingDir.mkdirs()
        val originalFile = "/data/data/com.neptune.domino/shared_prefs/Cocos2dxPrefsFile.xml"
        setFilePermission(originalFile, "0777")
        val outputFile = File(workingDir, "Cocos2dxPrefsFile.xml")

        copyFileRoot(originalFile, outputFile.absolutePath)
        setFilePermission(outputFile.absolutePath, "0777")
        Log.i("output", outputFile.toString())
        if (validateXml(outputFile)) {
            val originalpref = readTest(outputFile)
            //originalpref?.let { Log.i("read", it) }
            XML.modifySharedXml(originalpref, outputFile)
            if (validateXml(outputFile)) {
                if (outputFile.exists()) {
                    moveFileRoot(outputFile.absolutePath, originalFile)
                    setFilePermission(originalFile, "0660")
                    Log.i("tweak", "success")
                } else {
                    Log.e("tweak", "failed")
                }
            } else {
                Log.e("validate", "error validating output xml")
            }
        } else {
            Log.e("validate", "error validating input xml")
        }
    }

    fun readTest(file: File): String {
        val text = StringBuilder()
        try {
            val br = BufferedReader(FileReader(file))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                text.append(line)
                text.append('\n')
            }
            br.close()
        } catch (e: IOException) {
            //You'll need to add proper error handling here
        }
        return text.toString()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Utils.isRootGiven()) {
                    val runnable = Runnable {
                        stopApp("com.neptune.domino")
                        Init(context)
                        Thread.sleep(3000)
                        startApp("com.neptune.domino", "com.pokercity.lobby.lobby")
                    }
                    Thread(runnable).start()
                }
            }
        }

        @JvmStatic
        fun stopApp(packageName: String) {
            try {
                val process = Runtime.getRuntime().exec("su")
                //val `in`: InputStream = process.inputStream
                val out: OutputStream = process.outputStream
                val cmd = "am kill $packageName"
                out.write(cmd.toByteArray())
                out.flush()
                out.close()
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun startApp(packageName: String, activity: String) {
            try {
                val process = Runtime.getRuntime().exec("su")
                //val `in`: InputStream = process.inputStream
                val out: OutputStream = process.outputStream
                val cmd = "am start -n $packageName/$activity"
                out.write(cmd.toByteArray())
                out.flush()
                out.close()
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun setFilePermission(filePath: String, permCode: String) {
            try {
                val process = Runtime.getRuntime().exec("su")
                //val `in`: InputStream = process.inputStream
                val out: OutputStream = process.outputStream
                val cmd = "chmod $permCode $filePath"
                out.write(cmd.toByteArray())
                out.flush()
                out.close()
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun moveFileRoot(from: String, to: String) {
            try {
                val process = Runtime.getRuntime().exec("su")
                val `in`: InputStream = process.inputStream
                val out: OutputStream = process.outputStream
                val cmd = "mv $from $to"
                out.write(cmd.toByteArray())
                out.flush()
                out.close()
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun copyFileRoot(from: String, to: String) {
            try {
                val process = Runtime.getRuntime().exec("su")
                val `in`: InputStream = process.inputStream
                val out: OutputStream = process.outputStream
                val cmd = "cp -r $from $to"
                out.write(cmd.toByteArray())
                out.flush()
                out.close()
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun readFileRoot(filepath: String): String {
            try {
                val process = Runtime.getRuntime().exec("su")
                val `in`: InputStream = process.inputStream
                val out: OutputStream = process.outputStream
                val cmd = "cat $filepath"
                out.write(cmd.toByteArray())
                out.flush()
                out.close()
                val buffer = ByteArray(1024 * 12) //Able to read up to 12 KB (12288 bytes)
                val length: Int = `in`.read(buffer)
                val content = String(buffer, 0, length)
                //Wait until reading finishes
                process.waitFor()
                //Do your stuff here with "content" string
                //The "content" String has the content of /data/someFile
                return content
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return ""
        }

        @JvmStatic
        fun setupPermission() {
            try {
                val p = Runtime.getRuntime().exec("su")
                val outputStream = DataOutputStream(p.outputStream)
                outputStream.writeBytes("chmod 777 /data /data/data /data/data/com.neptune.domino\n")
                outputStream.flush()
                outputStream.writeBytes("exit\n")
                outputStream.flush()
                p.waitFor()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}