package com.dimaslanjaka.tools.ui.ytmp3;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dimaslanjaka.tools.BuildConfig;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.helper.SharedPref;
import com.dimaslanjaka.tools.ytdE.Launcher;

public class Ytmp3Fragment extends Fragment {

    EditText editText;
    ProgressBar progressBar;
    private String youTubeURL;
    private final String WritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String ReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private Context contextFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPref sharedpref;

    //@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Ytmp3ViewModel ytmp3ViewModel = ViewModelProviders.of(this).get(Ytmp3ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ytmp3, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        if (BuildConfig.DEBUG) {
            ytmp3ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });
        }
        ImageButton btn = (ImageButton) root.findViewById(R.id.mp3d);
        final EditText editText = root.findViewById(R.id.yturl);
        new com.dimaslanjaka.tools.helper.Formsaver(getContext(), editText);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                youTubeURL = editText.getText().toString().trim();
                if (youTubeURL.contains("youtu")) {
                    try {
                        new Launcher(getActivity(), 251, youTubeURL);
                        Toast.makeText(getContext(), "Fetching " + youTubeURL, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Enter URL First", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    private void restartApp() {

    }
}
