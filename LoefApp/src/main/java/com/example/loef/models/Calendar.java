package com.example.loef.models;

import java.time.ZonedDateTime;

public class Calendar {
    private ZonedDateTime date;
    private String clientName;
    private Integer serviceNo;

    private Werknemer werknemer;

    public Calendar(ZonedDateTime date, String clientName, Integer serviceNo, Werknemer werknemer) {
        this.date = date;
        this.clientName = clientName;
        this.serviceNo = serviceNo;
        this.werknemer = werknemer;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(Integer serviceNo) {
        this.serviceNo = serviceNo;
    }

    public Werknemer getWerknemer() {
        return werknemer;
    }

    public void setWerknemer(Werknemer werknemer) {
        this.werknemer = werknemer;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", clientName='" + clientName + '\'' +
                ", serviceNo=" + serviceNo +
                ", werknemer=" + (werknemer != null ? werknemer.getNaam() : "Onbekend") +
                '}';
    }
}
