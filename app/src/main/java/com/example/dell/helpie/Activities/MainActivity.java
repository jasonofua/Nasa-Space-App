package com.example.dell.helpie.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.dell.helpie.Fragment.Cha;
import com.example.dell.helpie.Fragment.Home;
import com.example.dell.helpie.Fragment.Profile;
import com.example.dell.helpie.Fragment.Settings;
import com.example.dell.helpie.R;
import com.example.dell.helpie.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    //This is our viewPager
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    fragment = new Home();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_gifts:
                    fragment = new Cha();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    fragment = new Profile();
                    loadFragment(fragment);


                    return true;
                case R.id.navigation_profile:
                    fragment = new Settings();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
