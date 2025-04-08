package com.example.loef.models;

public abstract class Persoon {
    protected String naam;

    public Persoon(String naam) {
        this.naam = naam;
    }

    public String getNaam() {
        return naam;
    }

    /**
     * Methode die overschreven moet worden door subklassen.
     * @return het berekende loon
     */
    public abstract double berekenLoon();
}
