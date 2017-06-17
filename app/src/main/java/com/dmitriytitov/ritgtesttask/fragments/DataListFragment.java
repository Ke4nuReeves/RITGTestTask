package com.dmitriytitov.ritgtesttask.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmitriytitov.ritgtesttask.data.DataLoader;
import com.dmitriytitov.ritgtesttask.R;

public class DataListFragment extends Fragment {

    public DataListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_list, container, false);
        DataLoader.RequestType requestType = (DataLoader.RequestType)getArguments().getSerializable("requestType");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new DataLoader(getContext(), recyclerView, requestType).requestData();

        return rootView;
    }
}
