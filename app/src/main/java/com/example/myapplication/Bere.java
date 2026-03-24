package com.example.myapplication;

import java.io.Serializable;
import java.util.Date;

public class Bere implements Serializable {

    public enum Type {
        BLONDA, NEAGRA, NEFILTRATA, IPA, LAGER
    }

    private String nume;
    private int cantitate; // ml
    private boolean alcoholica;
    private double rating;
    private Type tip;
    private Date dataProductie;

    public Bere() {
    }

    public Bere(String nume, int cantitate, boolean alcoholica, double rating, Type tip, Date dataProductie) {
        this.nume = nume;
        this.cantitate = cantitate;
        this.alcoholica = alcoholica;
        this.rating = rating;
        this.tip = tip;
        this.dataProductie = dataProductie;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public boolean isAlcoholica() {
        return alcoholica;
    }

    public void setAlcoholica(boolean alcoholica) {
        this.alcoholica = alcoholica;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Type getTip() {
        return tip;
    }

    public void setTip(Type tip) {
        this.tip = tip;
    }

    public Date getDataProductie() {
        return dataProductie;
    }

    public void setDataProductie(Date dataProductie) {
        this.dataProductie = dataProductie;
    }

    @Override
    public String toString() {
        return "Bere{" +
                "nume='" + nume + '\'' +
                ", cantitate=" + cantitate +
                ", alcoholica=" + alcoholica +
                ", rating=" + rating +
                ", tip=" + tip +
                ", dataProductie=" + dataProductie +
                '}';
    }
}
