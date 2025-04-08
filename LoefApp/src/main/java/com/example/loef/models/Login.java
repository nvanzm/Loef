package com.example.loef.models;

public class Login {
    private String gebruikersnaam;
    private String wachtwoord;

    // Constructor
    public Login(String gebruikersnaam, String wachtwoord) {
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
    }

    // Getters en Setters
    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String validate() {
        if (gebruikersnaam.isEmpty() || wachtwoord.isEmpty()) {
            return "Vul alle velden in.";
        } else if (gebruikersnaam.equals("noach") && wachtwoord.equals("123")) {
            return "Logging in...";
        } else if (gebruikersnaam.equals("admin") && wachtwoord.equals("admin")) {
            return "Welkom, Admin!";
        } else {
            return "Ongeldige gebruikersnaam of wachtwoord.";
        }
    }
}
