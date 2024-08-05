package com.example.gallerymanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    ArrayList<Image> adapter_imageArrayList;

    public Adapter(Context context, ArrayList<Image> adapter_imageArrayList) {
        this.context = context;
        this.adapter_imageArrayList = adapter_imageArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_display_adapter, parent, false);
        return new ViewHolder(view);
    }


    /*** this func setting the date from the class into the layout-View items ***/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // set the data in items
        holder.text.setText(adapter_imageArrayList.get(position).imageUri.toString());

        Glide.with(context).load(adapter_imageArrayList.get(position).imageUri.toString()).into(holder.image);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DataCenter.imageArrayList.add(DataCenter.currentPhotoIndex , DataCenter.deleted_imageArrayList.get((position)));
                    DataCenter.deleted_imageArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, DataCenter.deleted_imageArrayList.size());
                    TrashBinFragment.ifDeletedListIsEmpty();
                } catch (Exception e) {
                    System.out.println("ctach!!!!!!!");
                }
            }
        });


        /*** implement setOnClickListener event on item view. ***/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
              //  Toast.makeText(context, position + " " + DataCenter.deleted_imageArrayList.indexOf(DataCenter.deleted_imageArrayList.get(position)), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return adapter_imageArrayList.size();
    }

    /*** this class is holding the data in the layout to help to denfine them ***/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView image;
        TextView text;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            image = itemView.findViewById(R.id.ImageView_IDA);
            text = itemView.findViewById(R.id.textView_IDA);
            button = itemView.findViewById(R.id.btn_restore_img_IDA);
        }
    }

}
