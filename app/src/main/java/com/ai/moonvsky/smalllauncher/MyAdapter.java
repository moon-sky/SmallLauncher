package com.ai.moonvsky.smalllauncher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.Collator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ai.moonvsky.smalllauncher.AppUtils.getUninstallAppIntent;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public static final String TAG = "Test";
    private List<App> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        ImageView imageView;
        LinearLayout mRootView;
        boolean isLongClick = false;

        MyViewHolder(LinearLayout v) {
            super(v);
            mTextView = v.findViewById(R.id.tv_name);
            imageView = v.findViewById(R.id.img_app);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MyAdapter() {
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).appName);
        holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyApplication.getContext().startActivity(getUninstallAppIntent(mDataset.get(position).packageName));
                return false;
            }
        });
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.getContext().startActivity(mDataset.get(position).launchIntent);
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyApplication.getContext().startActivity(getUninstallAppIntent(mDataset.get(position).packageName));
                return false;
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.getContext().startActivity(mDataset.get(position).launchIntent);
            }
        });
        holder.imageView.setImageDrawable(mDataset.get(position).icon);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }

    void setmDataset(List<App> appList) {
        mDataset = appList;
        Log.d(TAG, "setmDataset:SIZE: " + appList.size());
        Collections.sort(mDataset, new Comparator<App>() {
            @Override
            public int compare(App o1, App o2) {

                return java.text.Collator.getInstance().compare(o2.getAppName(),o1.getAppName());
            }
        });
        notifyDataSetChanged();
    }


}