package com.dimaslanjaka.tools.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.dimaslanjaka.tools.Helpers.core.Service.Activity;
import com.dimaslanjaka.tools.Libs.Graphics.Images.Picasso;
import com.dimaslanjaka.tools.Libs.Graphics.Images.SVG;
import com.dimaslanjaka.tools.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
	private ArrayList<DataBinding> mDataset;

	// Provide a suitable constructor (depends on the kind of dataset)
	public HomeAdapter(ArrayList<DataBinding> myDataset) {
		mDataset = myDataset;
	}

	// Create new views (invoked by the layout manager)
	@NotNull
	@Override
	public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
	                                                   int viewType) {
		// create a new view
		CardView v = (CardView) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.fragment_home_list, parent, false);

		return new MyViewHolder(v, parent.getContext());
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		holder.home_title.setText(mDataset.get(position).title);
		if (mDataset.get(position).thumbnail.endsWith(".svg")) {
     /*
      GlideToVectorYou
              .init()
              .with(holder.context)
              //.setPlaceHolder(R.drawable.loading, R.drawable.ic_facebook_holo_dark)
              .load(Uri.parse(mDataset.get(position).thumbnail), holder.thumbnail);
      */
			SVG.fetch(holder.thumbnail.getContext(), mDataset.get(position).thumbnail, holder.thumbnail);
		} else {
			Picasso.fromURL(holder.thumbnail.getContext(), mDataset.get(position).thumbnail,
							holder.thumbnail);
		}

		holder.home_description.setText(mDataset.get(position).description);
		if (mDataset.get(position).classname != null) {
			if (!holder.root.hasOnClickListeners()) {
				holder.root.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Activity.start(holder.context, mDataset.get(position).classname);
					}
				});
			}
		}
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case

		public ImageView thumbnail;
		public TextView home_title;
		public TextView home_description;
		public Context context;
		public CardView root;

		public MyViewHolder(CardView view, Context c) {
			super(view);
			root = view;
			thumbnail = view.findViewById(R.id.home_thumbnail);
			home_title = view.findViewById(R.id.home_title);
			home_description = view.findViewById(R.id.home_description);
			context = c;
		}
	}
}