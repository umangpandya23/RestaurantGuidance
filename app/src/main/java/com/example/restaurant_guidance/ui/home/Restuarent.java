package com.example.restaurant_guidance.ui.home;

public class Restuarent {

    //Fields for restuarent
    private String name;
    private String address;
    private double lat;
    private double log;
    private String contact_number;
    private String url;
    private String info;
    private String city;
    private String state;
    private String zipcode;
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public Restuarent(){

    }

    public Restuarent(String name, String address, String contact_number, String url, String info, String city, String state, String zipcode, double lat, double log) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.log = log;
        this.contact_number = contact_number;
        this.url = url;
        this.info = info;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
