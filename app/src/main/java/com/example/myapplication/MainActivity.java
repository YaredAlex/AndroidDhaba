package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        navigationView = findViewById(R.id.menu_navigation);
        HomeFragment homeFragment = new HomeFragment();
        PersonalFragment personalFragment = new PersonalFragment();
        CustomizeFragment customizeFragment = new CustomizeFragment();
        changeFragment(homeFragment,"home");
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        changeFragment(homeFragment,"home");
                        break;
                    case R.id.menu_person:
                        changeFragment(personalFragment,"person");
                        break;
                    case R.id.menu_custom:
                        changeFragment(customizeFragment,"custom");
                        break;
                }
                return true;
            }
        });
    }
    void changeFragment(Fragment fragment,String tag){

        if(fragmentManager.findFragmentByTag("person")!=null){
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("person")).commit();
        }
        if(fragmentManager.findFragmentByTag("home")!=null){
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
        }
        if (fragmentManager.findFragmentByTag("custom")!=null){
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("custom")).commit();
        }
        if(fragmentManager.findFragmentByTag(tag)!=null){
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(tag)).setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).commit();
        }
        else{
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment,tag).commit();
        }

    }
}