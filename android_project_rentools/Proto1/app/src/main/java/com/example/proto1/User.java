package com.example.proto1;

public class User{
    public enum Role{
        CUSTOMER,
        OWNER
    }

    private int userID;
    private Email userEmail;
    private String firstName;
    private String secondName;
    private byte[] userImage;
    private Role userRole;
    private int userAge;
    private String userGender;
    private int userTelephone;

    public User(int userID, Email userEmail, String firstName, String secondName, byte[] userImage,String role,int age,String gender,int telephone) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userImage = userImage;
        decideRole(role);
        this.userAge = age;
        this.userGender = gender;
        this.userTelephone = telephone;
    }

    public Role getUserRole() {
        return userRole;
    }

    public User() {
    }

    public Email getUserEmail() {
        return userEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public byte[] getImage() {
        return userImage;
    }

    public int getUserID() {
        return userID;
    }

    private void decideRole(String role){
        switch (role){
            case "CUSTOMER":
                userRole = Role.CUSTOMER;
                break;
            case "OWNER":
                userRole = Role.OWNER;
                break;
            default:
                userRole = null;
                break;
        }
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public int getUserTelephone() {
        return userTelephone;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserTelephone(int userTelephone) {
        this.userTelephone = userTelephone;
    }
}
