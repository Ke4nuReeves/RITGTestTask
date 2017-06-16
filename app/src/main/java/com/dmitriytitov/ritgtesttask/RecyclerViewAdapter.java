package com.dmitriytitov.ritgtesttask;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dmitriy Titov on 16.06.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<DataListItem> items;

    RecyclerViewAdapter(List<DataListItem> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View itemLayout;

        ViewHolder(final LinearLayout itemLayout) {
            super(itemLayout);
            this.itemLayout = itemLayout;
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView idTextView = (TextView) holder.itemLayout.findViewById(R.id.id_text_view);
        TextView stringDataTextView = (TextView) holder.itemLayout.findViewById(R.id.string_data_text_view);
        idTextView.setText(String.valueOf(items.get(position).getId()));
        stringDataTextView.setText(items.get(position).getStringData());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
