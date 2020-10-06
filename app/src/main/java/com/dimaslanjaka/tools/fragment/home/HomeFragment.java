package com.dimaslanjaka.tools.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimaslanjaka.tools.AdmobStart;
import com.dimaslanjaka.tools.BuildConfig;
import com.dimaslanjaka.tools.Facebook.MainActivity;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.Service.Netspeed.NetspeedActivity;
import com.dimaslanjaka.tools.Sniper.Homepage;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
  private static final boolean DEBUG = false;
  Context HomeActivity;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    HomeActivity = getContext();

    RecyclerView recyclerView = root.findViewById(R.id.my_recycler_view);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(true);

    // use a linear layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(layoutManager);

    // specify an adapter (see also next example)
    ArrayList<DataBinding> dataList = new ArrayList<>();
    dataList.add(new DataBinding(
            "https://res.cloudinary.com/dimaslanjaka/image/fetch/https://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Facebook_logo_36x36.svg/1024px-Facebook_logo_36x36.svg.png",
            "Facebook Reaction Bot",
            "Auto like your friends status automatically",
            MainActivity.class
    ));
    dataList.add(new DataBinding(
            "https://res.cloudinary.com/dimaslanjaka/image/fetch/https://static.thenounproject.com/png/3330103-200.png",
            "Data Usage",
            "Android Internet Logger. Records all traffic and save usage data for unlimited date",
            NetspeedActivity.class
    ));
    if (BuildConfig.DEBUG) {
      dataList.add(new DataBinding(
              "https://res.cloudinary.com/dimaslanjaka/image/fetch/https://1.bp.blogspot.com/-8B3xy5c2gIg/VOxhulCuEKI/AAAAAAAAAEA/nyPwPnKg-Rk/s1600/okeschool-d7e25d163b124814.png",
              "ISP Indonesia Panel",
              "ISP Indonesia Unofficial Panel Dashboard",
              Homepage.class
      ));
      dataList.add(new DataBinding(
              "https://cdn.worldvectorlogo.com/logos/google-admob.svg",
              "Admob Runner",
              "Run your admob ads for this app",
              AdmobStart.class
      ));
      dataList.add(new DataBinding(
              "https://res.cloudinary.com/dimaslanjaka/image/fetch/https://4.bp.blogspot.com/-rtNRVM3aIvI/XJX_U07Z-II/AAAAAAAAJXY/YpdOo490FTgdKOxM4qDG-2-EzcNFAWkKACK4BGAYYCw/s1600/logo%2Bfirebase%2Bicon.png",
              "Firebase Settings",
              "Sync your setting preferences around multiple devices",
              com.dimaslanjaka.tools.Firebase.MainActivity.class
      ));
    }

    //RecyclerView.Adapter mAdapter = new HomeAdapter(dataList);
    recyclerView.setAdapter(new HomeAdapter(dataList));

    return root;
  }
}

