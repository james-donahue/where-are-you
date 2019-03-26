package edu.umw.cpsc.donahue.james.assignmentthree;

public class MyLocation {
    private String name;
    private double latitude;
    private double longitude;

    public MyLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String toString() {
        return "Name: " + name + " Latitude: " + latitude + " Longitude: " + longitude;
    }
}
