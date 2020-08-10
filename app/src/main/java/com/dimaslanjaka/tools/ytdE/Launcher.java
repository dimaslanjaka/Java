package com.dimaslanjaka.tools.ytdE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Launcher {
    public Context c = null;
    public Intent i = null;
    public Activity a = null;
    public String youTubeURL;

    public Launcher(Context context, Intent intent) {
        c = context;
        i = intent;
    }

    public Launcher(Context context) {
        c = context;
    }

    public Launcher(Context context, int itag, String yturl) {
        c = context;
        StartDownload(itag, yturl);
    }

    private com.dimaslanjaka.tools.helper.Permission perm;

    public Launcher(AppCompatActivity act, int itag, @NonNull String yturl) {
        a = act;
        c = act;
        perm = new com.dimaslanjaka.tools.helper.Permission(act);
        if (perm.checkPermission()) {
            StartDownload(itag, yturl);
        }
    }

    public void StartDownload(int iTag, @NonNull String yturl) { //iTag: 251 for mp3
        String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (perm != null && perm.checkPermission()) {
            YTDownload(iTag, yturl);
        } else if (a != null) {
            perm = new com.dimaslanjaka.tools.helper.Permission(a);
            if (perm.checkPermission()) {
                YTDownload(iTag, yturl);
            } else {
                Log.e("Error", "Cant download");
            }
        } else if (c != null && c.checkCallingOrSelfPermission(writePermission) == PackageManager.PERMISSION_GRANTED) {
            YTDownload(iTag, yturl);
        } else {
            Log.e("Error", "Cant download (2)");
        }
    }

    private void YTDownload(final int itag, String video_url) {
        if (video_url.length() > 0) {
            youTubeURL = video_url;
        }

        String VideoURLDownload = youTubeURL;
        //Log.d("YTDownload", VideoURLDownload);

        @SuppressLint("StaticFieldLeak") YouTubeUriExtractor youTubeUriExtractor = new YouTubeUriExtractor(c.getApplicationContext()) {
            @Override
            public void onUrisAvailable(String videoId, final String videoTitle, SparseArray<YtFile> ytFiles) {
                if ((ytFiles != null)) {
                    String downloadURL = ytFiles.get(itag).getUrl();
                    //Log.e("Download URL: ", downloadURL);

                    String extension = null;
                    if (itag == 18 || itag == 22) {
                        extension = ".mp4";
                        //DownloadManagingF(downloadURL, videoTitle, mp4);
                    } else if (itag == 251) {
                        extension = ".mp3";
                    }
                    Log.d("itag", String.valueOf(itag));
                    Log.d("extension", String.valueOf(extension));
                    if (extension == null) {
                        Log.d("extension", "NULL");
                        return;
                    }
                    Toast.makeText(c.getApplicationContext(), "Downloading " + videoTitle + extension.toString(), Toast.LENGTH_LONG).show();
                    DownloadManagingF(downloadURL, videoTitle, extension);
                } else {
                    Toast.makeText(c.getApplicationContext(), "(Launcher) Error With URL",
                            Toast.LENGTH_LONG).show();
                    Log.e("error", "YtFile is null");
                }
            }
        };
        youTubeUriExtractor.execute(VideoURLDownload);
    }

    private void DownloadManagingF(@NonNull String downloadURL, @NonNull String videoTitle, @NonNull String extentiondwn) {
        String downloadFolder = null;//c.getString(R.string.app_name);
        //Log.i("downloadFolder", downloadFolder);
        //Log.d("ext", extentiondwn);
        switch (extentiondwn) {
            case ".mp3":
                downloadFolder = Environment.DIRECTORY_MUSIC;
                break;
            case ".mp4":
                downloadFolder = Environment.DIRECTORY_MOVIES;
                break;
        }

        com.dimaslanjaka.tools.helper.storage.folder(downloadFolder);

        DownloadManager downloadManager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadURL));
        request.setTitle(videoTitle);
        //request.setDestinationInExternalPublicDir(Folder, videoTitle + extentiondwn);
        //Environment.DIRECTORY_PICTURES
        request.setDestinationInExternalPublicDir(
                downloadFolder, videoTitle + extentiondwn
        );
        if (downloadManager != null) {
            Toast.makeText(c.getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
            downloadManager.enqueue(request);
        }
        final String finalDownloadFolder = downloadFolder;
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Toast.makeText(c.getApplicationContext(), "Download Completed",
                        Toast.LENGTH_SHORT).show();

                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + finalDownloadFolder);
                Intent intentop = new Intent(Intent.ACTION_VIEW);
                intentop.setDataAndType(selectedUri, "resource/folder");

                if (intentop.resolveActivityInfo(c.getPackageManager(), 0) != null) {
                    //startActivity(intentop);
                    if (c != null) {
                        c.startActivity(intentop);
                    }
                } else {
                    Toast.makeText(c.getApplicationContext(), "Saved on: " + finalDownloadFolder, Toast.LENGTH_LONG).show();
                    //restartApp();
                }
                assert c != null;
                c.unregisterReceiver(this);
                //c.finish();
            }
        };
        c.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
