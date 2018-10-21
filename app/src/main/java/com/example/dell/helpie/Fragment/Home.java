package com.example.dell.helpie.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.helpie.Activities.PreDetails;
import com.example.dell.helpie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private TextView fire,flood,earthQuake,hurricane,tornado;
    private FloatingActionButton fabAdd;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fire = (TextView)root.findViewById(R.id.txtFire);
        flood = (TextView)root.findViewById(R.id.txtFlood);
        earthQuake = (TextView)root.findViewById(R.id.txtEarthQuake);
        hurricane = (TextView)root.findViewById(R.id.txtHurricane);
        tornado = (TextView)root.findViewById(R.id.txtTornado);


        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetails = new Intent(getContext(),PreDetails.class);
                showDetails.putExtra("name","fire");
                getContext().startActivity(showDetails);
            }
        });


        hurricane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetails = new Intent(getContext(),PreDetails.class);
                showDetails.putExtra("name","hurricane");
                getContext().startActivity(showDetails);
            }
        });


        flood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetails = new Intent(getContext(),PreDetails.class);
                showDetails.putExtra("name","flood");
                getContext().startActivity(showDetails);
            }
        });


        earthQuake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetails = new Intent(getContext(),PreDetails.class);
                showDetails.putExtra("","earthquake");
                getContext().startActivity(showDetails);
            }
        });

        tornado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetails = new Intent(getContext(),PreDetails.class);
                showDetails.putExtra("","");
                getContext().startActivity(showDetails);
            }
        });





        return root;
    }

}
