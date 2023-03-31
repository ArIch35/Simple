package com.example.proto1;

import java.util.ArrayList;
import java.util.List;


public class House{
    private int houseID;
    private String houseAddress;
    private String city;
    private String description;
    private User itemOwner;
    private int housePrice;
    private String availableDate;
    private int rentDuration;
    private int houseSize;
    private boolean hasUtilities;
    private boolean isFurnished;
    private ArrayList<byte[]> houseImage = new ArrayList<>();

    public String getAvailableDate() {
        return availableDate;
    }

    public boolean hasUtilities() {
        return hasUtilities;
    }

    public boolean isFurnished() {
        return isFurnished;
    }

    public int getHouseSize() {
        return houseSize;
    }

    public House(int houseID, String houseAddress, String city, String description,
                 User itemOwner, int housePrice, String availableDate, int rentDuration,
                 int houseSize, boolean hasUtilities, boolean isFurnished,ArrayList<byte[]> houseImage) {
        this.houseID = houseID;
        this.houseAddress = houseAddress;
        this.city = city;
        this.description = description;
        this.itemOwner = itemOwner;
        this.housePrice = housePrice;
        this.availableDate = availableDate;
        this.rentDuration = rentDuration;
        this.houseSize = houseSize;
        this.hasUtilities = hasUtilities;
        this.isFurnished = isFurnished;
        this.houseImage = houseImage;
    }

    public House() {
    }

    public User getItemOwner() {
        return itemOwner;
    }

    public int getHouseID() {
        return houseID;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public List<byte[]> getHouseImage() {
        return houseImage;
    }

    @Override
    public boolean equals(Object this_object) {
        if (this_object == this) {
            return true;
        }

        if (!(this_object instanceof House)) {
            return false;
        }

        House this_item = (House) this_object;

        return this_item.getHouseID() == houseID;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getRentDuration() {
        return rentDuration;
    }
}
