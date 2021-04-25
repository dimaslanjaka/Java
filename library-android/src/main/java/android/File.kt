package android

import android.content.Context
import android.media.MediaScannerConnection
import android.util.Log
import java.io.File

class File {
    companion object {
        fun resolveDir(file: File) {
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e("ResolveDir", "Cannot create folder ${file.absolutePath}")
                }
            }
        }

        fun mediaScan(file: File, context: Context?) {
            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(
                context, arrayOf(file.toString()), null
            ) { path, uri ->
                Log.i("ExternalStorage", "Scanned $path:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }
        }
    }
}
