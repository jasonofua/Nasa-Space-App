package com.example.dell.helpie.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class User extends BaseModel implements Parcelable {

    private String name,number,maritalStutus,state,id,imageUrl;


    public User() {
    }



    protected User(Parcel in) {
        name = in.readString();
        number = in.readString();
        maritalStutus = in.readString();
        state = in.readString();
        id = in.readString();
        imageUrl = in.readString();
    }

    public static User fromDictionary(Object dictionary){
        return fromDictionary(dictionary,User.class);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(maritalStutus);
        dest.writeString(state);
        dest.writeString(id);
        dest.writeString(imageUrl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMaritalStutus() {
        return maritalStutus;
    }

    public void setMaritalStutus(String maritalStutus) {
        this.maritalStutus = maritalStutus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void saveToDatabase(final AppCompatActivity activity, final Database database){

        if (database == null)
        {
            Toast.makeText(activity, "Cannot to save to store. Database unavailable.", Toast.LENGTH_SHORT).show();
            return;
        }

        Document userDocument;
        Map<String, Object> properties;

        if (TextUtils.isEmpty(this.getId())){
            //new style
            userDocument  = database.createDocument();
            this.setId(userDocument.getId());
            properties = this.toDictionary();
        }
        else{
            userDocument = database.getDocument(this.getId());
            properties = new HashMap<>();
            properties.putAll(userDocument.getProperties());
            properties.putAll(this.toDictionary());
        }

        try {
            userDocument.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Failed to save to store. Fatal error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

}
