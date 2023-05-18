package com.example.database;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddItem addItem = new AddItem();
        ItemViewFragment itemView = new ItemViewFragment();
        changeFragment(addItem,"addItem");
        bottomNav = findViewById(R.id.bottomNav);
        FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_addItem:
                        if(fragmentManager.findFragmentByTag("addItem")!=null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("showItem")).commit();
                            showFragment(fragmentManager.findFragmentByTag("addItem"));
                        }
                        else{
                            if(fragmentManager.findFragmentByTag("showItem")!=null)
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("showItem")).commit();
                            changeFragment(addItem,"addItem");
                        }
                        break;
                    case R.id.menu_viewitem:
                        if(fragmentManager.findFragmentByTag("showItem")!=null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("addItem")).commit();
                           showFragment(fragmentManager.findFragmentByTag("showItem"));
                           Log.d("Fragment is founded ","fragment show Item");
                        }
                        else{
                            if(fragmentManager.findFragmentByTag("addItem")!=null)
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("addItem")).commit();
                            changeFragment(itemView,"showItem");
                        }
                        break;
                }
                return true;
            }
        });

    }

    private void changeFragment(Fragment fragment,String tag) {
         transaction  = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,fragment,tag).commit();
    }
    private void showFragment(Fragment fragment){
         transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(fragment).commit();

    }


   }