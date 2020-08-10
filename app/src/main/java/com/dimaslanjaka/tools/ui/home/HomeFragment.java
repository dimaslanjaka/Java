package com.dimaslanjaka.tools.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dimaslanjaka.tools.BuildConfig;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.ytdE.YouTubeUriExtractor;
import com.dimaslanjaka.tools.ytdE.YtFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {
    final String search = "http://ytdl-l3n4r0x.herokuapp.com/search?q=";
    ImageButton btnHit;
    TextView txtJson;
    ProgressDialog pd;
    Context HomeActivity;
    LinearLayout linearWrapper;
    View copyLayout;
    private String youTubeURL;
    private String WritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private String ReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        if (BuildConfig.DEBUG) {
            final TextView textView = root.findViewById(R.id.text_home);
            homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });
        }
        HomeActivity = getActivity().getApplicationContext();
        btnHit = (ImageButton) root.findViewById(R.id.searchbtn);
        txtJson = (TextView) root.findViewById(R.id.ErrorSearch);
        final EditText mEdit = (EditText) root.findViewById(R.id.searchbox);
        btnHit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new JsonTask()
                        .execute(search + mEdit.getText().toString().trim());
            }
        });
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new JsonTask()
                        .execute(search + mEdit.getText().toString().trim());
            }
        });

        linearWrapper = (LinearLayout) root.findViewById(R.id.LineWrap);

        return root;
    }

    private void YouTubeVideoDownloadF(int iTag) {
        if (permStorage()) {
            YTDownload(iTag);
        }
    }

    private boolean permStorage() {
        boolean allowed = ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), WritePermission) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), ReadPermission) != PackageManager.PERMISSION_GRANTED;
        if (allowed) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{WritePermission, ReadPermission}, 1);
        }
        return allowed;
    }

    private void YTDownload(final int itag) {
        String VideoURLDownload = youTubeURL;
        Log.d("YTDownload", VideoURLDownload);
        @SuppressLint("StaticFieldLeak")
        YouTubeUriExtractor youTubeUriExtractor = new YouTubeUriExtractor(getActivity().getApplicationContext()) {
            @Override
            public void onUrisAvailable(String videoId, final String videoTitle, SparseArray<YtFile> ytFiles) {
                if ((ytFiles != null)) {
                    String downloadURL = ytFiles.get(itag).getUrl();
                    Log.e("Download URL: ", downloadURL);
                    //final String extension = ".mp3";
                    final String dURL = downloadURL;
                    final String vTitle = videoTitle;
                    /*if (itag == 18 || itag == 22) {
                        final String extension = ".mp4";
                        //DownloadManagingF(downloadURL, videoTitle, mp4);
                    } else if (itag == 251) {
                        final String extension = ".mp3";
                    }*/
                    String extension = null;
                    switch (itag) {
                        case 18:
                            extension = ".mp4";
                            break;
                        case 22:
                            extension = ".mp4";
                            break;
                        case 251:
                            extension = ".mp3";
                            break;
                    }
                    Log.d("itag", String.valueOf(itag));
                    Log.d("extension", String.valueOf(extension));
                    if (extension == null) {
                        Log.d("extension", "NULL");
                        return;
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Downloading " + vTitle + extension.toString(), Toast.LENGTH_LONG).show();
                    DownloadManagingF(dURL, vTitle, extension);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error With URL", Toast.LENGTH_LONG).show();
                }
            }
        };
        youTubeUriExtractor.execute(VideoURLDownload);
    }

    private void DownloadManagingF(@NonNull String downloadURL, @NonNull String videoTitle, @NonNull String extentiondwn) {
        String downloadFolder = getString(R.string.app_name);
        Log.d("ext", extentiondwn);
        switch (extentiondwn) {
            case ".mp3":
                downloadFolder = "mp3";
                break;
            case ".mp4":
                downloadFolder = "mp4";
                break;
        }
        if (downloadURL != null) {
            final String Folder = "/Download/" + downloadFolder + "/";
            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadURL));
            request.setTitle(videoTitle);
            request.setDestinationInExternalPublicDir(Folder, videoTitle + extentiondwn);
            if (downloadManager != null) {
                Toast.makeText(getActivity().getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                downloadManager.enqueue(request);
            }
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    Toast.makeText(getActivity().getApplicationContext(), "Download Completed", Toast.LENGTH_SHORT).show();

                    Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + Folder);
                    Intent intentop = new Intent(Intent.ACTION_VIEW);
                    intentop.setDataAndType(selectedUri, "resource/folder");

                    if (intentop.resolveActivityInfo(getActivity().getPackageManager(), 0) != null) {
                        startActivity(intentop);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Saved on: " + Folder, Toast.LENGTH_LONG).show();
                        restartApp();
                    }
                    getActivity().unregisterReceiver(this);
                    getActivity().finish();
                }
            };
            getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private void restartApp() {

    }

    private class JsonTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            /*pd = new ProgressDialog(HomeActivity);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();*/
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*if (pd.isShowing()) {
                pd.dismiss();
            }*/
            if (result == null) {
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                JSONArray items = json.getJSONArray("items");
                boolean error = json.getBoolean("error");
                if (error == true) {
                    txtJson.setText("error backend");
                } else {
                    String r_result = "";
                    linearWrapper.removeAllViewsInLayout();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject snippet = item.getJSONObject("snippet");
                        String published = snippet.getString("publishedAt");
                        String channel = snippet.getString("channelId");
                        String channel_title = snippet.getString("channelTitle");
                        String title = snippet.getString("title");
                        String desc = snippet.getString("description");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails").getJSONObject("default");
                        String thumb_url = "http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png";
                        if (thumbnails.has("url")) {
                            thumb_url = thumbnails.getString("url");
                        }
                        if (thumbnails.has("width")) {
                            int thumb_width = thumbnails.getInt("width");
                        }
                        if (thumbnails.has("height")) {
                            int thumb_height = thumbnails.getInt("height");
                        }
                        JSONObject vids = item.getJSONObject("id");
                        String vid = null;
                        if (vids.has("videoId")) {
                            vid = vids.getString("videoId").toString();
                            r_result += "https://youtu.be/" + vid + "\n";
                        }

                        copyLayout = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.search_card, null);
                        //copyLayout.setId(View.generateViewId());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            copyLayout.setId(View.generateViewId());
                        }
                        copyLayout.setMinimumHeight(70);
                        if (copyLayout.getParent() != null) {
                            ((ViewGroup) copyLayout.getParent()).removeView(copyLayout);
                            Log.d("parent", "removed");
                        }
                        ImageView thumbw = copyLayout.findViewById(R.id.thumb);
                        new DownloadImageTask(thumbw).execute(thumb_url);
                        TextView textw = copyLayout.findViewById(R.id.title);
                        textw.setText(title);

                        final String urlyt = "https://youtu.be/" + vid;
                        View.OnClickListener buttonClickListener = new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                youTubeURL = urlyt;
                                if (youTubeURL.contains("http")) {
                                    try {
                                        YouTubeVideoDownloadF(251);
                                        Toast.makeText(getActivity().getApplicationContext(), "Fetching " + youTubeURL, Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Enter URL First", Toast.LENGTH_LONG).show();
                                }
                            }
                        };

                        ImageButton btnw = copyLayout.findViewById(R.id.btn);
                        btnw.setOnClickListener(buttonClickListener);
                        /*CardView cardw = copyLayout.findViewById(R.id.CopyRes);
                        cardw.setOnClickListener(buttonClickListener);*/
                        linearWrapper.addView(copyLayout);

                        if (i == items.length() - 1) {
                            //txtJson.setText(r_result);
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO download image as source
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}

