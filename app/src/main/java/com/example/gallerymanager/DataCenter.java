package com.example.gallerymanager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class DataCenter {
    public static ArrayList<Image> imageArrayList = new ArrayList<>();
    public static ArrayList<Image> deleted_imageArrayList = new ArrayList<>();

    public static int currentPhotoIndex = 0;
    public static String currentPhotoPath;



}
