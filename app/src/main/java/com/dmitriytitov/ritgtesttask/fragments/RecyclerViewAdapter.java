package com.dmitriytitov.ritgtesttask.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmitriytitov.ritgtesttask.Country;
import com.dmitriytitov.ritgtesttask.R;

import java.util.List;

/**
 * Created by Dmitriy Titov on 16.06.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Country> items;

    public RecyclerViewAdapter(List<Country> items) {
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
        TextView countryNameTextView = (TextView) holder.itemLayout.findViewById(R.id.country_name_text_view);
        TextView capitalNameTextView = (TextView) holder.itemLayout.findViewById(R.id.capital_name_text_view);
        idTextView.setText(String.valueOf(items.get(position).getId()));
        countryNameTextView.setText(items.get(position).getName());
        capitalNameTextView.setText(items.get(position).getCapitalName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
