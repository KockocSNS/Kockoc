package com.kocapplication.pixeleye.kockocapp.detail.share;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kocapplication.pixeleye.kockocapp.R;

import java.util.List;

/**
 * Created by Han on 2016-07-08.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareViewHolder> {
    private Context context;
    private List<ResolveInfo> items;
    private View.OnClickListener listener;

    public ShareAdapter(Context context, List<ResolveInfo> data, View.OnClickListener listener) {
        super();
        this.context = context;
        this.listener = listener;

        if (data == null) throw new IllegalArgumentException("DATA MUST NOT BE NULL");
        this.items = data;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_layout, parent, false);
        itemView.setOnClickListener(listener);
        return new ShareViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {
        ResolveInfo info = items.get(position);
        holder.getTitle().setText(info.activityInfo.applicationInfo.loadLabel(context.getPackageManager()));
        holder.getIcon().setImageDrawable(info.activityInfo.applicationInfo.loadIcon(context.getPackageManager()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<ResolveInfo> getItems() {
        return items;
    }
}
