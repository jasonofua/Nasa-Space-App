package com.example.dell.helpie.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.example.dell.helpie.Model.DataHelper;
import com.example.dell.helpie.Model.ListModel;
import com.example.dell.helpie.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mehdi.sakout.fancybuttons.FancyButton;

public class CustomList extends AppCompatActivity {
    private MaterialSpinner spinnerPriority;
    private RecyclerView rvList;

    private TextInputLayout tilName;
    private EditText edtName;
    private FancyButton saveDetails;
    private String priLevel,listName;
    private StudentAdapter adapter;
    private String Databases;
    Database database;
    ArrayList<ListModel> studentsModelArrayList;

    private static final String[] priorities = {
            "Priority",
            "High",
            "Low",
            "Not Important"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        rvList = (RecyclerView)findViewById(R.id.rvList);
        Databases = getIntent().getStringExtra("dbName");

        if (Databases.equalsIgnoreCase("fire")){
            database = DataHelper.getDatabase(getApplicationContext(), DataHelper.FIRE);
        }else if (Databases.equalsIgnoreCase("flood")){
            database = DataHelper.getDatabase(getApplicationContext(), DataHelper.FLOOD);
        }else if (Databases.equalsIgnoreCase("earthquake")){
            database = DataHelper.getDatabase(getApplicationContext(), DataHelper.EARTHQUAKE);
        }else if (Databases.equalsIgnoreCase("hurricane")){
            database = DataHelper.getDatabase(getApplicationContext(), DataHelper.HURRICANE);
        }else if (Databases.equalsIgnoreCase("tornado")){
            database = DataHelper.getDatabase(getApplicationContext(), DataHelper.TORNADO);
        }

        getAllCustomers();





//        tilName = (TextInputLayout)findViewById(R.id.tilNameList);
//        edtName = (EditText)findViewById(R.id.edtListName);
//
//        spinnerPriority = (MaterialSpinner) findViewById(R.id.spinnerPriority);
//        spinnerPriority.setItems(priorities);
//
//        spinnerPriority.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                priLevel = item;
//            }
//        });
//        spinnerPriority.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
//
//            @Override public void onNothingSelected(MaterialSpinner spinner) {
//                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
//            }
//        });
//
        saveDetails = (FancyButton)findViewById(R.id.btnAddList);
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveItem();

            }
        });


    }

    private void getAllCustomers() {

        rvList = (RecyclerView) findViewById(R.id.rvList);
        rvList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CustomList.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        if (database == null)
            return;

        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS); //ALL_DOCS by id, BY_SEQUENCE by last modified

        try {
            QueryEnumerator result = query.run();
            studentsModelArrayList = new ArrayList<>();

            for (; result.hasNext(); ) {
                QueryRow row = result.next();
                ListModel customer = ListModel.fromDictionary(row.getDocument().getProperties());
                studentsModelArrayList.add(customer);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Toast.makeText(CustomList.this, "Get customers info failed", Toast.LENGTH_SHORT).show();
        }
        adapter = new StudentAdapter(studentsModelArrayList);
        rvList.setAdapter(adapter);

    }
    private void saveItem() {
        final Dialog dialog = new Dialog(CustomList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_layout);
        tilName = (TextInputLayout)dialog.findViewById(R.id.tilListName);
        edtName = (EditText)dialog.findViewById(R.id.edtListName);

        spinnerPriority = (MaterialSpinner)dialog.findViewById(R.id.spinnerPriority);
        spinnerPriority.setItems(priorities);

        spinnerPriority.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                priLevel = item;
            }
        });
        spinnerPriority.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        FancyButton add = (FancyButton)dialog.findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listName = edtName.getText().toString().trim();
                save(listName,priLevel);
                dialog.dismiss();
            }
        });

        dialog.show();





    }

    private void save(String listName,String priLevel) {
        if (TextUtils.isEmpty(listName)){
            tilName.setError("This field is actually required");
            return;
        }


        if (TextUtils.isEmpty(priLevel)){
            Toast.makeText(this, "This field is actually required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ListModel model = new ListModel();
            model.setName(listName);
            model.setPriority(priLevel);
            model.saveToDatabase(CustomList.this,database);

        }catch (Exception e){
            Toast.makeText(this, "Could not save item", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCustomers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllCustomers();
    }
}
