package com.example.loef.models;

import java.util.ArrayList;
import java.util.List;

public class UrenAdministratie {
    private final List<Werknemer> werknemers = new ArrayList<>();
    private final List<DataUrenItem> alleUren = new ArrayList<>();

    public void voegWerknemerToe(Werknemer werknemer) {
        werknemers.add(werknemer);
    }

    public void voegUrenToe(DataUrenItem item) {
        alleUren.add(item);
        if (item.getWerknemer() != null) {
            item.getWerknemer().voegUrenItemToe(item);
        }
    }

    public List<Werknemer> getWerknemers() {
        return werknemers;
    }

    public List<DataUrenItem> getAlleUren() {
        return alleUren;
    }

    public double getTotaalUren() {
        return alleUren.stream().mapToDouble(DataUrenItem::getUren).sum();
    }

    public double getTotaalLoon() {
        return werknemers.stream().mapToDouble(Werknemer::getLoon).sum();
    }
}
