package com.example.proto1;

public class Email{
    private final String emailAddress;
    private final String emailPassword;

    public Email(String emailAddress, String emailPassword) {
        this.emailAddress = emailAddress;
        this.emailPassword = emailPassword;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getEmailPassword() {
        return emailPassword;
    }
}
