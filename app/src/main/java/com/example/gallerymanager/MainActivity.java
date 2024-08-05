package com.example.gallerymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    PhotosFragment photosFragment = new PhotosFragment();
    TrashBinFragment trashBinFragment = new TrashBinFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init_menu();
    }

    public void Init_menu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, photosFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.navigation_Photos == item.getItemId()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, photosFragment).commit();
                    return true;
                }
                if (R.id.navigation_Trash_Bin == item.getItemId()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, trashBinFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}