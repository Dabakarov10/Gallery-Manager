package com.example.gallerymanager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ApplicationExitInfo;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.app.Dialog;
import android.graphics.Path;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentResolver;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.ContentResolver;

public class PhotosFragment extends Fragment implements View.OnClickListener {
    TextView txt_title;

    ImageView imageView;
    View view;
    Button btn_StoragePermission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photos, container, false);

        initUI();

        return view;
    }

    private void initUI() {
        imageView = view.findViewById(R.id.emptyView);
        txt_title = view.findViewById(R.id.PhotosFragment_txt);
        btn_StoragePermission = view.findViewById(R.id.btn_StoragePermission);

        imageView.setOnClickListener(this);
        btn_StoragePermission.setOnClickListener(this);

        imageView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                DataCenter.deleted_imageArrayList.add(DataCenter.imageArrayList.get(DataCenter.currentPhotoIndex));
                DataCenter.imageArrayList.remove(DataCenter.currentPhotoIndex);

                //new File(DataCenter.currentPhotoPath).delete();
                // מוחק את הקובץ אבל צריך אבל צריך להעביר אותו לדף של פח האשפה ולמחוק משם סופית

             //   Toast.makeText(getContext(), "נמחק!!!", Toast.LENGTH_SHORT).show();
                DataCenter.currentPhotoIndex--;
                DataCenter.currentPhotoPath = DataCenter.imageArrayList.get(DataCenter.currentPhotoIndex).getImageUri().toString();
                imageView.setImageURI(Uri.fromFile(new File(DataCenter.currentPhotoPath)));


                // Toast.makeText(getContext(), "top למעלה", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                DataCenter.currentPhotoIndex--;
                DataCenter.currentPhotoPath = DataCenter.imageArrayList.get(DataCenter.currentPhotoIndex).getImageUri().toString();
                imageView.setImageURI(Uri.fromFile(new File(DataCenter.currentPhotoPath)));
                txt_title.setText(DataCenter.currentPhotoPath + "\n" + DataCenter.imageArrayList.size() + "/"
                        + DataCenter.currentPhotoIndex);
                //Toast.makeText(getContext(), "right ימין", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                DataCenter.currentPhotoIndex++;
                DataCenter.currentPhotoPath = DataCenter.imageArrayList.get(DataCenter.currentPhotoIndex).getImageUri().toString();
                imageView.setImageURI(Uri.fromFile(new File(DataCenter.currentPhotoPath)));
                txt_title.setText(DataCenter.currentPhotoPath + "\n" + DataCenter.imageArrayList.size() + "/"
                        + DataCenter.currentPhotoIndex);
                //Toast.makeText(getContext(), "left שמאל", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                //Toast.makeText(getContext(), "bottom למטה", Toast.LENGTH_SHORT).show();
            }
        });

        if (Environment.isExternalStorageManager()) {
            btn_StoragePermission.setVisibility(View.GONE);
            showExternalStorage();
        }
    }

    private void askStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getContext().getPackageName())));
                    startActivity(intent);
                } catch (Exception exception) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            }
        }
    }

    private void showExternalStorage() {
        if (DataCenter.imageArrayList.size() == 0)
            DataCenter.imageArrayList = getImage();

        DataCenter.currentPhotoIndex = DataCenter.currentPhotoIndex == 0 ?
                DataCenter.imageArrayList.size() - 1 : DataCenter.currentPhotoIndex;
        // Collections.reverse(imageArrayList); אם אני רוצה להציג את התמונות מהישן לחדש --- להפעיל את השורה הזאת!
        DataCenter.currentPhotoPath = DataCenter.imageArrayList.get(DataCenter.currentPhotoIndex).getImageUri().toString();

        imageView.setVisibility(View.VISIBLE);
        txt_title.setVisibility(View.VISIBLE);

        txt_title.setText(DataCenter.currentPhotoPath + "\n" + DataCenter.imageArrayList.size() + "/" + DataCenter.currentPhotoIndex);
        imageView.setImageURI(Uri.fromFile(new File(DataCenter.currentPhotoPath)));
    }

    private ArrayList<Image> getImage() {
        ArrayList<Image> imageList = new ArrayList<>();
        ContentResolver imageResolver = getContext().getContentResolver();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = imageResolver.query(imageUri, null, null, null, null);
        if (imageCursor != null && imageCursor.moveToFirst()) {
            // path uri
            int imageCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //content uri
            long id = imageCursor.getLong(imageCursor.getColumnIndexOrThrow(BaseColumns._ID));
            do {
                String pathId = imageCursor.getString(imageCol);
                Uri uri = Uri.parse(pathId);
                Uri cUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
               // Log.d("###qq", String.valueOf(cUri));
                //Log.d("###qq", String.valueOf(uri));
                imageList.add(new Image(uri));
            }
            while (imageCursor.moveToNext());
        }
        return imageList;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_StoragePermission) {
            askStoragePermission();
            if (Environment.isExternalStorageManager()) {
                btn_StoragePermission.setVisibility(View.GONE);
                showExternalStorage();
            }
        }
        if (v == imageView) {
            DataCenter.currentPhotoIndex--;
            DataCenter.currentPhotoPath = DataCenter.imageArrayList.get(DataCenter.currentPhotoIndex).getImageUri().toString();
            imageView.setImageURI(Uri.fromFile(new File(DataCenter.currentPhotoPath)));
            txt_title.setText(DataCenter.currentPhotoPath + "\n" + DataCenter.imageArrayList.size() +  "/"
                    + DataCenter.currentPhotoIndex);
        }
    }

}