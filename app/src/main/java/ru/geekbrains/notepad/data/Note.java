package ru.geekbrains.notepad.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class Note implements Parcelable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Date date;
    private final String name;
    private final String description;
    private final int position;
    private final boolean done;

    public Note(String name, String description, int position, Date date) {
        this.date = date;
        this.name = name;
        this.description = description;
        this.position = position;
        this.done = false;
    }

    protected Note(Parcel in) {
        name = in.readString();
        description = in.readString();
        position = in.readInt();
        done = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(position);
        parcel.writeByte((byte) (done ? 1 : 0));
    }
}
