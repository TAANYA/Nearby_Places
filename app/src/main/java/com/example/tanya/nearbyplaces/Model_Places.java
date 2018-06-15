package com.example.tanya.nearbyplaces;

public class Model_Places
{
    private double lat, lng;
    private String name , icon;

    public Model_Places(double lat, double lng, String name, String icon) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.icon = icon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
