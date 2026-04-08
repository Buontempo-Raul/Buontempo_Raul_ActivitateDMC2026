package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.Date;

public class Bere implements Parcelable, Serializable {

    private static final long serialVersionUID = 1L;

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

    protected Bere(Parcel in) {
        nume = in.readString();
        cantitate = in.readInt();
        alcoholica = in.readByte() != 0;
        rating = in.readDouble();
        tip = Type.valueOf(in.readString());
        dataProductie = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeInt(cantitate);
        dest.writeByte((byte) (alcoholica ? 1 : 0));
        dest.writeDouble(rating);
        dest.writeString(tip.name());
        dest.writeLong(dataProductie != null ? dataProductie.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bere> CREATOR = new Creator<Bere>() {
        @Override
        public Bere createFromParcel(Parcel in) {
            return new Bere(in);
        }

        @Override
        public Bere[] newArray(int size) {
            return new Bere[size];
        }
    };

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
