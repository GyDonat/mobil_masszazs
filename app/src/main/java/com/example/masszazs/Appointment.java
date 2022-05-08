package com.example.masszazs;

public class Appointment {
    private String email;
    private String idopont;

    public Appointment(String email, String idopont) {
        this.email=email;
        this.idopont=idopont;
    }

    public String getEmail() {
        return email;
    }

    public String getIdopont() {
        return idopont;
    }
}
