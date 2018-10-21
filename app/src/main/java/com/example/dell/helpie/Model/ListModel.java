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

public class ListModel extends BaseModel implements Parcelable {


    private String name,priority,id;

    private int checked;



    public ListModel() {
    }

    protected ListModel(Parcel in) {
        name = in.readString();
        priority = in.readString();
        checked = in.readInt();
        id  = in.readString();
    }

    public static ListModel fromDictionary(Object dictionary){
        return fromDictionary(dictionary,ListModel.class);
    }

    public static final Creator<ListModel> CREATOR = new Creator<ListModel>() {
        @Override
        public ListModel createFromParcel(Parcel in) {
            return new ListModel(in);
        }

        @Override
        public ListModel[] newArray(int size) {
            return new ListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    dest.writeInt(checked);
    dest.writeString(priority);
    dest.writeString(id);
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
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
