package com.example.velimiratanasovski.contacts.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Contact implements Parcelable {

    private int id;
    private String name;
    private String lastname;
    private String address;
    private String phoneNumber;

    public Contact(String name, String lastname, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lastname = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(lastname);
        dest.writeString(address);
        dest.writeString(phoneNumber);
    }
}
