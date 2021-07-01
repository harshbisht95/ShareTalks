package com.example.sharetalks.Bean;

import java.util.Map;

public class UserDetails {
    String Username;
    String PhoneNumber;
    String Location;
    String Rank;

    public UserDetails() { }

    public UserDetails(String username, String phoneNumber, String location, String rank) {
        Username = username;
        PhoneNumber = phoneNumber;
        Location = location;
        Rank = rank;
    }

    public String getRank() { return Rank; }

    public void setRank(String rank) { Rank = rank; }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
