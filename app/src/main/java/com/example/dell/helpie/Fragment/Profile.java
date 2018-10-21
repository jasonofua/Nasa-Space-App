package com.example.dell.helpie.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.example.dell.helpie.Activities.AddDetails;
import com.example.dell.helpie.Model.DataHelper;
import com.example.dell.helpie.Model.User;
import com.example.dell.helpie.R;

import androidx.fragment.app.Fragment;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    Database database;
    private ImageView profileImage;
    private TextView txtName,txtNumber,txtState,txtStatus;
    private FancyButton editProfile;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String NAME = "name";
    SharedPreferences sharedpreferences;



    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        database = DataHelper.getDatabase(getContext(), DataHelper.USERDATA);
        profileImage = (ImageView)root.findViewById(R.id.imgUserProfile);
        txtName = (TextView)root.findViewById(R.id.name);
        txtNumber = (TextView)root.findViewById(R.id.number);
        txtState = (TextView)root.findViewById(R.id.state);
        txtStatus = (TextView)root.findViewById(R.id.maritalStatus);



        editProfile = (FancyButton)root.findViewById(R.id.btnEditPRo);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(),AddDetails.class));
            }
        });
        getProfile();


        return root;
    }

    private void getProfile() {

        if (database == null)
            return;
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS); //ALL_DOCS by id, BY_SEQUENCE by last modified

        try {
            QueryEnumerator result = query.run();

            for (; result.hasNext(); ) {
                QueryRow row = result.next();
                User customer = User.fromDictionary(row.getDocument().getProperties());
               txtName.setText(customer.getName());
               txtNumber.setText(customer.getNumber());
               txtState.setText(customer.getState());
               txtStatus.setText(customer.getMaritalStutus());
                profileImage.setImageBitmap(BitmapFactory.decodeFile(customer.getImageUrl()));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(NAME, customer.getName());
                editor.apply();

            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Get customers info failed", Toast.LENGTH_SHORT).show();
        }
    }

}
