package com.dimaslanjaka.tools.Service.Netspeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dimaslanjaka.tools.Libs.Log;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.Service.Netspeed.v2.NetspeedDataBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NetspeedAdapter extends RecyclerView.Adapter<NetspeedAdapter.MyViewHolder> {
  public static ArrayList<NetspeedDataBinding> mDataset;

  public NetspeedAdapter(ArrayList<NetspeedDataBinding> datas) {
    mDataset = datas;
  }

  // Create new views (invoked by the layout manager)
  @NotNull
  @Override
  public NetspeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
    // create a new view
    LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.netspeed_child, parent, false);

    return new NetspeedAdapter.MyViewHolder(v, parent.getContext());
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(@NotNull final NetspeedAdapter.MyViewHolder holder, final int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    NetspeedDataBinding data = mDataset.get(position);
    Log.out(data, position, mDataset.size());
    int wifi = Integer.parseInt(data.wifi);
    if (wifi == 0) {
      holder.wifi.setText("0 B");
    } else {
      holder.wifi.setText(Converter.parseSize(wifi));
    }

    int mobile = Integer.parseInt(data.mobile);
    if (mobile == 0) {
      holder.mobile.setText("0 B");
    } else {
      holder.mobile.setText(Converter.parseSize(mobile));
    }

    holder.date.setText(data.date);
    int total = wifi;
    total += mobile;
    if (total == 0) {
      holder.total.setText("0 B");
    } else {
      holder.total.setText(Converter.parseSize(total));
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    Log.out("Size items is " + mDataset.size());
    return mDataset.size();
  }

  // Provide a reference to the views for each data item
  // Complex data items may need more than one view per item, and
  // you provide access to all the views for a data item in a view holder
  public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView date, mobile, wifi, total;
    public Context context;
    public LinearLayout root;

    public MyViewHolder(LinearLayout view, Context c) {
      super(view);
      root = view;
      mobile = view.findViewById(R.id.todayMobile);
      wifi = view.findViewById(R.id.todayWifi);
      date = view.findViewById(R.id.todayDate);
      total = view.findViewById(R.id.todayTotal);
      context = c;
    }
  }
}
