package com.example.dell.helpie.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.dell.helpie.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import mehdi.sakout.fancybuttons.FancyButton;

public class PreDetails extends AppCompatActivity {

    private CardView document,water,cloth;
    private NestedScrollView root;
    private CheckBox documentCheck,waterCheck,clothCheck;
    private int load = 0;
    private FancyButton save;
    private boolean isClicked = false;
    private String name;
    private CollapsingToolbarLayout collapsingToolbarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra("name");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(name);

        document = (CardView) findViewById(R.id.documentCard);
        water = (CardView) findViewById(R.id.waterCard);
        cloth = (CardView) findViewById(R.id.clothCard);
        documentCheck = (CheckBox)findViewById(R.id.checkDocuments);
        waterCheck = (CheckBox)findViewById(R.id.checkWaterFood);
        clothCheck = (CheckBox)findViewById(R.id.checkCloths);
        root = (NestedScrollView)findViewById(R.id.nested);
        save = (FancyButton)findViewById(R.id.btnCompleteList);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (load <3){
                    Snackbar.make(root, "Your Go-Bag is not filled ensure you take everything", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    Snackbar.make(root, "Your Go-Bag is Completely packed, you are set to go", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                documentCheck.setChecked(true);
                load += 1;
                documentCheck.setEnabled(false);
                document.setEnabled(false);

            }
        });


        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCheck.setChecked(true);
                load += 1;
                waterCheck.setEnabled(false);
                water.setEnabled(false);

            }
        });


        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clothCheck.setChecked(true);
                clothCheck.setEnabled(false);
                load += 1;
                cloth.setEnabled(false);


            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.equalsIgnoreCase("fire")) {
                    Intent addCustom = new Intent(PreDetails.this, CustomList.class);
                    addCustom.putExtra("dbName", "fire");
                    startActivity(addCustom);
                }else if (name.equalsIgnoreCase("flood")){
                    Intent addCustom = new Intent(PreDetails.this, CustomList.class);
                    addCustom.putExtra("dbName", "flood");
                    startActivity(addCustom);

                }else if (name.equalsIgnoreCase("earthquake")){
                    Intent addCustom = new Intent(PreDetails.this, CustomList.class);
                    addCustom.putExtra("dbName", "earthquake");
                    startActivity(addCustom);

                }else if (name.equalsIgnoreCase("hurricane")){
                    Intent addCustom = new Intent(PreDetails.this, CustomList.class);
                    addCustom.putExtra("dbName", "hurricane");
                    startActivity(addCustom);

                }else if (name.equalsIgnoreCase("tornado")){
                    Intent addCustom = new Intent(PreDetails.this, CustomList.class);
                    addCustom.putExtra("dbName", "tornado");
                    startActivity(addCustom);

                }
            }
        });
    }
}
