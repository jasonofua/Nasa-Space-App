package com.example.dell.helpie.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.couchbase.lite.Database;
import com.example.dell.helpie.Model.DataHelper;
import com.example.dell.helpie.Model.User;
import com.example.dell.helpie.R;
import com.example.dell.helpie.Utils.ImageUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import mehdi.sakout.fancybuttons.FancyButton;

public class AddDetails extends AppCompatActivity {

    private TextInputLayout tilName,tilNumber;
    private EditText edtName,edtNUmber;
    private  String name,number,state,maritalStatus;
    ArrayList<com.nguyenhoanglam.imagepicker.model.Image> images;
    private MaterialSpinner spinnerState;
    private MaterialSpinner maritalStatusSpinner;
    private ImageView imageUser;

    private static final int REQUEST_CODE_PICKER = 0x8;
    private User user;
    private FancyButton saveDetails;


    private static final String[] spinnerStatess = {
            "State",
            "Abia" ,
            "Adamawa",
            "Akwa Ibom",
            "Anambra",
            "Bauchi",
            "Bayelsa",
            "Benue",
            "Borno",
            "Cross" ,
            "Delta",
            "Ebonyi",
            "Edo",
            "Ekiti",
            "Enugu",
            "Gombe",
            "Imo",
            "Jigawa",
            "Kaduna",
            "Kano",
            "Katsina",
            "Kebbi",
            "Kogi",
            "Kwara",
            "Lagos",
            "Nasarawa",
            "Niger",
            "Ogun",
            "Ondo",
            "Osun",
            "Oyo",
            "Plateau",
            "Rivers",
            "Sokoto",
            "Taraba",
            "Yobe",
            "Zamfara",
            "Abuja"


    };

    private static final String[] marital = {
            "Marital Status",
            "Single",
            "Married"

    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        tilName = (TextInputLayout)findViewById(R.id.tilFullName);
        tilNumber = (TextInputLayout)findViewById(R.id.tilNumber);
        edtName = (EditText)findViewById(R.id.edtFullName);
        edtNUmber = (EditText)findViewById(R.id.edtNumber);
        saveDetails = (FancyButton)findViewById(R.id.btnAddDetails);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        imageUser = (ImageView)findViewById(R.id.imgUser);
        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserImage();
            }
        });
        spinnerState = (MaterialSpinner) findViewById(R.id.spinnerStates);
        spinnerState.setItems(spinnerStatess);

        spinnerState.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                state = item;
            }
        });
        spinnerState.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });



        maritalStatusSpinner = (MaterialSpinner) findViewById(R.id.spinnerStatus);
        maritalStatusSpinner.setItems(marital);

        maritalStatusSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                maritalStatus = item;
            }
        });
        maritalStatusSpinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });




    }

    private void setUserImage() {
        ImagePicker.with(this)
                .setLimitMessage("You have reached selection limit")
                .setMultipleMode(true)
                .setImageTitle("Select your profile picture")
                .setDoneTitle("Done")
                .setMaxSize(1)
                .setAlwaysShowDoneButton(true)
                .setKeepScreenOn(true)
                .setCameraOnly(false)
                .start();

        // start image picker activity with request code
    }

    private File getImagesFolder() {
        File file = new File(getFilesDir(), "user-images");
        if (!file.exists() || !file.isDirectory())
        {
            boolean createDir = file.mkdir();
            if (!createDir)
                Toast.makeText(AddDetails.this, "Error", Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images.size() > 0){
                imageUser.setImageBitmap(BitmapFactory.decodeFile(images.get(0).getPath()));
            }
        }
    }

    public void saveUserInfo(){

        name = edtName.getText().toString();
        number = edtNUmber.getText().toString();


        if (TextUtils.isEmpty(name)){
            tilName.setError("This field is actually required");
            return;
        }

        if (TextUtils.isEmpty(number)){
            tilName.setError("This field is actually required");
            return;
        }

        if (TextUtils.isEmpty(state)){
            Toast.makeText(this, "This field is actually required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(maritalStatus)){
            Toast.makeText(this, "This field is actually required", Toast.LENGTH_SHORT).show();
            return;
        }


        // saving to student database
        Database database = DataHelper.getDatabase(getApplicationContext(), DataHelper.USERDATA);

        try {
            user = new User();
            user.setName(name);
            user.setMaritalStutus(maritalStatus);
            user.setState(state);
            user.setNumber(number);

            if (images != null && images.size() > 0){
                String sourceImagePath = images.get(0).getPath();
                user.setImageUrl(processImage(sourceImagePath));
            }

            user.saveToDatabase(AddDetails.this, database);
            Toast.makeText(this, "User Data uploaded successfully", Toast.LENGTH_SHORT).show();
            edtNUmber.setText("");
            edtName.setText("");
            saveDetails.setEnabled(false);


        }catch (Exception e){
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }


    private String processImage(String path) {

        String filenameArray[] = path.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        String newFileName = "student" + String.valueOf(Calendar.getInstance().getTimeInMillis())
                + extension;
        //save image to app internal storage folder
        File destFile = new File(getImagesFolder(), newFileName);
        String destImagePath = destFile.getPath();

        if (ImageUtils.resizeUploadImage(path, destImagePath))
            return destImagePath;
        else
            return null;
    }

}
