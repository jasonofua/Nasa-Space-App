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

public class Chat extends BaseModel implements Parcelable {

    private String message,sender,id;

    public Chat() {
    }

    protected Chat(Parcel in) {
        message = in.readString();
        sender = in.readString();
        id = in.readString();

    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(message);
        dest.writeString(sender);
        dest.writeString(id);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
