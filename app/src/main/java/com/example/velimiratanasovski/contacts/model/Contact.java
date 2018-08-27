package com.example.velimiratanasovski.contacts.model;

import android.os.Parcel;
import android.os.Parcelable;
public class Contact implements Parcelable {

    private int id;
    private String name;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String eMail;

    public Contact(String name, String lastName, String address, String phoneNumber, String eMail) {
        this.name = name;
        this.address = address;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lastName = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        eMail = in.readString();
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

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return eMail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(eMail);
    }
}
