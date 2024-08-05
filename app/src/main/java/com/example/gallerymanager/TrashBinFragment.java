package com.example.gallerymanager;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class TrashBinFragment extends Fragment implements View.OnClickListener {
     View view;
    static TextView emptyList_txt;
    static Button btn_delete_all;
    static RecyclerView recyclerView;
    Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trash_bin, container, false);
        initUI();
        return view;
    }

    public void initUI() {
        recyclerView = view.findViewById(R.id.recyclerView);
        btn_delete_all = view.findViewById(R.id.btn_delete_all);
        emptyList_txt = view.findViewById(R.id.emptyList_txt);

        btn_delete_all.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), DataCenter.deleted_imageArrayList);
        recyclerView.setAdapter(adapter); // set the Adapter to RecyclerView
        recyclerView.setHasFixedSize(true);
        ifDeletedListIsEmpty();
        setSizeofrecyclerViewToFitTheScreen();
    }


    public static void ifDeletedListIsEmpty() {
        if (DataCenter.deleted_imageArrayList.size() == 0) {
            emptyList_txt.setVisibility(View.VISIBLE);
            btn_delete_all.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyList_txt.setVisibility(View.GONE);
            btn_delete_all.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            btn_delete_all.setText("Delete all (" + DataCenter.deleted_imageArrayList.size() + ")");
        }

    }

    public void setSizeofrecyclerViewToFitTheScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = (int) (height * 0.85);

        recyclerView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_delete_all && DataCenter.deleted_imageArrayList.size() != 0) {
            //deleteAllPhotos();
        }
    }

    public void deleteAllPhotos() {
        int len = DataCenter.deleted_imageArrayList.size();
        for (int i = 0; i < len; i++) {
            try {
                new File(DataCenter.deleted_imageArrayList.get(0).imageUri.toString()).delete();
                DataCenter.deleted_imageArrayList.remove(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ifDeletedListIsEmpty();
    }
}