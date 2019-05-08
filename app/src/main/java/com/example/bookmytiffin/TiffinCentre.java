package com.example.bookmytiffin;

public class TiffinCentre {
    private String name;
    private String address;
    private int thumbnail;

    public TiffinCentre(String name, String address, int thumbnail) {
        this.name = name;
        this.address = address;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }
    public void changeText1(String text){
        address = text;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}



