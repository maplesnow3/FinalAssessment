package com.toilet;

public class Toilet {
    public boolean  isDamage;
    public double latitude;
    public double longitude;
    public String name;
    public int toiletId;

    public Toilet() {

    }

    public boolean isDamage() {
        return isDamage;
    }

    public void setDamage(boolean damage) {
        isDamage = damage;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToiletId() {
        return toiletId;
    }

    public void setToiletId(int toiletId) {
        this.toiletId = toiletId;
    }
}
